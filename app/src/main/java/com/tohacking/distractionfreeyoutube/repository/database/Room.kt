package com.tohacking.distractionfreeyoutube.repository.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {
    @Query("SELECT * FROM databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    @Query("SELECT * FROM databasevideo WHERE videoId = :videoId LIMIT 1")
    fun getVideo(videoId: String): LiveData<DatabaseVideo>

    @Insert(entity = DatabaseVideo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)

    @Insert(entity = DatabaseVideo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: DatabaseVideo)

    @Insert(entity = DatabasePlaylist::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: DatabasePlaylist)

    @Query("SELECT * FROM databaseplaylist WHERE `index` = :index")
    fun getPlaylist(index: Int): LiveData<DatabasePlaylist>
}

@Database(entities = [DatabaseVideo::class, DatabasePlaylist::class], version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract val databaseDao: VideoDao
}

private lateinit var INSTANCE: YoutubeDatabase

fun getDatabase(context: Context): YoutubeDatabase {
    synchronized(YoutubeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                YoutubeDatabase::class.java,
                "videos_playlists"
            ).build()
        }
    }
    return INSTANCE
}