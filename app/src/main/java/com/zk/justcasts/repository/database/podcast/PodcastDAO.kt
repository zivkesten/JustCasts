package com.zk.justcasts.repository.database.podcast

import androidx.room.*

@Dao
interface PodcastDAO {
    @Query("SELECT * FROM podcastentity")
    suspend fun getAll(): List<PodcastEntity>
//
//    @Query("SELECT * FROM podcastentity WHERE id LIKE :id LIMIT 1")
//    fun findById(title: String, id: String): PodcastEntity

    @Insert
    fun insertAll(vararg podcastEntities: PodcastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(podcastEntity: PodcastEntity): Long

    @Delete
    fun delete(user: PodcastEntity)
}