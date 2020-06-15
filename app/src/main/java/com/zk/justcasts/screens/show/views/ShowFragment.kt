package com.zk.justcasts.screens.show.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.navArgs
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.zk.justcasts.R
import com.zk.justcasts.databinding.ShowFragmentBinding
import com.zk.justcasts.screens.show.viewModel.ShowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowFragment : Fragment() {

    private val viewModel by viewModel<ShowViewModel>()

    private val args: ShowFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = buildContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}