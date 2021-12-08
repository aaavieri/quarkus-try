package cn.yjl.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AreaResDto(
    @field:JsonProperty("jsonId")
    var id: Long = 0,
    @field:JsonProperty("jsonCode")
    var code: String = "",
    @field:JsonProperty("jsonName")
    var name: String = ""
)