package com.zk.justcasts.di

import com.zk.justcasts.main.viewModel.MainActivityViewModel
import com.zk.justcasts.listenNow.viewModel.ListenNowViewModel
import com.zk.justcasts.shows.viewModel.MyShowsViewModel
import com.zk.justcasts.search.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelsModule {
	companion object{
		val modules = module {
			viewModel { MainActivityViewModel() }
			viewModel { MyShowsViewModel(get()) }
			viewModel { SearchViewModel() }
			viewModel { ListenNowViewModel() }
		}
	}
}
