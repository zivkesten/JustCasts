package com.zk.justcasts.shows

import android.view.View
import com.zk.justcasts.models.Podcast


interface OnItemClickListener {
    fun onItemClick(item: Podcast, sharedEllement: View)
}