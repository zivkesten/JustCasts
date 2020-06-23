package com.zk.justcasts.screens.shows.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.zk.justcasts.databinding.FragmentMyShowsBinding
import com.zk.justcasts.models.Podcast
import com.zk.justcasts.screens.shows.listUtils.OnPodcastClickListener
import com.zk.justcasts.screens.shows.listUtils.PodcastsRecyclerViewAdapter
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.ViewEffect
import com.zk.justcasts.screens.shows.model.ViewState
import com.zk.justcasts.screens.shows.viewModel.MyShowsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyShowsFragment: Fragment(), OnPodcastClickListener, SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModel<MyShowsViewModel>()

    private lateinit var binding: FragmentMyShowsBinding

    private val showsAdapter =
        PodcastsRecyclerViewAdapter(
            listener = this
        )

    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        observeViewState()
        if (savedInstanceState == null) {
            viewModel.processInput(Event.ScreenLoad)
        }
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

    private fun observeViewState() {
        disposables.addAll(
            viewModel
                .viewState
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { Log.d("Zivi","viewState $it") }
                .subscribe { state -> render(state) },
            viewModel
                .viewEffects
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { Log.d("Zivi","viewEffects $it") }
                .subscribe { effect -> trigger(effect) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun render(state: ViewState) {
        val items  = state.itemList?: return
        showsAdapter.update(items)
        binding.swiperefresh.isRefreshing = false
    }

    private fun trigger(effect: ViewEffect) {
        when(effect) {
            is ViewEffect.ShowVisualResultForAddToFavourites ->  Snackbar.make(binding.showsCoordinator, effect.message, Snackbar.LENGTH_LONG).show()
            is ViewEffect.TransitionToScreenWithElement -> transitionWithNavigationComponents(effect.extras, effect.direction)
        }
    }

    private fun transitionWithNavigationComponents(extras: Navigator.Extras, direction: NavDirections) {
        view?.findNavController()?.navigate(direction, extras)
    }

    private fun setupBinding() {
        binding.swiperefresh.setOnRefreshListener(this)
        binding.showsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = showsAdapter
        }
    }

    override fun onItemClick(item: Podcast, sharedElement: View) {
        viewModel.processInput(Event.ItemClicked(item, sharedElement))
    }

    override fun onRefresh() {
        viewModel.processInput(Event.SwipeToRefreshEvent)
        viewModel.processInput(Event.SubscribeToPodcast)
    }
}
