package com.zk.justcasts.models

import com.google.gson.annotations.SerializedName

data class BestPodcastsResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("has_next")
    val has_next: Boolean? = null,
    @SerializedName("podcasts")
    val podcasts: List<PodcastDTO>? = null,
    @SerializedName("parent_id")
    val parent_id: Int? = null,
    @SerializedName("page_number")
    val page_number: Int? = null,
    @SerializedName("next_page_number")
    val next_page_number: Int? = null,
    @SerializedName("previous_page_number")
    val previous_page_number: Int? = null,
    @SerializedName("Error") val errorMessage: String? = null)