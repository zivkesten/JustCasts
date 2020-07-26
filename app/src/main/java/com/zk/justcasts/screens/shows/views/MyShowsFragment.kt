package com.zk.justcasts.screens.shows.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.zk.justcasts.databinding.FragmentMyShowsBinding
import com.zk.justcasts.presentation.extensions.observe
import com.zk.justcasts.screens.shows.listUtils.PodcastsRecyclerViewAdapter
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import com.zk.justcasts.screens.shows.viewModel.MyShowsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyShowsFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModel<MyShowsViewModel>()

    private lateinit var binding: FragmentMyShowsBinding

    private val showsAdapter = PodcastsRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        if (savedInstanceState == null) {
            viewModel.onEvent(Event.ScreenLoad)
        }
        observe(viewModel.viewState, Observer { render(it) })
        observe(viewModel.viewEffects, Observer { trigger(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyShowsBinding.inflate(inflater, container, false)
        setupBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun render(viewState: ViewState?) {
        if (viewState == null) return // Ignore null values
        Log.d("Zivi", "----- viewState $viewState")
        showsAdapter.update(viewState.itemList)
    }

    private fun trigger(effect: ViewEffect) {
        when(effect) {
            is ViewEffect.ShowVisualResultForAddToFavourites ->  Snackbar.make(binding.showsCoordinator, effect.message, Snackbar.LENGTH_LONG).show()
            is ViewEffect.TransitionToScreenWithElement ->  view?.findNavController()?.navigate(effect.direction, effect.extras)
        }
    }

    private fun setupBinding() {
        binding.swiperefresh.setOnRefreshListener(this)
        binding.showsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            showsAdapter.setDebounceClickListener { item, sharedElement ->
                viewModel.onEvent(Event.ItemClicked(item, sharedElement))
            }
            adapter = showsAdapter
        }
    }

    override fun onRefresh() {
        viewModel.onEvent(Event.SwipeToRefreshEvent)
    }
}
