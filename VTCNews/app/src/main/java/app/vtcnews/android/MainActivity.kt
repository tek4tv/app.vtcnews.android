package app.vtcnews.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import app.vtcnews.android.network.Resource
import app.vtcnews.android.repos.MenuRepo
import app.vtcnews.android.ui.trang_chu.TrangChuFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    @Inject
    lateinit var menuRepo : MenuRepo

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = ""

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragent_holder, TrangChuFragment.newInstance())
            .commit()
    }
}