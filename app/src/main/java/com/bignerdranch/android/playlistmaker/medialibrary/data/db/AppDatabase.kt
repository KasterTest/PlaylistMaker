package com.bignerdranch.android.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao.FavoriteTracksDao
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao.PlaylistDao
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.TypeConverter


@Database(version = 6, entities = [TrackEntity::class, PlaylistEntity::class])
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): FavoriteTracksDao
    abstract fun playlistsDao(): PlaylistDao
}