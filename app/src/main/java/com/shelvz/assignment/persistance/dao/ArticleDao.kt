package com.shelvz.assignment.persistance.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.shelvz.assignment.models.Article

@Dao
interface ArticleDao : BaseDao {
    @Query("SELECT * FROM articles")
    fun get(): List<Article>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: String): Article

    @Query("DELETE FROM articles")
    fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<Article>)
}

