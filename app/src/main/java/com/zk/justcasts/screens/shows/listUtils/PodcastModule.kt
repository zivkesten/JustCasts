package com.zk.justcasts.screens.shows.listUtils

import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.google.android.material.card.MaterialCardView
import com.zk.justcasts.R
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.extensions.setDebounceClickListener
//
//class PodcastModule : ItemModule<PodcastDTO>() {
//    override fun provideModuleConfig() = object : ItemModuleConfig() {
//        override fun withLayoutResource() = R.layout.list_item_show
//    }
//
//    override fun onBind(item: Item<PodcastDTO>, viewBinder: ViewBinder) {
//        val title = viewBinder.findViewById<TextView>(R.id.show_name)
//        val cover = viewBinder.findViewById<ImageView>(R.id.show_cover)
//        val card = viewBinder.findViewById<MaterialCardView>(R.id.show_card)
//        title.text = item.model.title
//        cover.transitionName = "card${item.metadata.position}"
//        card.setDebounceClickListener {  }
//        cover.load(item.model.image) {
//            crossfade(true)
//            placeholder(android.R.drawable.progress_indeterminate_horizontal)
//            //transformations(CircleCropTransformation())
//        }
//    }
//
//    override fun onUnbind(item: Item<PodcastDTO>, viewBinder: ViewBinder) {
//        // unbind logic like stop animation, release webview resources, etc.
//    }
//}