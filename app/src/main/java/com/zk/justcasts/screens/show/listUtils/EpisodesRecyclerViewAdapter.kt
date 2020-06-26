package com.zk.justcasts.screens.show.listUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zk.justcasts.databinding.ListItemEpisodeBinding
import com.zk.justcasts.models.EpisodeDTO
import com.zk.justcasts.screens.shows.listUtils.EpisodeListDiffUtil

class EpisodesRecyclerViewAdapter(private var values: List<EpisodeDTO> = ArrayList(), private val listener: OnEpisodeClickListener) : RecyclerView.Adapter<EpisodesRecyclerViewAdapter.ViewHolder>() {

    fun update(items: List<EpisodeDTO>) {
        if (values.isEmpty()) {
            values = items
            notifyDataSetChanged()
            return
        }
        val diffResult = DiffUtil.calculateDiff(
            EpisodeListDiffUtil(
                values,
                items
            )
        )
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemEpisodeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(values[position])

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(private val binding: ListItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EpisodeDTO) {
            with(binding) {
                episodeTitle.text = item.title
                episodeLayout.transitionName = "card$adapterPosition"
                Picasso.get()
                    .load(item.image)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .into(episodeImage)
                binding.episodeLayout.setOnClickListener {
                    listener.onItemClick(item, episodeLayout)
                }
            }
        }
    }
}

interface OnEpisodeClickListener {
    fun onItemClick(item: EpisodeDTO, sharedElement: View)
}
