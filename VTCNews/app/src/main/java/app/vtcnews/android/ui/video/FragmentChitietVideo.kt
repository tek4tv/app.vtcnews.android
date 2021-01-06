package app.vtcnews.android.ui.video

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import app.vtcnews.android.MainActivity
import app.vtcnews.android.R
import app.vtcnews.android.databinding.VideoChitietLayoutBinding
import app.vtcnews.android.model.Article
import app.vtcnews.android.viewmodels.VideoHomeFragViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentChitietVideo : Fragment() {
    lateinit var binding: VideoChitietLayoutBinding
    lateinit var player:SimpleExoPlayer
    val viewModel: VideoHomeFragViewModel by viewModels()

    companion object {
        fun newInstance(title: String, idVideoDetail: Long,categoryId : Long) = FragmentChitietVideo().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putLong("idvideodetail", idVideoDetail)
                putLong("categoryid", categoryId)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VideoChitietLayoutBinding.inflate(layoutInflater, container, false)
        val view = layoutInflater.inflate(R.layout.video_chitiet_layout, container, false)

        val activity = activity as? MainActivity
        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
//        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_24)
//        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar?.setNavigationOnClickListener(View.OnClickListener {
//            toolbar?.setNavigationIcon(R.drawable.ic_nav_drawer_24)
//            activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            activity?.supportActionBar?.setDisplayShowHomeEnabled(true)
//            //binding.toolbar.setNavigationIcon(R.drawable.ic_nav_drawer_24)
//            activity?.supportActionBar?.setHomeButtonEnabled(true)
//            requireActivity().onBackPressed()
//        })
        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        activity?.supportFragmentManager!!.addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener {
            if (activity?.supportFragmentManager.getBackStackEntryCount() > 0) {
                toggle.setDrawerIndicatorEnabled(false)
                activity?.supportActionBar!!.setDisplayHomeAsUpEnabled(true) // show back button
                toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        activity.onBackPressed()
                        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                })
            } else {
                //show hamburger
                toggle.setDrawerIndicatorEnabled(true)
                activity?.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                toggle.syncState()
                toolbar!!.setNavigationOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        drawerLayout!!.openDrawer(GravityCompat.START)
                        drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                })
            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVideoDetail(requireArguments().getLong("idvideodetail"))
        Log.i("idaCurrent", ""+requireArguments().getLong("idvideodetail"))
        binding.tvTitleVideo.setText(requireArguments().getString("title"))
        dataListNextVideoObser()
        dataVideoDetailObser()
        viewModel.getVideoByCategory(1,requireArguments().getLong("categoryid"))

    }

    fun dataVideoDetailObser() {
        viewModel.videoDetail.observe(viewLifecycleOwner)
        {   player = SimpleExoPlayer.Builder(requireContext()).build()
            val url = it[0].videoURL
            Log.i("idaUrl", url)
            binding.pvVideo.setPlayer(player)
            val mediaItem: MediaItem =
                MediaItem.fromUri("https://media.vtc.vn/"+url)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }
    fun dataListNextVideoObser()
    {
        viewModel.listVideoByCategory.observe(viewLifecycleOwner)
        {listVideoByCategory ->
            val adapter = VideoItemAdapter(listVideoByCategory.take(8))

            val layoutManager = GridLayoutManager(context, 2)
            binding.rvVideoNext.adapter = adapter
            binding.rvVideoNext.layoutManager = layoutManager
            adapter.clickListen = {
                article:Article ->
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_holder,
                        FragmentChitietVideo.newInstance(article.title!!,article.id as Long,article.categoryID!!)
                    ).commit()
                Log.i("idaDetail", "" + article.id)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onPause() {
        super.onPause()
        player.stop()
    }


}