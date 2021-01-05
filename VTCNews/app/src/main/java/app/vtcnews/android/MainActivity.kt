package app.vtcnews.android

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import app.vtcnews.android.databinding.ActivityMainBinding
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import app.vtcnews.android.ui.trang_chu_sub_section.ArticlesFragment
import com.example.vtclive.Video.FragmentVideoPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var curFrag = "trang_chu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportFragmentManager.apply {
            addOnBackStackChangedListener {
                if (fragments.isEmpty()) return@addOnBackStackChangedListener
                when (fragments[0]) {
                    is TrangChuFragment -> binding.mainBottomNav.selectedItemId = R.id.menuTrangChu
                    is ArticlesFragment -> binding.mainBottomNav.selectedItemId = R.id.menuTrending
                    is AudioHomeFragment -> binding.mainBottomNav.selectedItemId = R.id.menuAudio
                    else -> Log.d("MainActivity", "Unknown Fragment type!")
                }
            }
        }

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
            }
            true
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
            binding.root,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.root.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //binding.toolbar.setNavigationIcon(R.drawable.ic_nav_drawer_24)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainNavDrawer.setNavigationItemSelectedListener {

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
            }

            binding.root.closeDrawer(GravityCompat.START)
            true
        }
    }


    private fun switchToTrangChu() {
        if (curFrag == "trang_chu") return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
            .addToBackStack(null).commit()
        curFrag = "trang_chu"
    }

    private fun switchToTrending() {
        if (curFrag == "trending") return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1))
            .addToBackStack(null).commit()
        curFrag = "trending"
    }

    private fun switchToAudio() {
        if (curFrag == "audio") return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, AudioHomeFragment.newInstance())
            .addToBackStack(null)
            .commit()
        curFrag = "audio"
    }

    fun switchToVideo() {
        if (curFrag == "video") return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, FragmentVideoPage.newInstance())
            .addToBackStack(null)
            .commit()
        curFrag = "video"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

}