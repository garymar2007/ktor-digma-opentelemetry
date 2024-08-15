package com.gary.domain

import io.opentelemetry.api.trace.Span
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div
import it.skrape.selects.html5.span
import io.opentelemetry.instrumentation.annotations.WithSpan

@WithSpan
suspend fun scrapeEbayPrice(urlToScrap: String): Int {

    var price = skrape(HttpFetcher){

        request {
            this.url = urlToScrap
        }

        /**
         * In order to get the price from the html document, we need to find the following structure:
         * <div class="x-price-primary" data-testid="x-price-primary">
         *     <span class="ux-textspans">US $44.95/ea</span>
         * </div>
         */
        response {
            htmlDocument {
                div {
                    withClass = "x-price-primary"
                    findFirst {
                        span {
                            withClass = "ux-textspans"
                            findFirst {
                                text
                            }
                        }
                    }
                }
            }
        }

    }
    Span.current().setAttribute("price",price)

    return ridiculouslySimplePriceExtractor(price)

}