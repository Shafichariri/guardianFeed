package com.shelvz.assignment.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.shelvz.assignment.kit.base.BaseModel

@Entity(tableName = "articles")
data class Article(
        @PrimaryKey()
        @ColumnInfo(name = "id")
        @SerializedName("id")
        override var id: String,
        var type: String,
        var sectionId: String? = null,
        var sectionName: String? = null,
        var webPublicationDate: String? = null,
        var webTitle: String? = null,
        var webUrl: String? = null,
        var apiUrl: String? = null,
        @Embedded(prefix = "article_")
        var fields: ArticleFields? = null) : BaseModel

@Entity(tableName = "articleFields")
data class ArticleFields(@PrimaryKey(autoGenerate = true)
                         @ColumnInfo(name = "id")
                         @SerializedName("id")
                         var id: Int? = null,
                         @ColumnInfo(name = "thumbnail")
                         var thumbnail: String? = null) : BaseFields
