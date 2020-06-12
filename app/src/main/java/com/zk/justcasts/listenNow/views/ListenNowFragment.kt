package com.zk.justcasts.listenNow.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.zk.justcasts.R
import com.zk.justcasts.databinding.FragmentListenNowBinding
import com.zk.justcasts.models.Podcast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListenNowFragment: Fragment() {

    companion object {
        @JvmStatic
        private val TAG = ListenNowFragment::class.java.simpleName
        fun newInstance(data: Podcast, transitionName: String?) =
            ListenNowFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, data)
                    putString(ARG_PARAM2, transitionName)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sharedElementEnterTransition = MaterialContainerTransform(context)
        sharedElementEnterTransition = buildContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform().apply {
            drawingViewId = R.id.main_container
            interpolator = FastOutSlowInInterpolator()
            containerColor = MaterialColors.getColor(requireActivity().findViewById(android.R.id.content), R.attr.colorSurface)
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            duration = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListenNowBinding.inflate(inflater, container, false)
        binding.listenContainer.transitionName = arguments?.getString(ARG_PARAM2)
        sharedElementReturnTransition = MaterialContainerTransform()
        return binding.root
    }
}