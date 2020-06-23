package com.zk.justcasts.models

import com.google.gson.annotations.SerializedName

data class BestPodcastsResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("has_next")
    val has_next: Boolean?,
    @SerializedName("podcasts")
    val podcasts: List<PodcastDTO>?,
    @SerializedName("parent_id")
    val parent_id: Int?,
    @SerializedName("page_number")
    val page_number: Int?,
    @SerializedName("next_page_number")
    val next_page_number: Int?,
    @SerializedName("previous_page_number")
    val previous_page_number: Int?)