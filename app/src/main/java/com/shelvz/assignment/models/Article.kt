package com.shelvz.assignment.models

data class Article(
        override var id: String,
        var type: String,
        var sectionId: String? = null,
        var sectionName: String? = null,
        var webPublicationDate: String? = null,
        var webTitle: String? = null,
        var webUrl: String? = null,
        var apiUrl: String? = null,
        var fields: ArticleFields? = null) : BaseModel

data class ArticleFields(var thumbnail: String? = null) : BaseFields
