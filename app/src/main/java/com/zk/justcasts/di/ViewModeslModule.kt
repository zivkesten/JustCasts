package com.zk.justcasts.di

import com.zk.justcasts.main.viewModel.MainActivityViewModel
import com.zk.justcasts.screens.listenNow.viewModel.ListenNowViewModel
import com.zk.justcasts.screens.shows.viewModel.MyShowsViewModel
import com.zk.justcasts.screens.search.viewModel.SearchViewModel
import com.zk.justcasts.screens.show.viewModel.ShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelsModule {
	companion object{
		val modules = module {
			viewModel { MainActivityViewModel() }
			viewModel { MyShowsViewModel(get()) }
			viewModel { SearchViewModel() }
			viewModel { ListenNowViewModel() }
			viewModel { ShowViewModel(get()) }
		}
	}
}
