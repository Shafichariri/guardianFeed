package com.shelvz.assignment.models.response

import com.shelvz.assignment.kit.base.BaseModel

data class APIResponse<T : BaseModel>(
        override var response: APIResponseBody<T>? = null) : APIBaseResponse<T>

interface APIBaseResponse<T : BaseModel> {
    var response: APIResponseBody<T>?
}

