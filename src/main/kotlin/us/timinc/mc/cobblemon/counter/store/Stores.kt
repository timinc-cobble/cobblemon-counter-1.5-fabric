package us.timinc.mc.cobblemon.counter.store

import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry

object Stores {
    fun register() {
        PlayerDataExtensionRegistry.register(KoCount.NAME, KoCount::class.java)
        PlayerDataExtensionRegistry.register(KoStreak.NAME, KoStreak::class.java)
        PlayerDataExtensionRegistry.register(CaptureCount.NAME, CaptureCount::class.java)
        PlayerDataExtensionRegistry.register(CaptureStreak.NAME, CaptureStreak::class.java)
        PlayerDataExtensionRegistry.register(Encounter.NAME, Encounter::class.java)
    }
}