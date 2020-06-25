package com.zk.justcasts.screens.shows.listUtils

import androidx.recyclerview.widget.DiffUtil
import com.zk.justcasts.models.Episode

class EpisodeListDiffUtil(private var newItems: List<Episode>, private var oldItems: List<Episode>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldItems[oldItemPosition] === newItems[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}