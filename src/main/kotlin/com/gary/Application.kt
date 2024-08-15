package com.gary

import com.gary.consumers.ScrapeLogic
import com.gary.consumers.ScrapeLogicImpl
import com.gary.consumers.configureScapeConsumer
import com.gary.controllers.configureInternalController
import com.gary.controllers.configurePriceController
import com.gary.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    EngineMain.main(args)
}

val appModule = module {
    singleOf(::ScrapeLogicImpl) { bind<ScrapeLogic>() }

}

fun Application.module() {

//    val openTelemetry: OpenTelemetry = GlobalOpenTelemetry.get()
//
//    install(KtorServerTracing){
//        setOpenTelemetry(openTelemetry)
//    }

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    val scrapeLogic by inject<ScrapeLogic>()

    configureRabbitMQ()
    configureScapeConsumer(scrapeLogic)
    configureSerialization()
    configureData()
    configureRouting()
    configurePriceController(scrapeLogic)
    configureInternalController()
}