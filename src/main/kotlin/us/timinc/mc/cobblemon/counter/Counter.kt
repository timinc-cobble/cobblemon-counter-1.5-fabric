package us.timinc.mc.cobblemon.counter

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import us.timinc.mc.cobblemon.counter.command.Commands
import us.timinc.mc.cobblemon.counter.config.ConfigBuilder
import us.timinc.mc.cobblemon.counter.config.CounterConfig
import us.timinc.mc.cobblemon.counter.event.Events
import us.timinc.mc.cobblemon.counter.store.Stores

object Counter : ModInitializer {
    @Suppress("unused", "MemberVisibilityCanBePrivate")
    const val MOD_ID = "cobbled_counter"

    private var logger: Logger = LogManager.getLogger(MOD_ID)
    var config: CounterConfig = ConfigBuilder.load(CounterConfig::class.java, MOD_ID)

    override fun onInitialize() {
        Stores.register()
        Events.register()
        CommandRegistrationCallback.EVENT.register(Commands::register)
    }

    fun info(msg: String) {
        if (!config.debug) return
        logger.info(msg)
    }
}