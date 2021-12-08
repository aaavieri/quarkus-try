package cn.yjl.repository

import cn.yjl.entity.Area
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AreaRepository : PanacheRepository<Area> {

    fun findByCode(code: String): Uni<Area?> {
        return find("code = ?1", code).firstResult()
    }
}