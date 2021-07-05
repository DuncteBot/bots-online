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

import gnu.trove.impl.sync.TSynchronizedLongObjectMap
import gnu.trove.map.TLongObjectMap
import gnu.trove.map.hash.TLongObjectHashMap
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

lateinit var jda: JDA
val botsToCache = mapOf(
    // bot_id to guild_id
    210363111729790977L to 191245668617158656L,
    397297702150602752L to 580687843232317440L
)
val statusMap: TLongObjectMap<BotOnlineStatus> = TSynchronizedLongObjectMap(TLongObjectHashMap(), Object())

fun startBot() {
    botsToCache.keys.forEach {
        statusMap.put(it, BotOnlineStatus(it, OnlineStatus.OFFLINE))
    }

    jda = JDABuilder.createLight(
        System.getenv("JDA_TOKEN"),
        GatewayIntent.GUILD_MEMBERS,
        GatewayIntent.GUILD_PRESENCES
    )
        .enableCache(
            CacheFlag.ONLINE_STATUS
        )
        .addEventListeners(Listener())
        .setActivity(Activity.watching("Duncte's bots"))
        .setMemberCachePolicy { botsToCache.containsKey(it.idLong) }
        .build()
}
