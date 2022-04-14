package com.kotlin.audioextractor.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kotlin.audioextractor.adapters.CustomAdapterFiles
import com.kotlin.audioextractor.adapters.CustomAdapterFolders
import com.kotlin.audioextractor.comparators.FileComparatorSortDate
import com.kotlin.audioextractor.comparators.FileComparatorSortName
import com.kotlin.audioextractor.comparators.FileComparatorSortSize
import com.kotlin.audioextractor.databinding.FragmentHomeBinding
import com.kotlin.audioextractor.models.FileItem
import com.kotlin.audioextractor.models.Folder
import com.kotlin.audioextractor.viewModels.HomeViewModel
import java.util.*

class Home : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: CustomAdapterFiles
    private lateinit var adapterFolder: CustomAdapterFolders
    private lateinit var fileList: ArrayList<FileItem>
    private lateinit var folderList: ArrayList<Folder>

    private fun initializations() {
        fileList = ArrayList()
        folderList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializations()
        //initRecyclerView()
        initFolderRecyclerView()
        Handler(Looper.getMainLooper()).postDelayed({
            initViewModel()
        }, 50)

        binding!!.apply {
            //btnSortDateHome.setOnClickListener { onSortClick(0) }
            //btnSortNameHome.setOnClickListener { onSortClick(1) }
            //btnSortSizeHome.setOnClickListener { onSortClick(2) }
        }
    }

    private fun initRecyclerView() {
        adapter = CustomAdapterFiles()
        binding!!.rvFilesMain.adapter = adapter
    }

    private fun initFolderRecyclerView() {
        adapterFolder = CustomAdapterFolders()
        binding!!.rvFilesMain.adapter = adapterFolder
    }

    private fun initViewModel() {
        viewModel.fetchAudioFiles()
        /*viewModel.fileListMutableLiveData.observe(viewLifecycleOwner) {
            binding!!.progressBarHome.visibility = View.GONE
            adapter.submitList(it)
        }*/
        viewModel.folderListMutableLiveData.observe(viewLifecycleOwner) {
            binding!!.progressBarHome.visibility = View.GONE
            adapterFolder.submitList(it)
        }
    }

    private fun onSortClick(sortType: Int) {
        when (sortType) {
            0 -> {
                val updatedList: List<FileItem> = ArrayList(adapter.currentList)
                Collections.sort(updatedList, FileComparatorSortDate())
                adapter.submitList(updatedList.asReversed())
            }
            1 -> {
                val updatedList: List<FileItem> = ArrayList(adapter.currentList)
                Collections.sort(updatedList, FileComparatorSortName())
                adapter.submitList(updatedList)
            }
            2 -> {
                val updatedList: List<FileItem> = ArrayList(adapter.currentList)
                Collections.sort(updatedList, FileComparatorSortSize())
                adapter.submitList(updatedList.asReversed())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}