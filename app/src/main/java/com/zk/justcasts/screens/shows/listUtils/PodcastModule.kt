package com.zk.justcasts.screens.shows.listUtils

import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.zk.justcasts.R
import com.zk.justcasts.models.PodcastDTO
import com.idanatz.oneadapter.external.interfaces.Item

class PodcastModule : ItemModule<PodcastDTO>() {
    override fun provideModuleConfig() = object : ItemModuleConfig() {
        override fun withLayoutResource() = R.layout.list_item_show
    }

    override fun onBind(item: Item<PodcastDTO>, viewBinder: ViewBinder) {
        val title = viewBinder.findViewById<TextView>(R.id.show_name)
        title.text = item.model.title
        val cover = viewBinder.findViewById<ImageView>(R.id.show_cover)
        cover.transitionName = "card${item.metadata.position}"
        cover.load(item.model.image) {
            crossfade(true)
            placeholder(android.R.drawable.progress_indeterminate_horizontal)
            //transformations(CircleCropTransformation())
        }
    }

    override fun onUnbind(item: Item<PodcastDTO>, viewBinder: ViewBinder) {
        // unbind logic like stop animation, release webview resources, etc.
    }
}