package app.vtcnews.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import app.vtcnews.android.viewmodels.TrangChuFragViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private val viewModel: TrangChuFragViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        viewModel.getMenuList()
        viewModel.getData()
        finish()
    }
}