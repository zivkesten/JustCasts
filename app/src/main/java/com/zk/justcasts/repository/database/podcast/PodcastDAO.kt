package com.zk.justcasts.repository.database.podcast

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

@Dao
interface PodcastDAO {
    @Query("SELECT * FROM podcastentity")
    fun getAll(): List<PodcastEntity>
//
//    @Query("SELECT * FROM podcastentity WHERE id LIKE :id LIMIT 1")
//    fun findById(title: String, id: String): PodcastEntity

    @Insert
    fun insertAll(vararg podcastEntities: PodcastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(podcastEntity: PodcastEntity): Single<Long>

    @Delete
    fun delete(user: PodcastEntity)
}