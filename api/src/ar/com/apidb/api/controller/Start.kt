package ar.com.apidb.api.controller

import ar.com.apidb.db.Db
import ar.com.apidb.mysql.MysqlHelper
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.slf4j.LoggerFactory
import java.io.File
import org.eclipse.jetty.webapp.WebAppContext

import org.eclipse.jetty.servlet.ServletHolder

class Start {
    private fun start() {
        try {
            if (!File(LOG_BACK_FILE).exists()) throw Exception("No se encuentra el archivo de configuración de archivos logs $LOG_BACK_FILE")
            val context = LoggerFactory.getILoggerFactory() as LoggerContext
            context.reset()
            val configurator = JoranConfigurator()
            configurator.context = context
            configurator.doConfigure(LOG_BACK_FILE)

            if (!File(DB_PROPERTIES).exists()) throw Exception("No se encuentra el archivo de configuración de base de datos $DB_PROPERTIES")
            Db.setPlatform(MysqlHelper(DB_PROPERTIES))

            if (!File(WS_PROPERTIES).exists()) throw Exception("No se encuentra el archivo de configuración del servidor web $WS_PROPERTIES")
            WebParams.getParams(WS_PROPERTIES)

            val redLatitud = WebAppContext()
            redLatitud.contextPath = "/api"
            redLatitud.resourceBase = javaClass.classLoader.getResource("ar/com/apidb/api/view")!!.toExternalForm()
            redLatitud.isParentLoaderPriority = true

            redLatitud.addServlet(ServletHolder(LogSyncServlet()), "/api/logsync/*")
            redLatitud.addServlet(ServletHolder(VisitaServlet()), "/api/visita/*")
            redLatitud.addServlet(ServletHolder(VisitaIncumplimientoServlet()), "/api/visitaincumplimiento/*")
            redLatitud.addServlet(ServletHolder(VisitaAgrupacionServlet()), "/api/visitaagrupacion/*")
            redLatitud.addServlet(ServletHolder(SeguimientoServlet()), "/api/seguimiento/*")
            redLatitud.addServlet(ServletHolder(PrsVisitaServlet()), "/api/prsvisita/*")

            val contexts = ContextHandlerCollection()
            contexts.handlers = arrayOf<Handler>(redLatitud)

            val server = Server(WebParams.webport)
            server.handler = contexts
            server.start()

        } catch (e: Exception) {
            LOG.error("Error", e)
        }
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(Start::class.java)
        private const val LOG_BACK_FILE = "logback.xml"
        private const val DB_PROPERTIES = "db.properties"
        private const val WS_PROPERTIES = "ws.properties"

        @Suppress("UnusedMainParameter")
        @JvmStatic
        fun main(args: Array<String>) {
            Start().start()
        }
    }
}