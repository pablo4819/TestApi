package ar.com.apidb.api.model

import ar.com.apidb.db.Cursor
import ar.com.apidb.db.Db
import ar.com.apidb.db.Entity
import ar.com.apidb.db.Values
import ar.com.apidb.helper.FDate
import java.util.*

class PrsVisita : Entity<Int, PrsVisita> {
    enum class Operacion {
        A, M, B
    }

    enum class Cumple {
        S, N
    }

    var prs_visita_id: Int? = null
    var prs_visita_visita_id: Int? = null
    var prs_visita_peseprs_id: Int? = null
    var prs_visita_cumple: Cumple? = null
    var prs_visita_foss: Date? = null
    var prs_visita_opss: Operacion? = null
    var prs_visita_rechazo_id: Int? = null
    var prs_visita_codigo_art: String? = null

    constructor()

    constructor(id: Int) {
        try {
            get(id)
        } catch (e: Exception) {
            LOG.error("Error instantiating " + javaClass.simpleName + " class ", e)
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
        //table definicion
        const val TABLE = "prs_visita"
        const val PRS_VISITA_ID = "prs_visita_id"
        const val PRS_VISITA_VISITA_ID = "prs_visita_visita_id"
        const val PRS_VISITA_PESEPRS_ID = "prs_visita_peseprs_id"
        const val PRS_VISITA_CUMPLE = "prs_visita_cumple"
        const val PRS_VISITA_FOSS = "prs_visita_foss"
        const val PRS_VISITA_OPSS = "prs_visita_opss"
        const val PRS_VISITA_RECHAZO_ID = "prs_visita_rechazo_id"
        const val PRS_VISITA_CODIGO_ART = "prs_visita_codigo_art"

        fun getById(prs_visita_id: Int): List<PrsVisita> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Visita::class.java, TABLE, "$PRS_VISITA_ID=?", prs_visita_id) as List<PrsVisita>
        }

        fun getByCodArtandPeseprsId(codeArt: String, peseprs_id: Int): PrsVisita? {
            @Suppress("UNCHECKED_CAST")
            val result = Db.get().search(PrsVisita::class.java, TABLE, "$PRS_VISITA_CODIGO_ART=? AND $PRS_VISITA_PESEPRS_ID =?", codeArt, peseprs_id) as List<PrsVisita>
            return if (result.isNotEmpty()) result[0] else null
        }
    }

    override val cv: Values
        get() {
            val values = Db.newCV()
            values.put(PRS_VISITA_VISITA_ID, prs_visita_visita_id)
            values.put(PRS_VISITA_PESEPRS_ID, prs_visita_peseprs_id)
            values.put(PRS_VISITA_CUMPLE, prs_visita_cumple)
            values.put(PRS_VISITA_FOSS, FDate.formatU(prs_visita_foss))
            values.put(PRS_VISITA_OPSS, prs_visita_opss)
            values.put(PRS_VISITA_RECHAZO_ID, prs_visita_rechazo_id)
            values.put(PRS_VISITA_CODIGO_ART, prs_visita_codigo_art)

            return values
        }

    override fun map(c: Cursor) {
        id = c.getInt(ID)
        prs_visita_visita_id = c.getInt(PRS_VISITA_VISITA_ID)
        prs_visita_peseprs_id = c.getInt(PRS_VISITA_PESEPRS_ID)
        prs_visita_cumple = if (c.getString(PRS_VISITA_CUMPLE) != null) Cumple.valueOf(c.getString(PRS_VISITA_CUMPLE)!!) else null
        prs_visita_foss = FDate.parseDate(c.getString(PRS_VISITA_FOSS))
        prs_visita_opss = if (c.getString(PRS_VISITA_OPSS) != null) Operacion.valueOf(c.getString(PRS_VISITA_OPSS)!!) else null
        prs_visita_rechazo_id = c.getInt(PRS_VISITA_RECHAZO_ID)
        prs_visita_codigo_art = c.getString(PRS_VISITA_CODIGO_ART)
    }

    fun updatePrsVisita(codeArt: String, peseprs_id: Int): Int {
        return Db.get().update(TABLE, cv, "$PRS_VISITA_CODIGO_ART=? and $PRS_VISITA_PESEPRS_ID=?", codeArt, peseprs_id)
    }

    override var id: Int?
        get() = prs_visita_id
        set(value) {
            prs_visita_id = value
        }
    override val idColumnName: String
        get() = PRS_VISITA_ID

    override val table: String
        get() = TABLE
    override val ID: String
        get() = "prs_visita_id"
}