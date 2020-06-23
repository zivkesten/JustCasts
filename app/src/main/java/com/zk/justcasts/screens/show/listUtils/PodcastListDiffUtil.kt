package com.zk.justcasts.screens.shows.listUtils

import androidx.recyclerview.widget.DiffUtil
import com.zk.justcasts.models.Podcast

class PodcastListDiffUtil(private var newItems: List<Podcast>, private var oldItems: List<Podcast>) :
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
    
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}