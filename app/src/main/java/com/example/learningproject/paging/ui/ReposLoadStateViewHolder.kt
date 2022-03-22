package com.example.learningproject.paging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.learningproject.R
import com.example.learningproject.databinding.ReposLoadStateFooterViewItemBinding

class ReposLoadStateViewHolder(private val binding:ReposLoadStateFooterViewItemBinding,
  retry:()->Unit) :RecyclerView.ViewHolder(binding.root){
      init {
          binding.retryButton.setOnClickListener {
              retry.invoke()
          }

      }
    fun bind(loadstate:LoadState)
    {
        if(loadstate is LoadState.Error)
        {
            binding.errorMsg.text=loadstate.error.localizedMessage

        }
        binding.progressBar.isVisible=loadstate is LoadState.Loading
        binding.retryButton.isVisible=loadstate is LoadState.Error
        binding.errorMsg.isVisible=loadstate is LoadState.Error
    }
    companion object
    {
        fun create(parent:ViewGroup,retry: () -> Unit):ReposLoadStateViewHolder
        {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.repos_load_state_footer_view_item,parent,false)
            val binding=ReposLoadStateFooterViewItemBinding.bind(view)
            return ReposLoadStateViewHolder(binding,retry)
        }
    }
}