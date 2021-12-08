package cn.yjl.handler

import org.jboss.logging.Logger
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class CommonExceptionHandler : ExceptionMapper<Exception> {
    override fun toResponse(e: Exception): Response {
        LOG.error(e.message, e)
        return Response.ok().entity(mapOf("code" to 3, "msg" to e.message)).build()
    }

    companion object {
        private val LOG = Logger.getLogger(CommonExceptionHandler::class.java)
    }
}