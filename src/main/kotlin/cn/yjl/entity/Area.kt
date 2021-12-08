package cn.yjl.entity

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase
import javax.persistence.*

@Entity
@Table(name = "t_area")
open class Area : PanacheEntityBase() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    open var id: Long = 0L

    @Column(name = "code", nullable = false)
    open var code: String = ""

    @Column(name = "name")
    open var name: String? = null

    @Column(name = "cityCode")
    open var cityCode: String? = null
}