package us.timinc.mc.cobblemon.counter.store

import com.cobblemon.mod.common.api.storage.player.PlayerDataExtension
import com.google.gson.JsonObject

class Encounter : PlayerDataExtension {
    companion object {
        const val NAME = "encounter"
    }

    private val encounters = mutableMapOf<String, Boolean>()

    fun reset() {
        encounters.clear()
    }

    fun add(speciesName: String) {
        encounters[speciesName] = true
    }

    fun get(speciesName: String): Boolean {
        return encounters.getOrDefault(speciesName, false)
    }

    fun total(): Int {
        return encounters.values.filter { it }.size
    }

    override fun deserialize(json: JsonObject): Encounter {
        val encounterData = json.getAsJsonObject("encounters")
        for (speciesName in encounterData.keySet()) {
            encounters[speciesName] = encounterData.get(speciesName).asBoolean
        }

        return this
    }

    override fun name(): String {
        return NAME
    }

    override fun serialize(): JsonObject {
        val json = JsonObject()
        json.addProperty("name", NAME)

        val encounterData = JsonObject()
        for (speciesName in encounters.keys) {
            encounterData.addProperty(speciesName, encounters[speciesName])
        }
        json.add("encounters", encounterData)

        return json
    }
}