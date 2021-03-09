package ar.com.apidb.api.controller

import ar.com.apidb.api.model.Visita
import ar.com.apidb.api.model.VisitaAgrupacion
import ar.com.apidb.db.Entity.Companion.LOG
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class VisitaAgrupacionServlet : HttpServlet() {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
        gson = gsonBuilder.create()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 1 && path[0] == "getAll" -> {
                    resp.outputStream.write(gson.toJson(VisitaAgrupacion().getAll(VisitaAgrupacion.VISITA_AGRUPACION_AGRUPACION_ID, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getId" -> {
                    resp.outputStream.write(gson.toJson(VisitaAgrupacion.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 3 && path[0] == "getAgrupacionId" -> {
                    resp.outputStream.write(gson.toJson(VisitaAgrupacion.getByCodArtAndAgrId(path[1].toString(), path[2].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
            }
        } catch (e: Exception) {
            LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            Thread.sleep(15000)
            val visitaAgrupacion = gson.fromJson(req.reader, VisitaAgrupacion::class.java)
            if (visitaAgrupacion != null) {
                val visita = Visita.getByCodArt(visitaAgrupacion.visita_agrupacion_codigo_art!!)
                if (visita != null) {
                    val aux = VisitaAgrupacion.getByCodArtAndAgrId(visitaAgrupacion.visita_agrupacion_codigo_art!!, visitaAgrupacion.visita_agrupacion_agrupacion_id!!)
                    if (aux == null) {
                        visitaAgrupacion.visita_agrupacion_visita_id = visita.visita_id
                        if (visitaAgrupacion.insert() > 0) {
                            resp.outputStream.write(gson.toJson(visitaAgrupacion).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        visitaAgrupacion.visita_agrupacion_visita_id = visita.visita_id
                        if (visitaAgrupacion.updateVisitaAgrupacion(visitaAgrupacion.visita_agrupacion_codigo_art!!, visitaAgrupacion.visita_agrupacion_agrupacion_id!!) > 0) {
                            resp.outputStream.write(gson.toJson(visitaAgrupacion).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al actualizar Visita Agrupacion"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_NOT_FOUND
                        }
                    }
                }
            } else {
                resp.outputStream.write(SimpleMsg.create("Datos incorrectos de las agrupaciones"))
                resp.contentType = "application/json"
                resp.status = HttpServletResponse.SC_NOT_FOUND
            }
        } catch (e: Exception) {
            LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 3 && path[0] == "deletevisitaagrupacion" -> {
                    val visitaAgrupacion = VisitaAgrupacion.getByCodArtAndAgrId(path[1].toString(), path[2].toInt())
                    if (visitaAgrupacion != null) {
                        if (visitaAgrupacion.delete() > 0) {
                            resp.outputStream.write(SimpleMsg.create("Visita Agrupacion ha sido eliminada con éxito"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al eliminar la Visita Agrupacion"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la Visita Agrupacion"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                }
            }
        } catch (e: Exception) {
            LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }
}