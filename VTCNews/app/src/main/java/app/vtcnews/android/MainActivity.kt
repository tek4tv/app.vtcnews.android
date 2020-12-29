package app.vtcnews.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import app.vtcnews.android.databinding.ActivityMainBinding
import app.vtcnews.android.ui.audio.AudioHomeFragment
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        binding.mainBottomNav.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.menuAudio) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_holder, AudioHomeFragment.newInstance()).addToBackStack(null).commit()
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

}