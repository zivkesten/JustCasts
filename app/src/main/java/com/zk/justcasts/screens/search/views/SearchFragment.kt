package com.zk.justcasts.screens.search.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.zk.justcasts.databinding.FragmentSearchBinding
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.extensions.observe
import com.zk.justcasts.presentation.extensions.onTextChanged
import com.zk.justcasts.screens.search.model.Event
import com.zk.justcasts.screens.search.model.ViewEffect
import com.zk.justcasts.screens.search.model.ViewState
import com.zk.justcasts.screens.search.viewModel.SearchViewModel
import com.zk.justcasts.screens.shows.OnItemClickListener
import com.zk.justcasts.screens.shows.listUtils.PodcastsRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding

    val viewModel by viewModel<SearchViewModel>()

    private val showsAdapter = PodcastsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        observe(viewModel.viewState, Observer { render(it) })
        observe(viewModel.viewEffects, Observer { trigger(it) })
        if (savedInstanceState == null) {
            viewModel.onEvent(Event.ScreenLoad)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupBinding()

        return binding.root
    }

    private fun handleSearchInput(it: String) {
        if (it.length > 2) {
            viewModel.onEvent(Event.SearchTextInput(it))
        }
    }

    private fun render(state: ViewState) {
        state.searchResultList?.let {
            showsAdapter.update(it)
        }
        Log.d("Zivi", "----- viewState $state")
    }

    private fun trigger(effect: ViewEffect) {
        when(effect) {
            is ViewEffect.TransitionToScreenWithElement ->  view?.findNavController()?.navigate(effect.direction, effect.extras)
            else -> Unit // TODO: Handle
        }
    }

    private fun setupBinding() {
        binding.searchPodcasts.onTextChanged { handleSearchInput(it) }
        binding.searchedPodcasts.apply {
            layoutManager = GridLayoutManager(context, 2)
            showsAdapter.setDebounceClickListener { item, sharedElement ->
                viewModel.onEvent(Event.ItemClicked(item, sharedElement))
            }
            adapter = showsAdapter
        }
    }
}