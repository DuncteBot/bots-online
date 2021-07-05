/*
 *     Simple bot to check if my other bots are online
 *     Copyright (C) 2021 Duncan "duncte123" Sterken
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.duncte123.botsonline.web

import io.ktor.application.call
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import me.duncte123.botsonline.discord.statusMap

fun startServer() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        routing {
            get("/") {
                call.respondText("Hello world", status = HttpStatusCode.OK)
            }

            get("/overview") {
                val items = statusMap.values().joinToString(",")

                call.respondText(
                    """[$items]""",
                    contentType = ContentType.parse("application/json"),
                    status = HttpStatusCode.OK
                )
            }

            get("/online/{bot_id}") {
                try {
                    val botId = call.parameters["bot_id"]!!.toLong()
                    val status = statusMap[botId]

                    if (status == null) {
                        call.respondText(
                            """{"error": "Bot not found"}""",
                            contentType = ContentType.parse("application/json"),
                            status = HttpStatusCode.NotFound
                        )
                        return@get
                    }

                    call.respondText(
                        status.toString(),
                        contentType = ContentType.parse("application/json"),
                        status = HttpStatusCode.OK
                    )
                } catch (e: NumberFormatException) {
                    call.respondText(
                        """{"error": "\"bot_id\" parameter is not a valid long"}""",
                        contentType = ContentType.parse("application/json"),
                        status = HttpStatusCode.BadRequest
                    )
                }
            }
        }
    }.start(wait = true)
}
