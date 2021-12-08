package cn.yjl.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class ResponseDto<T> (
    @field:JsonProperty("code")
    var code: Int = 0,
    @field:JsonProperty("msg")
    var msg: String = "",
    @field:JsonProperty("data")
    var data: T? = null)