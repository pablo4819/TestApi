package ar.com.apidb.db

import ar.com.apidb.db.Database
import ar.com.apidb.db.Values

object Db {

    private var db: Database? = null

    fun setPlatform(db: Database) {
        Db.db = db
        db.open()
    }

    fun get(): Database {
        return db!!
    }

    fun newCV(): Values {
        return db!!.newValues()
    }

}