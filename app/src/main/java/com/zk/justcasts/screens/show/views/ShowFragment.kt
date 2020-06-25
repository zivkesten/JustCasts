package com.zk.justcasts.screens.show.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zk.justcasts.R
import com.zk.justcasts.databinding.ShowFragmentBinding
import com.zk.justcasts.models.Episode
import com.zk.justcasts.presentation.extensions.observe
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import com.zk.justcasts.screens.show.viewModel.ShowViewModel
import com.zk.justcasts.screens.shows.listUtils.EpisodesRecyclerViewAdapter
import com.zk.justcasts.screens.shows.listUtils.OnEpisodeClickListener
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
        return binding.root
    }

    private fun setupBinding() {
        binding.episodeList.apply {
            layoutManager = GridLayoutManager(context, 2)
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

    }

    private fun trigger(effect: ViewEffect) {
        when(effect) {
            is ViewEffect.ShowAddToFavConfirmation ->  {
                Log.d("Zivi", "Added to favourites ${effect.podcastAdded.title}")
                val rootView: View = requireActivity().window.decorView.findViewById(android.R.id.content)

                Snackbar.make(rootView, effect.podcastAdded.title + "Was added", Snackbar.LENGTH_LONG).show()
            }
            is ViewEffect.NoEffect -> Log.d("Zivi", "no effect")
            is ViewEffect.TransitionToScreenWithElement -> TODO()
        }
    }

    override fun onItemClick(item: Episode, sharedElement: View) {
        Log.d("Zivi", "clicked episode ${item.title}")
    }
}