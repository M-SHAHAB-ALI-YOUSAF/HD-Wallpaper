package com.example.hdwallpapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.hdwallpapers.adaptor.ImagePagerAdapter
import com.example.hdwallpapers.models.ImageViewModel
import com.example.hdwallpapers.models.NetworkUtils
import com.tashila.pleasewait.PleaseWaitDialog
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.hdwallpapers.databinding.FragmentImageBinding // Ensure to use your correct binding class

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ImagePagerAdapter
    private val viewModel: ImageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val progressDialog = PleaseWaitDialog(context = requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Wallpapers are Loading...")


        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        if (NetworkUtils.isInternetAvailable(requireContext())) {
            progressDialog.show()

            viewModel.fetchImages("46503658-ee94f9496a14078767498ae87")

            viewModel.images.observe(viewLifecycleOwner, Observer { images ->
                val groupedImages = images.chunked(6)
                adapter = ImagePagerAdapter(groupedImages)
                binding.viewPager.adapter = adapter
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
