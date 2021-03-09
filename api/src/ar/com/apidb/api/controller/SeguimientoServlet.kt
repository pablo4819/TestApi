package ar.com.apidb.api.controller

import ar.com.apidb.api.model.Seguimiento
import ar.com.apidb.api.model.Visita
import ar.com.apidb.db.Entity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SeguimientoServlet : HttpServlet() {
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
                    resp.outputStream.write(gson.toJson(Seguimiento().getAll(Seguimiento.SEGUIMIENTO_VISITA_ID, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getId" -> {
                    resp.outputStream.write(gson.toJson(Seguimiento.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 3 && path[0] == "getAgrupacionId" -> {
                    resp.outputStream.write(gson.toJson(Seguimiento.getByCodArtAndRecId(path[1].toString(), path[2].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
            }
        } catch (e: Exception) {
            Entity.LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            Thread.sleep(15000)
            val seguimiento = gson.fromJson(req.reader, Seguimiento::class.java)
            if (seguimiento != null) {
                val visita = Visita.getByCodArt(seguimiento.seguimiento_codigo_art!!)
                if (visita != null) {
                    val aux = Seguimiento.getByCodArtAndRecId(seguimiento.seguimiento_codigo_art!!, seguimiento.seguimiento_recomendacion_id!!)
                    seguimiento.seguimiento_visita_id = visita.visita_id
                    if (aux == null) {
                        if (seguimiento.insert() > 0) {
                            resp.outputStream.write(gson.toJson(seguimiento).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        if (seguimiento.updateSeguimiento(seguimiento.seguimiento_codigo_art!!, seguimiento.seguimiento_recomendacion_id!!) > 0) {
                            resp.outputStream.write(gson.toJson(seguimiento).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al actualizar Seguimiento"))
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
            Entity.LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val path = StringUtils.split(req.pathInfo, '/')
            when {
                path.size == 3 && path[0] == "deleteseguimiento" -> {
                    val seguimiento = Seguimiento.getByCodArtAndRecId(path[1].toString(), path[2].toInt())
                    if (seguimiento != null) {
                        if (seguimiento.delete() > 0) {
                            resp.outputStream.write(SimpleMsg.create("Seguimiento ha sido eliminada con éxito"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al eliminar Seguimiento"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Datos incorrectos en Seguimiento"))
                        resp.contentType = "application/json"
                        resp.status = HttpServletResponse.SC_NOT_FOUND
                    }
                }
            }
        } catch (e: Exception) {
            Entity.LOG.error("Invalid request", e)
            resp.outputStream.write(SimpleMsg.create(e))
            resp.contentType = "application/json"
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }
}