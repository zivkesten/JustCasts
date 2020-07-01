package com.zk.justcasts.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("took")
    val took: Float? = null,
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("results")
    val results: List<PodcastDTO>? = emptyList(),
    @SerializedName("next_offset")
    val next_offset: Int? = null,
    @SerializedName("Error") val errorMessage: String? = null)
