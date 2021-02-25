package ar.com.apidb.api.controller

import ar.com.apidb.api.model.Visita
import ar.com.apidb.api.model.PrsVisita
import ar.com.apidb.db.Entity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class PrsVisitaServlet : HttpServlet() {
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
                    resp.outputStream.write(gson.toJson(PrsVisita().getAll(PrsVisita.PRS_VISITA_PESEPRS_ID, false)).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 2 && path[0] == "getId" -> {
                    resp.outputStream.write(gson.toJson(PrsVisita.getById(path[1].toInt())).toByteArray(StandardCharsets.UTF_8))
                    resp.contentType = "application/json"
                    resp.status = HttpServletResponse.SC_OK
                }
                path.size == 3 && path[0] == "getIncumplimientoId" -> {
                    resp.outputStream.write(gson.toJson(PrsVisita.getByCodArtandPeseprsId(path[1].toString(), path[2].toInt())).toByteArray(StandardCharsets.UTF_8))
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
            val prsVisita = gson.fromJson(req.reader, PrsVisita::class.java)
            if (prsVisita != null) {
                val visita = Visita.getByCodArt(prsVisita.prs_visita_codigo_art!!)
                if (visita != null) {
                    val aux = PrsVisita.getByCodArtandPeseprsId(prsVisita.prs_visita_codigo_art!!, prsVisita.prs_visita_peseprs_id!!)
                    if (aux == null) {
                        prsVisita.prs_visita_visita_id = visita.visita_id
                        if (prsVisita.insert() > 0) {
                            resp.outputStream.write(gson.toJson(prsVisita).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        prsVisita.prs_visita_visita_id = visita.visita_id
                        if (prsVisita.updatePrsVisita(prsVisita.prs_visita_codigo_art!!, prsVisita.prs_visita_peseprs_id!!) > 0) {
                            resp.outputStream.write(gson.toJson(prsVisita).toByteArray(StandardCharsets.UTF_8))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al actualizar Prs Visita"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_NOT_FOUND
                        }
                    }
                }
            } else {
                resp.outputStream.write(SimpleMsg.create("Datos incorrectos del PRS Visita"))
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
                path.size == 3 && path[0] == "deletevisitaincumplimiento" -> {
                    val prsVisita = PrsVisita.getByCodArtandPeseprsId(path[1].toString(), path[2].toInt())
                    if (prsVisita != null) {
                        if (prsVisita.delete() > 0) {
                            resp.outputStream.write(SimpleMsg.create("Prs Visita ha sido eliminada con éxito"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        } else {
                            resp.outputStream.write(SimpleMsg.create("Error al eliminar la Prs Visita"))
                            resp.contentType = "application/json"
                            resp.status = HttpServletResponse.SC_OK
                        }
                    } else {
                        resp.outputStream.write(SimpleMsg.create("Datos incorrectos en la Prs Visita"))
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