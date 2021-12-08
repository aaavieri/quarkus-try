package cn.yjl.resource

import cn.yjl.dto.request.AreaReqDto
import cn.yjl.dto.response.AreaResDto
import cn.yjl.dto.response.ResponseDto
import cn.yjl.service.ApiService
import org.jboss.logging.Logger
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ApiResource: AbstractResource() {

    companion object {
        private val LOG = Logger.getLogger(ApiResource::class.java)
    }

    @Inject
    lateinit var apiService: ApiService

    @POST
    @Path("/getArea")
    suspend fun getArea(data: AreaReqDto): ResponseDto<AreaResDto> {
        LOG.debugf("api getArea input: %s", data)
        val areaResDto: AreaResDto = apiService.getArea(data.code)
        LOG.debugf("api getArea output: %s", areaResDto)
        return this.success(areaResDto)
    }
}