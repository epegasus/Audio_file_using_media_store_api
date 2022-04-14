package com.kotlin.audioextractor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.audioextractor.R
import com.kotlin.audioextractor.databinding.ListItemFolderBinding
import com.kotlin.audioextractor.models.Folder

class CustomAdapterFolders : ListAdapter<Folder, CustomAdapterFolders.CustomViewHolderFolders>(DiffUtilsFolder) {

    class CustomViewHolderFolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListItemFolderBinding.bind(itemView)
    }

    object DiffUtilsFolder : DiffUtil.ItemCallback<Folder>() {

        override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderFolders {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_folder, parent, false)
        return CustomViewHolderFolders(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolderFolders, position: Int) {
        val item = getItem(position)
        with(holder) {
            binding.tvTitleListItemFolder.text = item.folderName
        }
    }
}