package com.zk.justcasts.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zk.justcasts.repository.database.podcast.PodcastEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PodcastDTO (
    @SerializedName("id")
    val id: String,
    @SerializedName("rss")
    val rss: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("email")
    val email: String? = null,
//    @SerializedName("extra")
//    val extra: Extra?,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("website")
    val website: String? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("genre_ids")
    val genre_ids: List<Int>? = emptyList(),
    @SerializedName("episodes")
    val episodes: List<EpisodeDTO>? = emptyList(),
    @SerializedName("itunes_id")
    val itunes_id: String? = null,
    @SerializedName("publisher")
    val publisher: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("is_claimed")
    val is_claimed: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("total_episodes")
    val total_episodes: Int? = null,
    @SerializedName("explicit_content")
    val explicit_content: Boolean? = null,
    @SerializedName("latest_pub_date_ms")
    val latest_pub_date_ms: Long? = null,
    val errorMessage: String? = null): Parcelable, BaseDTO {

    override fun entity(): PodcastEntity {
        return PodcastEntity(id, rss, type, email, /*extra,*/ image, title, country,
            website, language, /*genre_ids,*/ itunes_id,
            publisher, thumbnail, is_claimed, description, total_episodes, explicit_content, latest_pub_date_ms)
    }
}