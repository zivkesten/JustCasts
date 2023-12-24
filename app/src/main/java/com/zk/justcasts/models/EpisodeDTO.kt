package com.zk.justcasts.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeDTO (
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("audio")
    val audio: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("pub_date_ms")
    val pub_date_ms: String? = null,
    @SerializedName("listennotes_url")
    val listenNotesUrl: String? = null,
    @SerializedName("audio_length_sec")
    val audio_length_sec: String? = null,
    @SerializedName("explicit_content")
    val explicit_content: String? = null,
    @SerializedName("maybe_audio_invalid")
    val maybe_audio_invalid: String? = null,
    @SerializedName("listennotes_edit_url")
    val listenNotesEditUrl: String? = null,
    @SerializedName("Error") val errorMessage: String? = null
) : Parcelable