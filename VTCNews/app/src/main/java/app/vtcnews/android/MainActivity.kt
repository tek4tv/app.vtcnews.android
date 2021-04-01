package app.vtcnews.android


import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import app.vtcnews.android.databinding.ActivityMainBinding
import app.vtcnews.android.model.NavDrawer.NavDrawerModel
import app.vtcnews.android.ui.Search.SearchFragment
import app.vtcnews.android.ui.audio.AudioWithoutTab
import app.vtcnews.android.ui.navdrawer.NavExpandAdapter
import app.vtcnews.android.ui.share.ShareFragment
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import app.vtcnews.android.ui.trang_chu_sub_section.ArticlesFragment
import app.vtcnews.android.ui.video.FragmentVideoPage
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.set


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var curFrag = "home"
    private var headerList: MutableList<NavDrawerModel> = ArrayList()
    private var childList: HashMap<NavDrawerModel, List<NavDrawerModel>> = HashMap()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.mainActivityLayout.isEnabled = false
        setUpDrawerMenu()
//        setupGGAnalytic()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "home")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)


        val bundle2 = Bundle()
        bundle2.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle2)

        if (hasNetworkAvailable(this)) {
            switchToTrangChu()
            binding.mainBottomNav.setOnNavigationItemSelectedListener {
//                val fragment = supportFragmentManager.findFragmentByTag("fragVideo")
//                if (fragment != null) {
//                    try {
//                        val motionLaout =
//                            findViewById<PlayerScreenMotionLayout>(R.id.root_layout)
//                        motionLaout.transitionToEnd()
//                    } catch (e: NullPointerException) {
//                    }
//                }
                if (supportFragmentManager.backStackEntryCount > 0) {
                    for (i in 0 until supportFragmentManager.backStackEntryCount) {
                        supportFragmentManager.popBackStackImmediate()
                    }
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.toolbar.navigationIcon = setUpHambug()
                    binding.toolbar.setNavigationOnClickListener {
                        toggle = ActionBarDrawerToggle(
                            this,
                            binding.drawerLayout,
                            binding.toolbar,
                            R.string.open_drawer,
                            R.string.close_drawer
                        )
                        binding.drawerLayout.addDrawerListener(toggle)
                        binding.drawerLayout.openDrawer(GravityCompat.START)
                    }
                }

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
//                        val fragment = supportFragmentManager.findFragmentByTag("fragVideo")
//                        if (fragment != null) {
//                            try {
//                                val motionLaout =
//                                    findViewById<PlayerScreenMotionLayout>(R.id.root_layout)
//                                motionLaout.transitionToEnd()
//                            } catch (e: NullPointerException) {
//                            }
//                        }
                        if (supportFragmentManager.backStackEntryCount > 0) {
                            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                                supportFragmentManager.popBackStack()
                            }
                            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                            binding.toolbar.navigationIcon = setUpHambug()
                            binding.toolbar.setNavigationOnClickListener {
                                toggle = ActionBarDrawerToggle(
                                    this,
                                    binding.drawerLayout,
                                    binding.toolbar,
                                    R.string.open_drawer,
                                    R.string.close_drawer
                                )
                                binding.drawerLayout.addDrawerListener(toggle)
                                binding.drawerLayout.openDrawer(GravityCompat.START)
                            }
                        }
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
                        is AudioWithoutTab -> binding.mainBottomNav.menu[3].isChecked = true
                        is ShareFragment -> binding.mainBottomNav.menu[4].isChecked = true
                        else -> Log.d("MainActivity", "Unknown Fragment type!")
                    }
                }
                if (supportFragmentManager.backStackEntryCount == 0) {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.toolbar.navigationIcon = setUpHambug()
                    binding.toolbar.setNavigationOnClickListener {
                        toggle = ActionBarDrawerToggle(
                            this@MainActivity,
                            binding.drawerLayout,
                            binding.toolbar,
                            R.string.open_drawer,
                            R.string.close_drawer
                        )
                        binding.drawerLayout.addDrawerListener(toggle)
                        binding.drawerLayout.openDrawer(GravityCompat.START)
                    }
                } else {
                    val bitmap = BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_arrow_back_24
                    )
                    val width = resources.getDimension(R.dimen.image_width).toInt()
                    val height = resources.getDimension(R.dimen.image_height).toInt()
                    val newdrawable: Drawable =
                        BitmapDrawable(
                            resources,
                            Bitmap.createScaledBitmap(bitmap, width, height, true)
                        )
                    supportActionBar!!.setHomeAsUpIndicator(newdrawable)
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.toolbar.navigationIcon = newdrawable
                    binding.toolbar.setNavigationOnClickListener {
                        hidekeyboard()
                        onBackPressed()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nManager.cancelAll()
    }

    private fun hidekeyboard() {
        val imm =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this@MainActivity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.app_bar_search -> {
                val fragment = supportFragmentManager.findFragmentByTag("search")
                if (fragment == null) {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right)
                        .replace(R.id.fragment_holder, SearchFragment.newInstance(), "search")
                        .addToBackStack(null)
                        .commit()
                }
                val bundle = Bundle()
                bundle.putString("Img", "home")
                firebaseAnalytics.logEvent("search", bundle)


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpDrawerMenu() {
        //expandlistview
        prepareMenuData()
        val adapter = NavExpandAdapter(this, headerList, childList)
        binding.expandableListView.setAdapter(adapter)
        binding.expandableListView.setOnGroupClickListener { _, _, i, _ ->
            if (headerList[i] == headerList[0]) {
                switchToTrangChu()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                binding.mainBottomNav.selectedItemId = R.id.menuTrangChu
            }
            if (headerList[i] == headerList[2]) {
                switchToVideo()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                binding.mainBottomNav.selectedItemId = R.id.menuVideo
            }
            false
        }
        binding.expandableListView.setOnChildClickListener { _, _, i, _, _ ->
            if (headerList[i] == headerList[1]) {
                switchToTrending()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                binding.mainBottomNav.selectedItemId = R.id.menuTrending
            }
            if (headerList[i] == headerList[3]) {
                switchToAudio()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                binding.mainBottomNav.selectedItemId = R.id.menuAudio
            }
            false
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    private fun setupNavDrawer() {
        val header = binding.mainNavDrawer.getHeaderView(0)
        val navClose = header.findViewById<ImageView>(R.id.navClose)
        navClose.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        binding.toolbar.navigationIcon = setUpHambug()
        binding.drawerLayout.addDrawerListener(toggle)


        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setUpHambug(): Drawable {
        val drawerMenu = ContextCompat.getDrawable(this, R.drawable.ic_menu)
        return drawerMenu!!
    }

    private fun prepareMenuData() {
        var menuModel = NavDrawerModel(
            R.drawable.ic_home_menu,
            "Trang chủ",
            false
        ) //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel)
        //end home menu

        if (!menuModel.isParent!!) {
            childList[menuModel] == null
        }

        menuModel = NavDrawerModel(R.drawable.ic_tintuc_menu, "Tin tức", true) //Menu of News
        headerList.add(menuModel)

        var childNewsList: MutableList<NavDrawerModel>? = ArrayList()
        var childNews1: NavDrawerModel? = NavDrawerModel(
            0,
            "Tin mới",
            false
        )
        childNewsList!!.add(childNews1!!)
        if (menuModel.isParent == true) {
            childList[menuModel] = childNewsList
        }


        menuModel = NavDrawerModel(R.drawable.ic_video_menu, "Video", false) //Menu of Video
        headerList.add(menuModel)
        menuModel = NavDrawerModel(R.drawable.ic_audio_menu, "Audio", true) //Menu of Audio
        headerList.add(menuModel)

        var childAudioList: MutableList<NavDrawerModel>? = ArrayList()
        val childAudio1 = NavDrawerModel(
            0,
            "Podcast",
            false
        )
        childAudioList!!.add(childAudio1)
        val chilAudio2 = NavDrawerModel(
            0,
            "Sách nói",
            false
        )
        childAudioList.add(chilAudio2)
        val chilAudio3 = NavDrawerModel(
            0,
            "Âm nhạc",
            false
        )
        childAudioList.add(chilAudio3)
        if (menuModel.isParent == true) {
            childList[menuModel] = childAudioList
        }
    }

    private fun switchToTrangChu() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, TrangChuFragment.newInstance())
            .commit()
        curFrag = "home"
    }


    private fun switchToTrending() {
        if (curFrag == "trending") return
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, ArticlesFragment.newInstance(-1), "trending")
            .commit()
        curFrag = "trending"

    }

    private fun switchToAudio() {
        if (curFrag == "audio") return
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, AudioWithoutTab.newInstance())
            .commit()
        curFrag = "audio"

    }

    private fun switchToVideo() {

        if (curFrag == "video") return
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, FragmentVideoPage.newInstance())
            .commit()
        curFrag = "video"

    }

    private fun switchToShare() {
        if (curFrag == "share") return
        supportFragmentManager.beginTransaction()
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.mainBottomNav.setOnNavigationItemSelectedListener {
//            val fragment = supportFragmentManager.findFragmentByTag("fragVideo")
//            if (fragment != null) {
//                try {
//                    val motionLaout =
//                        findViewById<PlayerScreenMotionLayout>(R.id.root_layout)
//                    motionLaout.setTransition(R.id.end, R.id.end)
//                } catch (e: NullPointerException) {
//                }
//            }
            if (supportFragmentManager.backStackEntryCount > 0) {
                for (i in 0 until supportFragmentManager.backStackEntryCount) {
                    supportFragmentManager.popBackStackImmediate()
                }
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                binding.toolbar.navigationIcon = setUpHambug()
                binding.toolbar.setNavigationOnClickListener {
                    toggle = ActionBarDrawerToggle(
                        this,
                        binding.drawerLayout,
                        binding.toolbar,
                        R.string.open_drawer,
                        R.string.close_drawer
                    )
                    binding.drawerLayout.addDrawerListener(toggle)
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
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
            true
        }
    }


}




