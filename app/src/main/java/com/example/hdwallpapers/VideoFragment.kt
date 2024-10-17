package com.example.hdwallpapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import com.example.hdwallpapers.models.NetworkUtils
import com.tashila.pleasewait.PleaseWaitDialog
import androidx.viewpager2.widget.ViewPager2
import com.example.hdwallpapers.adaptor.MediaItem
import com.example.hdwallpapers.adaptor.MediaPagerAdapter
import com.example.hdwallpapers.databinding.FragmentVideosBinding
import com.example.hdwallpapers.models.VideoViewModel

class VideoFragment : Fragment() {

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MediaPagerAdapter
    private val viewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val progressDialog = PleaseWaitDialog(context = requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Videos thumnails are Loading...")

        binding.viewPagerVideos.orientation = ViewPager2.ORIENTATION_VERTICAL

        if (NetworkUtils.isInternetAvailable(requireContext())) {
            progressDialog.show()

            viewModel.fetchVideos("46503658-ee94f9496a14078767498ae87")

            viewModel.videoList.observe(viewLifecycleOwner, Observer { videos ->
                val groupedVideos = videos.chunked(6).map { videoGroup ->
                    videoGroup.map { videoResult ->
                        MediaItem.VideoItem(videoResult) // Wrap each VideoResult in MediaItem.VideoItem
                    }
                }
                adapter = MediaPagerAdapter(groupedVideos)
                binding.viewPagerVideos.adapter = adapter
                progressDialog.dismiss()
            })
        } else {
            progressDialog.dismiss()
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
