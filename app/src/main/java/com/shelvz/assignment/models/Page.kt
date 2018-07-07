package com.shelvz.assignment.models

data class Page(override var pageSize: Int?, override var currentPage: Int?) : PageInfo

interface PageInfo {
    var pageSize: Int?
    var currentPage: Int?
}
