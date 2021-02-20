package ar.com.apidb.api.controller

import ar.com.apidb.api.model.Visita
import ar.com.apidb.api.model.VisitaIncumplimiento
import ar.com.apidb.db.Entity.Companion.LOG
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class VisitaIncumplimientoServlet : HttpServlet() {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss")
        gson = gsonBuilder.create()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 1 && path[0] == "getAll" -> {
                    resp.outputStream.write(gson.toJson(VisitaIncumplimiento().getAll(VisitaIncumplimiento.VISITA_INCUMPLIMIENTO_INCUMPLIMIENTO_ID, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getId" -> {
                    resp.outputStream.write(gson.toJson(VisitaIncumplimiento.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 3 && path[0] == "getIncumplimientoId" -> {
                    resp.outputStream.write(gson.toJson(VisitaIncumplimiento.getByCodArtandIncId(path[1].toString(), path[2].toInt())).toByteArray(StandardCharsets.UTF_8))
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
            val visitaIncumplimiento = gson.fromJson(req.reader, VisitaIncumplimiento::class.java)
            if (visitaIncumplimiento != null) {
                val visita = Visita.getByCodArt(visitaIncumplimiento.visita_incumplimiento_codigo_art!!)
                if (visita != null) {
                    val aux = VisitaIncumplimiento.getByCodArtandIncId(visitaIncumplimiento.visita_incumplimiento_codigo_art!!, visitaIncumplimiento.visita_incumplimiento_incumplimiento_id!!)
                    if (aux == null) {
                        visitaIncumplimiento.visita_incumplimiento_visita_id = visita.visita_id
                        if (visitaIncumplimiento.insert() > 0) {
                            resp.outputStream.write(gson.toJson(visitaIncumplimiento).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        visitaIncumplimiento.visita_incumplimiento_visita_id = visita.visita_id
                        if (visitaIncumplimiento.updateVisitaIncumplimiento(visitaIncumplimiento.visita_incumplimiento_codigo_art!!, visitaIncumplimiento.visita_incumplimiento_incumplimiento_id!!) > 0) {
                            resp.outputStream.write(gson.toJson(visitaIncumplimiento).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al actualizar Visita Incumplimiento"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_NOT_FOUND
                        }
                    }
                }
            } else {
                resp.outputStream.write(SimpleMsg.create("Datos incorrectos de los incumplimientos"))
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
                path.size == 3 && path[0] == "deletevisitaincumplimiento" -> {
                    val visitaIncumplimiento = VisitaIncumplimiento.getByCodArtandIncId(path[1].toString(), path[2].toInt())
                    if (visitaIncumplimiento != null) {
                        if (visitaIncumplimiento.delete() > 0) {
                            resp.outputStream.write(SimpleMsg.create("Visita Incumplimiento ha sido eliminada con éxito"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al eliminar la Visita Incumplimiento"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la Visita Incumplimiento"))
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