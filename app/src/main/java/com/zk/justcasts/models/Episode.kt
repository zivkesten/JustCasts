package com.zk.justcasts.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode (
    @SerializedName("id")
    val id: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("audio")
    val audio: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("pub_date_ms")
    val pub_date_ms: String?,
    @SerializedName("listennotes_url")
    val listenNotesUrl: String?,
    @SerializedName("audio_length_sec")
    val audio_length_sec: String?,
    @SerializedName("explicit_content")
    val explicit_content: String?,
    @SerializedName("maybe_audio_invalid")
    val maybe_audio_invalid: String?,
    @SerializedName("listennotes_edit_url")
    val listenNotesEditUrl: String?
): Parcelable