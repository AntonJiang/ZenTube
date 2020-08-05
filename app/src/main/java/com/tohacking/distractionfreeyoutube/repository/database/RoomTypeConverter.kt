package com.tohacking.distractionfreeyoutube.repository.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tohacking.distractionfreeyoutube.repository.data.VideoSnippet

class RoomTypeConverter {
    companion object {
        val gson = Gson()
    }

    @TypeConverter
    fun fromVideoSnippet(value: VideoSnippet): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toVideoSnippet(value: String): VideoSnippet {
        return gson.fromJson(value, VideoSnippet::class.java)
    }

    @TypeConverter
    fun fromListString(value: List<String>): String {
        return value.joinToString("|")
    }

    @TypeConverter
    fun toListString(value: String): List<String> {
        return if (value == "") listOf() else value.split("|")
    }
}