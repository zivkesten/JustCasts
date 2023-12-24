package com.zk.justcasts.repository.database.podcast

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zk.justcasts.models.BaseEntity

@Entity
data class ExtraEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "url1") val url1: String?,
    @ColumnInfo(name = "url2") val url2: String?,
    @ColumnInfo(name = "url3") val url3: String?,
    @ColumnInfo(name = "google_url") val google_url: String?,
    @ColumnInfo(name = "spotify_url") val spotify_url: String?,
    @ColumnInfo(name = "youtube_url") val youtube_url: String?,
    @ColumnInfo(name = "linkdin_url") val linkdin_url: String?,
    @ColumnInfo(name = "wechat_handle") val wechat_handle: String?,
    @ColumnInfo(name = "patreon_handle") val patreon_handle: String?,
    @ColumnInfo(name = "twitter_handle") val twitter_handle: String?,
    @ColumnInfo(name = "facebook_handle") val facebook_handle: String?,
    @ColumnInfo(name = "instagram_handle") val instagram_handle: String?): BaseEntity

