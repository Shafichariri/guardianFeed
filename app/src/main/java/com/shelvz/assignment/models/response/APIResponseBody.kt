package com.shelvz.assignment.models.response

import com.shelvz.assignment.models.BaseModel

data class APIResponseBody<T : BaseModel>(
        override var status: String? = null,
        override var userTier: String? = null,
        override var total: Int? = null,
        override var startIndex: Int? = null,
        override var pageSize: Int? = null,
        override var currentPage: Int? = null,
        override var pages: Int? = null,
        override var orderBy: String? = null,
        override var results: List<T>? = null) : APIBaseResponseBody<T>

interface APIBaseResponseBody<T : BaseModel> {
    var status: String?
    var userTier: String?
    var total: Int?
    var startIndex: Int?
    var pageSize: Int?
    var currentPage: Int?
    var pages: Int?
    var orderBy: String?
    var results: List<T>?
}
