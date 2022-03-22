package com.example.learningproject.paging.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.savedstate.SavedStateRegistryOwner
import com.example.learningproject.paging.data.GithubRepository

class ViewModelFactory(owner: SavedStateRegistryOwner,
                       private val repository: GithubRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    @ExperimentalPagingApi
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRepositoriesViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}