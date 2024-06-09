package us.timinc.mc.cobblemon.counter.config

import com.google.gson.GsonBuilder
import us.timinc.mc.cobblemon.counter.Counter
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class CounterConfig {
    val debug = false
    val broadcastKosToPlayer = true
    val broadcastCapturesToPlayer = true
    val broadcastEncountersToPlayer = true
}