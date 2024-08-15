package com.gary.controllers

import com.gary.consumers.ScrapeLogic
import com.gary.dtos.PriceRecordDto
import com.gary.utils.dbQuery
import com.gary.plugins.PriceRecord
import com.gary.plugins.PriceRecords
import com.gary.plugins.Website
import com.gary.plugins.Websites
import io.github.serpro69.kfaker.Faker
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureInternalController() {
    routing {
        // Create city
        get("/seed") {
            val faker = Faker()

            val currentMoment = Clock.System.now()
            val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)

            for (i in 1..100) {
                val newWebsite = transaction() {

                    val newWebsite = Website.new {
                        name = faker.internet.domain()
                        url = "${faker.internet.domain()}/${faker.internet.slug()}"
                    }
                    newWebsite
                }

                for (i in 1..10) {

                    transaction() {
                        for (i in 1..1000) {

                            PriceRecord.new {
                                timeStamp = datetimeInUtc
                                price = faker.random.nextInt(100)
                                website = newWebsite

                            }
                        }
                    }
                }
            }
        }
    }
}