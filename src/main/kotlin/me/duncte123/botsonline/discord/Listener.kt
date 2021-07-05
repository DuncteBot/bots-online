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

package me.duncte123.botsonline.discord

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.slf4j.LoggerFactory

class Listener : EventListener {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is UserUpdateOnlineStatusEvent -> {
                val botId = event.user.idLong

                if (!botsToCache.containsKey(botId)) {
                    return
                }

                // Update the status
                statusMap[botId].status =  event.newOnlineStatus
            }
            is GuildReadyEvent -> {
                val guild = event.guild
                val guildId = guild.idLong

                if (botsToCache.containsValue(guildId)) {
                    log.info("Loading members for $guild")
                    // cache the members
                    guild.loadMembers()
                        .onSuccess {
                        log.info("Loaded members for $guild")

                        it.filter { m -> botsToCache.containsKey(m.idLong) }
                            .forEach { m ->
                                statusMap[m.idLong].status =  m.onlineStatus
                        }
                    }
                    .onError {
                        log.error("Failed to load members for $guild", it)
                    }
                }
            }
        }
    }
}
