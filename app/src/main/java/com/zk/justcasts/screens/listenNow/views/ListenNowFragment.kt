package com.zk.justcasts.screens.listenNow.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zk.justcasts.R
import com.zk.justcasts.databinding.FragmentListenNowBinding
import com.zk.justcasts.databinding.ShowFragmentBinding
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.presentation.extensions.observe
import com.zk.justcasts.screens.listenNow.viewModel.ListenNowViewModel
import com.zk.justcasts.screens.show.model.Event
import com.zk.justcasts.screens.show.model.ViewEffect
import com.zk.justcasts.screens.show.model.ViewState
import com.zk.justcasts.screens.show.viewModel.ShowViewModel
import com.zk.justcasts.screens.show.views.ShowFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListenNowFragment: Fragment() {

    private val viewModel by viewModel<ListenNowViewModel>()

    private val args: ShowFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = buildContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
        //observe(viewModel.viewState, Observer { render(it) /})
        //observe(viewModel.viewEffects, Observer { trigger(it) })
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            interpolator = FastOutSlowInInterpolator()
            containerColor = MaterialColors.getColor(requireActivity().findViewById(android.R.id.content), R.attr.colorSurface)
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            duration = 300
        }

    private lateinit var binding: FragmentListenNowBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListenNowBinding.inflate(inflater, container, false)
        setupBinding()
        //viewModel.onEvent(Event.ScreenLoad(args.data))
        return binding.root
    }

    private fun setupBinding() {
        binding.listenNowCoordinator.transitionName = args.transitionName
        Picasso.get()
            .load(args.data.image)
            .into(binding.episodeImage, object: Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError(e: java.lang.Exception?) {
                    startPostponedEnterTransition()
                }
            })
//        binding.collapsingToolbar.title = args.data.title
//        binding.episodeList.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = episodeAdapter
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.playFab.setOnClickListener {
            //viewModel.onEvent(event = Event.AddToMyShows(args.data))
        }
    }

    private fun render(state: ViewState) {
        Log.d("Zivi", "----- viewState $state")
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
}