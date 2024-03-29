package com.bignerdranch.android.playlistmaker.medialibrary.data.db.entity

import androidx.room.TypeConverter
import java.util.Date

class TypeConverter {
    @TypeConverter
    fun convert(value: Date?): Long? {
        return value?.time
    }
    
    @TypeConverter
    fun convert(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}