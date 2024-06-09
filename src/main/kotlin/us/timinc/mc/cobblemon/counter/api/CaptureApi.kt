package us.timinc.mc.cobblemon.counter.api

import com.cobblemon.mod.common.Cobblemon
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.counter.Counter.config
import us.timinc.mc.cobblemon.counter.Counter.info
import us.timinc.mc.cobblemon.counter.store.CaptureCount
import us.timinc.mc.cobblemon.counter.store.CaptureStreak

object CaptureApi {
    fun add(player: PlayerEntity, species: String) {
        val data = Cobblemon.playerData.get(player)

        val captureCount: CaptureCount = data.extraData.getOrPut(CaptureCount.NAME) { CaptureCount() } as CaptureCount
        val captureStreak: CaptureStreak =
            data.extraData.getOrPut(CaptureStreak.NAME) { CaptureStreak() } as CaptureStreak

        captureCount.add(species)
        captureStreak.add(species)

        val newCount = captureCount.get(species)
        val newStreak = captureStreak.get(species)

        info(
            "Player ${player.displayName.string} captured a $species streak($newStreak) count($newCount)"
        )
        if (config.broadcastCapturesToPlayer) {
            player.sendMessage(
                Text.translatable(
                    "counter.capture.confirm", species, newCount, newStreak
                )
            )
        }

        Cobblemon.playerData.saveSingle(data)
    }

    fun getTotal(player: PlayerEntity): Int {
        val playerData = Cobblemon.playerData.get(player)
        return (playerData.extraData.getOrPut(CaptureCount.NAME) { CaptureCount() } as CaptureCount).total()
    }

    fun getCount(player: PlayerEntity, species: String): Int {
        val playerData = Cobblemon.playerData.get(player)
        return (playerData.extraData.getOrPut(CaptureCount.NAME) { CaptureCount() } as CaptureCount).get(species)
    }

    fun getStreak(player: PlayerEntity): Pair<String, Int> {
        val playerData = Cobblemon.playerData.get(player)
        val captureStreakData = (playerData.extraData.getOrPut(CaptureStreak.NAME) { CaptureStreak() } as CaptureStreak)
        return Pair(captureStreakData.species, captureStreakData.count)
    }

    fun resetCount(player: PlayerEntity) {
        val playerData = Cobblemon.playerData.get(player)
        val captureCountData = (playerData.extraData.getOrPut(CaptureCount.NAME) { CaptureCount() } as CaptureCount)
        captureCountData.reset()
    }

    fun resetStreak(player: PlayerEntity) {
        val playerData = Cobblemon.playerData.get(player)
        val captureStreakData = (playerData.extraData.getOrPut(CaptureStreak.NAME) { CaptureStreak() } as CaptureStreak)
        captureStreakData.reset()
    }

    fun reset(player: PlayerEntity) {
        resetCount(player)
        resetStreak(player)
    }
}