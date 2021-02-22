package ar.com.apidb.api.model

import ar.com.apidb.db.Cursor
import ar.com.apidb.db.Db
import ar.com.apidb.db.Entity
import ar.com.apidb.db.Values
import ar.com.apidb.helper.FDate
import java.util.*

class VisitaAgrupacion : Entity<Int, VisitaAgrupacion> {

    enum class Operacion {
        A, M, B
    }

    var visita_agrupacion_id: Int? = null
    var visita_agrupacion_visita_id: Int? = null
    var visita_agrupacion_agrupacion_id: Int? = null
    var visita_agrupacion_fechanotificacion: Date? = null
    var visita_agrupacion_fechaverificacion: Date? = null
    var visita_agrupacion_fecharegularizacion: Date? = null
    var visita_agrupacion_tipooperacion: Operacion? = null
    var visita_agrupacion_cumplido: Int? = null
    var visita_agrupacion_nota: String? = null
    var visita_agrupacion_codigo_art: String? = null

    constructor()

    constructor(id: Int) {
        try {
            get(id)
        } catch (e: Exception) {
            LOG.error("Error instantiating " + javaClass.simpleName + " class", e)
        }
    }

    constructor(c: Cursor) {
        try {
            map(c)
        } catch (e: Exception) {
            LOG.error("Error instantiating " + javaClass.simpleName + " class", e)
        }
    }

    companion object {
        const val TABLE = "visita_agrupacion"
        const val VISITA_AGRUPACION_ID = "visita_agrupacion_id"
        const val VISITA_AGRUPACION_VISITA_ID = "visita_agrupacion_visita_id"
        const val VISITA_AGRUPACION_AGRUPACION_ID = "visita_agrupacion_agrupacion_id"
        const val VISITA_AGRUPACION_FECHANOTIFICACION = "visita_agrupacion_fechanotificacion"
        const val VISITA_AGRUPACION_FECHAVERIFICACION = "visita_agrupacion_fechaverificacion"
        const val VISITA_AGRUPACION_FECHAREGULARIZACION = "visita_agrupacion_fecharegularizacion"
        const val VISITA_AGRUPACION_TIPOOPERACION = "visita_agrupacion_tipooperacion"
        const val VISITA_AGRUPACION_CUMPLIDO = "visita_agrupacion_cumplido"
        const val VISITA_AGRUPACION_NOTA = "visita_agrupacion_nota"
        const val VISITA_AGRUPACION_CODIGO_ART = "visita_agrupacion_codigo_art"

        fun getById(visita_agrupacion_id: Int): List<VisitaAgrupacion> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(VisitaAgrupacion::class.java, TABLE, "${VISITA_AGRUPACION_ID}=?", visita_agrupacion_id) as List<VisitaAgrupacion>
        }

        fun getByCodArtAndAgrId(codeArt: String, agrupacion_id: Int): VisitaAgrupacion? {
            @Suppress("UNCHECKED_CAST")
            val result = Db.get().search(VisitaAgrupacion::class.java, TABLE, "$VISITA_AGRUPACION_CODIGO_ART=? AND $VISITA_AGRUPACION_AGRUPACION_ID =?", codeArt, agrupacion_id) as List<VisitaAgrupacion>
            return if (result.isNotEmpty()) result[0] else null
        }
    }

    override val cv: Values
        get() {
            val values = Db.newCV()
            values.put(VISITA_AGRUPACION_VISITA_ID, visita_agrupacion_visita_id)
            values.put(VISITA_AGRUPACION_AGRUPACION_ID, visita_agrupacion_agrupacion_id)
            values.put(VISITA_AGRUPACION_FECHANOTIFICACION, FDate.formatU(visita_agrupacion_fechanotificacion))
            values.put(VISITA_AGRUPACION_FECHAVERIFICACION, FDate.formatU(visita_agrupacion_fechaverificacion))
            values.put(VISITA_AGRUPACION_FECHAREGULARIZACION, FDate.formatU(visita_agrupacion_fecharegularizacion))
            values.put(VISITA_AGRUPACION_TIPOOPERACION, visita_agrupacion_tipooperacion)
            values.put(VISITA_AGRUPACION_CUMPLIDO, visita_agrupacion_cumplido)
            values.put(VISITA_AGRUPACION_NOTA, visita_agrupacion_nota)
            values.put(VISITA_AGRUPACION_CODIGO_ART, visita_agrupacion_codigo_art)

            return values
        }

    override fun map(c: Cursor) {
        id = c.getInt(ID)
        visita_agrupacion_visita_id = c.getInt(VISITA_AGRUPACION_VISITA_ID)
        visita_agrupacion_agrupacion_id = c.getInt(VISITA_AGRUPACION_AGRUPACION_ID)
        visita_agrupacion_fechanotificacion = FDate.parseDate(c.getString(VISITA_AGRUPACION_FECHANOTIFICACION))
        visita_agrupacion_fechaverificacion = FDate.parseDate(c.getString(VISITA_AGRUPACION_FECHAVERIFICACION))
        visita_agrupacion_fecharegularizacion = FDate.parseDate(c.getString(VISITA_AGRUPACION_FECHAREGULARIZACION))
        visita_agrupacion_tipooperacion = if (c.getString(VISITA_AGRUPACION_TIPOOPERACION) != null) Operacion.valueOf(c.getString(VISITA_AGRUPACION_TIPOOPERACION)!!) else null
        visita_agrupacion_cumplido = c.getInt(VISITA_AGRUPACION_CUMPLIDO)
        visita_agrupacion_nota = c.getString(VISITA_AGRUPACION_NOTA)
        visita_agrupacion_codigo_art = c.getString(VISITA_AGRUPACION_CODIGO_ART)
    }

    fun updateVisitaAgrupacion(codeArt: String, agrupacionId: Int): Int {
        return Db.get().update(TABLE, cv, "${VISITA_AGRUPACION_CODIGO_ART}=? and ${VISITA_AGRUPACION_AGRUPACION_ID}=?", codeArt, agrupacionId)
    }

    override var id: Int?
        get() = visita_agrupacion_id
        set(value) {
            visita_agrupacion_id = value
        }
    override val idColumnName: String
        get() = VISITA_AGRUPACION_ID

    override val table: String
        get() = TABLE
    override val ID: String
        get() = "visita_agrupacion_id"
}