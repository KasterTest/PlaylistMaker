package com.bignerdranch.android.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.dao.FavoriteTracksDao
import com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity.TrackEntity


@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): FavoriteTracksDao
}