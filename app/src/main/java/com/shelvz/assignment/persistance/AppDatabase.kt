package com.shelvz.assignment.persistance

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.shelvz.assignment.models.Article
import com.shelvz.assignment.persistance.dao.ArticleDao

@Database(entities = [Article::class],
        version = AppDatabase.DATABASE_VERSION, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "shelvz.db"
        private val DATABASE_CLASS_TYPE = AppDatabase::class.java

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        DATABASE_CLASS_TYPE, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
    }
}
