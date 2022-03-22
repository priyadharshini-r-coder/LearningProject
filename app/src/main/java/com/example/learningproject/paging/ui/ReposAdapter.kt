package com.example.learningproject.paging.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learningproject.R
import com.example.learningproject.paging.api.GithubService.Companion.create

class ReposAdapter :PagingDataAdapter<UiModel,RecyclerView.ViewHolder>(UIMODEL_COMPARATOR){
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return if(viewType== R.layout.repo_view_item)
       {
           RepoViewHolder.create(parent)
       }
        else
       {
           SeparatorViewHolder.create(parent)
       }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position))
        {
            is UiModel.RepoItem->R.layout.repo_view_item
            is UiModel.SeparatorItem->R.layout.separator_view_item
          null->throw UnsupportedOperationException("Unknown view")
        }
    }

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem &&
                        oldItem.repo.fullName == newItem.repo.fullName) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}