package com.zk.justcasts.screens.shows.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.zk.justcasts.databinding.FragmentMyShowsBinding
import com.zk.justcasts.models.Podcast
import com.zk.justcasts.screens.shows.OnItemClickListener
import com.zk.justcasts.screens.shows.PodcastsRecyclerViewAdapter
import com.zk.justcasts.screens.shows.model.Event
import com.zk.justcasts.screens.shows.model.ViewState
import com.zk.justcasts.screens.shows.viewModel.MyShowsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyShowsFragment: Fragment(), OnItemClickListener {

    private val viewModel by viewModel<MyShowsViewModel>()

    private lateinit var binding: FragmentMyShowsBinding

    private val showsAdapter = PodcastsRecyclerViewAdapter(listener = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        if (savedInstanceState == null) {
            viewModel.event(Event.ScreenLoad)
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
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.obtainState.observe(viewLifecycleOwner, Observer {
            Log.d("Zivi", "podcasts in fragment: " + it)
            render(it)
        })
    }

    private fun render(state: ViewState) {
        showsAdapter.update(state.itemList)
        //swiperefresh.isRefreshing = false
    }

    private fun setupBinding() {
        //binding.swiperefresh.setOnRefreshListener(this)
        binding.showsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = showsAdapter
        }
    }

    override fun onItemClick(item: Podcast, sharedElement: View) {

        ViewCompat.getTransitionName(sharedElement)?.let {

            val extras = FragmentNavigatorExtras(sharedElement to it)
            val action = MyShowsFragmentDirections.actionShowsFragmentToShowFragment2(item, it)
            view?.findNavController()?.navigate(action, extras)
        } ?: run {
            Log.w("Zivi", "No transition name")
        }
    }
}
