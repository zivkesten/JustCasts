package com.zk.justcasts.screens.show.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zk.justcasts.R
import com.zk.justcasts.databinding.ShowFragmentBinding
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.presentation.extensions.observe
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import com.zk.justcasts.screens.show.viewModel.ShowViewModel
import com.zk.justcasts.screens.show.listUtils.EpisodesRecyclerViewAdapter
import com.zk.justcasts.screens.show.listUtils.OnEpisodeClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowFragment : Fragment(), OnEpisodeClickListener {

    private val viewModel by viewModel<ShowViewModel>()

    private val episodeAdapter =
        EpisodesRecyclerViewAdapter(
            listener = this
        )

    private val args: ShowFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = buildContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
        observe(viewModel.viewState, Observer { render(it) })
        observe(viewModel.viewEffects, Observer { trigger(it) })
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            interpolator = FastOutSlowInInterpolator()
            containerColor = MaterialColors.getColor(requireActivity().findViewById(android.R.id.content), R.attr.colorSurface)
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            duration = 300
        }

    private lateinit var binding: ShowFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShowFragmentBinding.inflate(inflater, container, false)
        setupBinding()
        viewModel.onEvent(Event.ScreenLoad(args.data))
        return binding.root
    }

    private fun setupBinding() {
        binding.coordinator.transitionName = args.transitionName
        Picasso.get()
            .load(args.data.image)
            .into(binding.showCover, object: Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError(e: java.lang.Exception?) {
                    startPostponedEnterTransition()
                }
            })
        binding.collapsingToolbar.title = args.data.title
        binding.episodeList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = episodeAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            viewModel.onEvent(event = Event.AddToMyShows(args.data))
        }
    }

    private fun render(state: ViewState) {
        state.episodes?.let { episodeAdapter.update(it) }
        Log.d("Zivi", "----- viewState $state")
    }

    private fun trigger(effect: ViewEffect) {
        when(effect) {
            is ViewEffect.NoEffect -> Log.d("Zivi", "no effect")
            is ViewEffect.TransitionToScreenWithElement ->  view?.findNavController()?.navigate(effect.direction, effect.extras)
        }
    }

    override fun onItemClick(item: EpisodeDTO, sharedElement: View) {
        viewModel.onEvent(Event.ListItemClicked(item, sharedElement))
    }
}