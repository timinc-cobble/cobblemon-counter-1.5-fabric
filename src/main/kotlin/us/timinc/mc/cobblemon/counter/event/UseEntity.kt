package us.timinc.mc.cobblemon.counter.event

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import us.timinc.mc.cobblemon.counter.Counter.config
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.api.EncounterApi
import java.util.*

object UseEntity {
    fun handle(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        if (entity !is PokemonEntity) return ActionResult.PASS
        if (!entity.pokemon.isWild()) {
            return ActionResult.PASS
        }
        if (!player.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty) {
            return ActionResult.PASS
        }
        if (!world.isClient) {
            val species = entity.pokemon.species.name.lowercase(Locale.getDefault())
            val alreadyEncountered = EncounterApi.check(player, species)
            if (alreadyEncountered) {
                if (config.broadcastEncountersToPlayer) {
                    val alreadyCaught = CaptureApi.getCount(player, species) > 0
                    player.sendMessage(
                        Text.translatable(
                            if (alreadyCaught) "counter.encounter.alreadyCaught" else "counter.encounter.alreadyEncountered",
                            species
                        )
                    )
                }
            } else {
                EncounterApi.add(player, species)
            }
        }
        return ActionResult.SUCCESS
    }
}