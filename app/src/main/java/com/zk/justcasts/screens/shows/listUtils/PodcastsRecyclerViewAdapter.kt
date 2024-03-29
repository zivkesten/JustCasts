package com.zk.justcasts.screens.shows.listUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zk.justcasts.databinding.ListItemShowBinding
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.presentation.extensions.setDebounceClickListener
import com.zk.justcasts.screens.shows.OnItemClickListener

class PodcastsRecyclerViewAdapter(private var values: List<PodcastDTO> = ArrayList()) : RecyclerView.Adapter<PodcastsRecyclerViewAdapter.ViewHolder>() {

    fun update(items: List<PodcastDTO>) {
       //if (values.isEmpty()) {
            values = items
            notifyDataSetChanged()
        //    return
       // }
      //  val diffResult = DiffUtil.calculateDiff(
      //      PodcastListDiffUtil(
       //         values,
      //          items
      //      )
      //  )
      //  diffResult.dispatchUpdatesTo(this)
    }

    private var onDebounceCLickListener: ((item: PodcastDTO, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemShowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(values[position])

    override fun getItemCount(): Int {
        return values.size
    }

    fun setDebounceClickListener(onDebounceCLickListener: ((item: PodcastDTO, view: View) -> Unit)) {
        this.onDebounceCLickListener = onDebounceCLickListener
    }

    inner class ViewHolder(private val binding: ListItemShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PodcastDTO) {
            with(binding) {
                showName.text = item.title
                showCard.transitionName = "card$adapterPosition"
                Picasso.get()
                    .load(item.image)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .into(showCover)
                binding.showCard.setDebounceClickListener {
                    onDebounceCLickListener?.invoke(item, binding.showCard)
                }
            }
        }
    }
}

