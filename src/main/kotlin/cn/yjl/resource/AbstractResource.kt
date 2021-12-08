package cn.yjl.resource

import cn.yjl.dto.response.ResponseDto

abstract class AbstractResource {
    fun <T> success(data: T) = ResponseDto<T>(0, "ok", data)

    fun <T> fail(errMsg: String) =  ResponseDto<T>(1, errMsg, null)
}