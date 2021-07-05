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

import net.dv8tion.jda.api.OnlineStatus

data class BotOnlineStatus(val botId: Long, var status: OnlineStatus) {
    private val isOnline: Boolean
        get() = status != OnlineStatus.OFFLINE

    override fun toString() = """{"bot_id": "$botId", "online_status": "$status", "is_online": $isOnline}"""
}
