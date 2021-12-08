package cn.yjl.service

import cn.yjl.entity.Area
import cn.yjl.repository.AreaRepository
import cn.yjl.dto.response.AreaResDto
import io.quarkus.redis.client.reactive.ReactiveRedisClient
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ApiService {

    @Inject
    lateinit var redisClient: ReactiveRedisClient

    @Inject
    lateinit var areaRepository: AreaRepository

    suspend fun getArea(code: String?): AreaResDto {
        code ?: throw RuntimeException("code is null")
        val area: Area = this.areaRepository.findByCode(code).awaitSuspending() ?: throw RuntimeException("no data")
        return AreaResDto(area.id, area.code, area.name ?: "")
    }
}