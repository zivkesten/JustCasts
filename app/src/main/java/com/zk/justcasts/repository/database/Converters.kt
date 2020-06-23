package com.zk.justcasts.repository.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun fromArrayList(list: List<Int?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrayList(list: String): List<Int>? {
        val type: Type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson<List<Int>>(list, type)
    }
}