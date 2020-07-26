package com.zk.justcasts.repository.database.podcast

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zk.justcasts.models.BaseEntity
import com.zk.justcasts.models.PodcastDTO

@Entity
data class PodcastEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "rss") val rss: String? = null,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    //@ColumnInfo(name = "extra") val extra: Extra? = null,
    @ColumnInfo(name = "image") val image: String? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "website") val website: String? = null,
    @ColumnInfo(name = "language") val language: String? = null,
    //@ColumnInfo(name = "genre_ids") val genre_ids: List<Int>? = null,
    @ColumnInfo(name = "itunes_id") val itunes_id: String? = null,
    @ColumnInfo(name = "publisher") val publisher: String? = null,
    @ColumnInfo(name = "thumbnail") val thumbnail: String? = null,
    @ColumnInfo(name = "is_claimed") val is_claimed: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "total_episodes") val total_episodes: Int? = null,
    @ColumnInfo(name = "explicit_content") val explicit_content: Boolean? = null,
    @ColumnInfo(name = "latest_pub_date_ms") val latest_pub_date_ms: Long? = null
): BaseEntity {
    fun dto(): PodcastDTO {
        return PodcastDTO(id, rss, type, email, /*extra,*/ image, title, country,
            website, language, /*genre_ids,*/
            emptyList(), emptyList(), itunes_id,
            publisher, thumbnail, is_claimed, description, total_episodes, explicit_content, latest_pub_date_ms)
    }
}
