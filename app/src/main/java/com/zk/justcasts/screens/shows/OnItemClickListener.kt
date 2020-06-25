package com.zk.justcasts.screens.shows

import android.view.View
import com.zk.justcasts.models.PodcastDTO


interface OnItemClickListener {
    fun onItemClick(item: PodcastDTO, sharedElement: View)
}