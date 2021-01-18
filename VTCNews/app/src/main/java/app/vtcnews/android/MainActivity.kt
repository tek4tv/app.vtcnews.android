package app.vtcnews.android

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import app.vtcnews.android.databinding.ActivityMainBinding
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.comment.CommentFragment
import app.vtcnews.android.ui.trang_chu.AllChannelFragment
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
        supportActionBar?.hide()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        binding.refreshlayout.setOnRefreshListener {
            if (hasNetworkAvailable(this)) {
                binding.tvNoInternet.visibility = View.GONE
                when (curFrag) {
                    "home" -> {
                        binding.refreshlayout.isRefreshing = false
                        switchToTrangChu()
                    }
                    "trending" -> {
                        binding.refreshlayout.isRefreshing = false
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1))
                            .addToBackStack(null)
                            .commit()
                    }
                    "audio" -> {
                        binding.refreshlayout.isRefreshing = false
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_holder, AudioHomeFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                    "video" -> {
                        binding.refreshlayout.isRefreshing = false
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_holder, FragmentVideoPage.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                    //"share" -> switchTo
                }
            } else {
                binding.refreshlayout.isRefreshing = false
            }
        }

        if (hasNetworkAvailable(this)) {
            binding.tvNoInternet.visibility = View.GONE
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
//                      switchToShare()
                    }
                }
                true
            }

            supportFragmentManager.apply {
                addOnBackStackChangedListener {
                    fragments.lastOrNull()?.let { curFragment ->
                        when (curFragment) {
                            is TrangChuFragment -> binding.mainBottomNav.menu[0].isChecked = true
                            is ArticlesFragment -> binding.mainBottomNav.menu[1].isChecked = true
                            is FragmentVideoPage -> binding.mainBottomNav.menu[2].isChecked = true
                            is AudioHomeFragment -> binding.mainBottomNav.menu[3].isChecked = true
                            else -> Log.d("MainActivity", "Unknown Fragment type!")
                        }
                    }
                }
            }
        } else {
            binding.tvNoInternet.isVisible = true
        }

        setupNavDrawer()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //binding.toolbar.setNavigationIcon(R.drawable.ic_nav_drawer_24)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1))
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

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
            .replace(R.id.fragment_holder, CommentFragment.newInstance(574750))
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




