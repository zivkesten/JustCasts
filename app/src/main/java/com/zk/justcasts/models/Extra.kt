package com.zk.justcasts.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zk.justcasts.repository.database.podcast.ExtraEntity

data class Extra(
    @SerializedName("url1")
    val url1: String?,
    @SerializedName("url2")
    val url2: String?,
    @SerializedName("url3")
    val url3: String?,
    @SerializedName("google_url")
    val google_url: String?,
    @SerializedName("spotify_url")
    val spotify_url: String?,
    @SerializedName("youtube_url")
    val youtube_url: String?,
    @SerializedName("linkdin_url")
    val linkdin_url: String?,
    @SerializedName("wechat_handle")
    val wechat_handle: String?,
    @SerializedName("patreon_handle")
    val patreon_handle: String?,
    @SerializedName("twitter_handle")
    val twitter_handle: String?,
    @SerializedName("facebook_handle")
    val facebook_handle: String?,
    @SerializedName("instagram_handle")
    val instagram_handle: String?
): BaseDTO {

    override fun entity(): BaseEntity {
        return ExtraEntity(1, url1, url2, url3, google_url,
            spotify_url, youtube_url, linkdin_url, wechat_handle,
            patreon_handle, twitter_handle,
            facebook_handle, instagram_handle)
    }



}
