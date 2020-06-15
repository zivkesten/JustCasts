package com.zk.justcasts.screens.listenNow.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import com.zk.justcasts.databinding.FragmentListenNowBinding

class ListenNowFragment: Fragment() {

    companion object {
        @JvmStatic
        private val TAG = ListenNowFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialFadeThrough()
        sharedElementReturnTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListenNowBinding.inflate(inflater, container, false)
        //binding.listenContainer.transitionName = arguments?.getString(ARG_PARAM2)
        return binding.root
    }
}