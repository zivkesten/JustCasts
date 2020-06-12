package com.zk.justcasts.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Podcast(
    @SerializedName("id")
    val id: String?,
    @SerializedName("rss")
    val rss: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("extra")
    val extra: Extra?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("genre_ids")
    val genre_ids: List<Int>?,
    @SerializedName("itunes_id")
    val itunes_id: String?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("is_claimed")
    val is_claimed: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("total_episodes")
    val total_episodes: Int?,
    @SerializedName("explicit_content")
    val explicit_content: Boolean?,
    @SerializedName("latest_pub_date_ms")
    val latest_pub_date_ms: Long?): Parcelable