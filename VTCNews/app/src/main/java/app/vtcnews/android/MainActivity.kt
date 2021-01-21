package app.vtcnews.android

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import app.vtcnews.android.databinding.ActivityMainBinding
import app.vtcnews.android.ui.article_detail_fragment.PlayerScreenMotionLayout
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.share.ShareFragment
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import app.vtcnews.android.ui.trang_chu_sub_section.ArticlesFragment
import com.example.vtclive.Video.FragmentVideoPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var curFrag = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.mainActivityLayout.isEnabled = false


            if (hasNetworkAvailable(this)) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_holder, TrangChuFragment.newInstance())
                    .commit()
                binding.mainBottomNav.setOnNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.menuTrangChu -> {
                            switchToTrangChu()
                        }

                        R.id.menuTrending -> {
                            switchToTrending()
                        }

                        R.id.menuAudio -> {
                            switchToAudio()
                        }
                        R.id.menuVideo -> {
                            switchToVideo()
                        }
                        R.id.menuShare -> {
                            switchToShare()
                        }
                    }
                    val fragment = supportFragmentManager.findFragmentByTag("fragVideo")
                    if (fragment != null) {
                        try {
                            val motionLaout = findViewById<PlayerScreenMotionLayout>(R.id.root_layout)
                            motionLaout.transitionToEnd()
                        }
                        catch (e: NullPointerException)
                        {}
                    }
                    if(supportFragmentManager.backStackEntryCount > 0)
                    {
                        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_hambug)
                        binding.toolbar.setNavigationOnClickListener {
                            setupNavDrawer()
                        }
                    }
                    true
                }
            } else {
                Toast.makeText(this, "Không có kết nối Internet", Toast.LENGTH_SHORT).show()
                binding.mainActivityLayout.isEnabled = true
                binding.mainActivityLayout.setOnRefreshListener {
                    binding.mainActivityLayout.isRefreshing = false
                    if (hasNetworkAvailable(this)) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragment_holder, TrangChuFragment.newInstance())
                            .commit()
                        binding.mainActivityLayout.isEnabled = false
                        binding.mainBottomNav.setOnNavigationItemSelectedListener {
                            when (it.itemId) {
                                R.id.menuTrangChu -> {
                                    switchToTrangChu()
                                }

                                R.id.menuTrending -> {
                                    switchToTrending()
                                }

                                R.id.menuAudio -> {
                                    switchToAudio()
                                }
                                R.id.menuVideo -> {
                                    switchToVideo()
                                }
                                R.id.menuShare -> {
                                    switchToShare()
                                }
                            }
                            val fragment = supportFragmentManager.findFragmentByTag("fragVideo")
                            if (fragment != null) {
                                try {
                                    val motionLaout = findViewById<PlayerScreenMotionLayout>(R.id.root_layout)
                                    motionLaout.transitionToEnd()
                                }
                                catch (e: NullPointerException)
                                {}
                            }
                            if(supportFragmentManager.backStackEntryCount > 0)
                            {
                                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                                binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_hambug)
                                binding.toolbar.setNavigationOnClickListener {
                                    setupNavDrawer()
                                }
                            }
                            true
                        }
                    }
                }
            }
        setupNavDrawer()
        supportFragmentManager.apply {
            addOnBackStackChangedListener {
                fragments.lastOrNull()?.let { curFragment ->
                    when (curFragment) {
                        is TrangChuFragment -> binding.mainBottomNav.menu[0].isChecked = true
                        is ArticlesFragment -> binding.mainBottomNav.menu[1].isChecked = true
                        is FragmentVideoPage -> binding.mainBottomNav.menu[2].isChecked = true
                        is AudioHomeFragment -> binding.mainBottomNav.menu[3].isChecked = true
                        is ShareFragment -> binding.mainBottomNav.menu[4].isChecked = true
                        else -> Log.d("MainActivity", "Unknown Fragment type!")
                    }
                }
                if (supportFragmentManager.backStackEntryCount == 0) {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_hambug)
                    binding.toolbar.setNavigationOnClickListener {
                        setupNavDrawer()
                    }
                } else {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
                    binding.toolbar.setNavigationOnClickListener {
                        onBackPressed()
                    }
                }
            }
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId)
//        {
//            R.id.app_bar_search ->
//            {
//              Toast.makeText(this,"se",Toast.LENGTH_SHORT).show()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        toggle.onConfigurationChanged(newConfig)
        binding.mainBottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuTrangChu -> {
                    switchToTrangChu()
                }

                R.id.menuTrending -> {
                    switchToTrending()
                }

                R.id.menuAudio -> {
                    switchToAudio()
                }
                R.id.menuVideo -> {
                    switchToVideo()
                }
                R.id.menuShare -> {
                    switchToShare()
                }
            }
            val fragment = supportFragmentManager.findFragmentByTag("fragVideo")
            if (fragment != null) {
                try {
                    val motionLaout = findViewById<PlayerScreenMotionLayout>(R.id.root_layout)
                    motionLaout.transitionToEnd()
                }
                catch (e: NullPointerException)
                {}
            }
            if(supportFragmentManager.backStackEntryCount > 0)
            {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_hambug)
                binding.toolbar.setNavigationOnClickListener {
                    setupNavDrawer()
                }
            }
            true
        }
    }

    private fun setupNavDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        binding.mainNavDrawer.setNavigationItemSelectedListener {
            if (hasNetworkAvailable(this)) {
                when (it.itemId) {
                    R.id.item_nav_drawer_trang_chu -> {
                        switchToTrangChu()
                        binding.mainBottomNav.selectedItemId = R.id.menuTrangChu
                    }

                    R.id.item_nav_drawer_trending -> {
                        switchToTrending()
                        binding.mainBottomNav.selectedItemId = R.id.menuTrending
                    }

                    R.id.item_nav_drawer_audio -> {
                        switchToAudio()
                        binding.mainBottomNav.selectedItemId = R.id.menuAudio
                    }
                    R.id.item_nav_drawer_video -> {
                        switchToVideo()
                        binding.mainBottomNav.selectedItemId = R.id.menuVideo
                    }

                }
            } else {
                Toast.makeText(this, "Không có kết nối Internet", Toast.LENGTH_SHORT).show()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


    private fun switchToTrangChu() {
        if (curFrag == "home") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
                .commit()
            curFrag = "home"
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
                .commit()
            curFrag = "home"
        }

    }

    private fun switchToTrending() {

        if (curFrag == "trending") return

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1), "trending")
            .commit()
        curFrag = "trending"

    }

    private fun switchToAudio() {


        if (curFrag == "audio") return

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, AudioHomeFragment.newInstance())
            .commit()
        curFrag = "audio"

    }

    private fun switchToVideo() {

        if (curFrag == "video") return

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, FragmentVideoPage.newInstance())
            .commit()
        curFrag = "video"

    }

    private fun switchToShare() {
        if (curFrag == "share") return

//        supportFragmentManager.beginTransaction()
//            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
//            .replace(R.id.fragment_holder, CommentFragment.newInstance(574750))
//            .commit()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, ShareFragment.newInstance())
            .commit()
        curFrag = "share"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    private fun hasNetworkAvailable(context: Context): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }


}




