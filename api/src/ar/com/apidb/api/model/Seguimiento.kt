package ar.com.apidb.api.model

import ar.com.apidb.db.Cursor
import ar.com.apidb.db.Db
import ar.com.apidb.db.Entity
import ar.com.apidb.db.Values
import ar.com.apidb.helper.FDate
import java.util.*

class Seguimiento : Entity<Int, Seguimiento> {

    enum class Operacion {
        A, M
    }

    enum class Tipo {
        V, I, C
    }

    var seguimiento_id: Int? = null
    var seguimiento_visita_id: Int? = null
    var seguimiento_recomendacion_id: Int? = null
    var seguimiento_fverificacion: Date? = null
    var seguimiento_tipo: Tipo? = null
    var seguimiento_fois: Date? = null
    var seguimiento_opis: Operacion? = null
    var seguimiento_codigo_art: String? = null

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
        const val TABLE = "seguimiento"
        const val SEGUIMIENTO_ID = "seguimiento_id"
        const val SEGUIMIENTO_VISITA_ID = "seguimiento_visita_id"
        const val SEGUIMIENTO_RECOMENDACION_ID = "seguimiento_recomendacion_id"
        const val SEGUIMIENTO_FVERIFICACION = "seguimiento_fverificacion"
        const val SEGUIMIENTO_FOIS = "seguimiento_fois"
        const val SEGUIMIENTO_OPIS = "seguimiento_opis"
        const val SEGUIMIENTO_TIPO = "seguimiento_tipo"
        const val SEGUIMIENTO_CODIGO_ART = "seguimiento_codigo_art"

        fun getById(seguimiento_id: Int): List<Seguimiento> {
            @Suppress("UNCHECKED_CAST")
            return Db.get().search(Seguimiento::class.java, TABLE, "${SEGUIMIENTO_ID}=?", seguimiento_id) as List<Seguimiento>
        }

        fun getByCodArtAndRecId(codeArt: String, recomendacion_id: Int): Seguimiento? {
            @Suppress("UNCHECKED_CAST")
            val result = Db.get().search(Seguimiento::class.java, TABLE, "$SEGUIMIENTO_CODIGO_ART=? AND $SEGUIMIENTO_RECOMENDACION_ID =?", codeArt, recomendacion_id) as List<Seguimiento>
            return if (result.isNotEmpty()) result[0] else null
        }
    }

    override val cv: Values
        get() {
            val values = Db.newCV()
            values.put(SEGUIMIENTO_VISITA_ID, seguimiento_visita_id)
            values.put(SEGUIMIENTO_RECOMENDACION_ID, seguimiento_recomendacion_id)
            values.put(SEGUIMIENTO_FVERIFICACION, FDate.formatU(seguimiento_fverificacion))
            values.put(SEGUIMIENTO_FOIS, FDate.formatU(seguimiento_fois))
            values.put(SEGUIMIENTO_OPIS, seguimiento_opis)
            values.put(SEGUIMIENTO_TIPO, seguimiento_tipo)
            values.put(SEGUIMIENTO_CODIGO_ART, seguimiento_codigo_art)

            return values
        }

    override fun map(c: Cursor) {
        id = c.getInt(ID)
        seguimiento_visita_id = c.getInt(SEGUIMIENTO_VISITA_ID)
        seguimiento_recomendacion_id = c.getInt(SEGUIMIENTO_RECOMENDACION_ID)
        seguimiento_fverificacion = FDate.parseDate(c.getString(SEGUIMIENTO_FVERIFICACION))
        seguimiento_fois = FDate.parseDate(c.getString(SEGUIMIENTO_FOIS))
        seguimiento_opis = if (c.getString(SEGUIMIENTO_OPIS) != null) Operacion.valueOf(c.getString(SEGUIMIENTO_OPIS)!!) else null
        seguimiento_tipo = if (c.getString(SEGUIMIENTO_TIPO) != null) Tipo.valueOf(c.getString(SEGUIMIENTO_TIPO)!!) else null
        seguimiento_codigo_art = c.getString(SEGUIMIENTO_CODIGO_ART)
    }

    fun updateSeguimiento(codeArt: String, agrupacionId: Int): Int {
        return Db.get().update(TABLE, cv, "${SEGUIMIENTO_CODIGO_ART}=? and ${SEGUIMIENTO_RECOMENDACION_ID}=?", codeArt, agrupacionId)
    }

    override var id: Int?
        get() = seguimiento_id
        set(value) {
            seguimiento_id = value
        }
    override val idColumnName: String
        get() = SEGUIMIENTO_ID

    override val table: String
        get() = TABLE
    override val ID: String
        get() = "seguimiento_id"
}