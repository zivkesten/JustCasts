package com.zk.justcasts.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zk.justcasts.repository.database.podcast.PodcastEntity
import com.zk.justcasts.repository.database.podcast.PodcastDAO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { provideDataBase(androidContext()) }
}

private fun provideDataBase(context: Context): ShowsDatabase {
    return Room.databaseBuilder(
        context,
        ShowsDatabase::class.java, ShowsDatabase::class.java.simpleName
    ).build()
}

@Database(entities = [PodcastEntity::class], version = 1)

abstract class ShowsDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDAO
}