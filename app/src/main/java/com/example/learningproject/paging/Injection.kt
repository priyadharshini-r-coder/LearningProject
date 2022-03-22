package com.example.learningproject.paging

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.learningproject.paging.api.GithubService
import com.example.learningproject.paging.data.GithubRepository
import com.example.learningproject.paging.db.RepoDatabase
import com.example.learningproject.paging.ui.ViewModelFactory

object Injection {
    private fun provideGithubRepository(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(), RepoDatabase.getInstance(context))
    }


    fun provideViewModelFactory(context: Context, owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideGithubRepository(context))
    }
}