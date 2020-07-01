package com.zk.justcasts.repository.database.podcast

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.zk.justcasts.models.BaseEntity
import com.zk.justcasts.models.Extra
import com.zk.justcasts.models.PodcastDTO
import com.zk.justcasts.repository.database.Converters
import java.util.*

@Entity
data class PodcastEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "rss") val rss: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "email") val email: String?,
    //@ColumnInfo(name = "extra") val extra: Extra?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "website") val website: String?,
    @ColumnInfo(name = "language") val language: String?,
    //@ColumnInfo(name = "genre_ids") val genre_ids: List<Int>?,
    @ColumnInfo(name = "itunes_id") val itunes_id: String?,
    @ColumnInfo(name = "publisher") val publisher: String?,
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    @ColumnInfo(name = "is_claimed") val is_claimed: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "total_episodes") val total_episodes: Int?,
    @ColumnInfo(name = "explicit_content") val explicit_content: Boolean?,
    @ColumnInfo(name = "latest_pub_date_ms") val latest_pub_date_ms: Long?
): BaseEntity {
    fun dto(): PodcastDTO {
        return PodcastDTO(id?: "", rss, type, email, /*extra,*/ image, title, country,
            website, language, /*genre_ids,*/ emptyList(), itunes_id,
            publisher, thumbnail, is_claimed, description, total_episodes, explicit_content, latest_pub_date_ms)
    }
}
