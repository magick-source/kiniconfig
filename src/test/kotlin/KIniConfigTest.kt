package eu.magicksource.kiniconfig

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class KIniConfigTest {
    @Test
    fun getValue() {
        val source = someConfig()
        val iniConfig = KIniConfig(source)
        assertNotNull(iniConfig)

        assertEquals(source[""]?.get("key1"), iniConfig.getValue("key1"))
        assertEquals(null, iniConfig.getValue("key3"))

        assertEquals(source["section1"]?.get("key4"), iniConfig.getValue("section1","key4"))
    }

    @Test
    fun getSection() {
        val source = someConfig()
        val iniConfig = KIniConfig(source)
        assertNotNull(iniConfig)

        assertEquals(mapOf("" to source["section1"]), iniConfig.getSection("section1")?.asMap())
    }

    @Test
    fun asMap() {
        val source = someConfig()
        val iniConfig = KIniConfig(source)

        assertEquals(source, iniConfig.asMap())
    }

    @Test
    fun mappedTo() {
        val source = someConfig()
        val iniConfig = KIniConfig(source)
        assertNotNull(iniConfig)

        val config = iniConfig.mappedTo(ConfigMapped::class)
        assertNotNull(config)

        assertEquals(source[""]?.get("key1"), config.key1)
        assertEquals(source["section1"], config.section1)
    }

    fun someConfig(): Map<String, Map<String,String>> = mapOf(
        "" to mapOf(
            "key1" to "value1",
            "key2" to "value2",
        ),
        "section1" to mapOf(
            "key3" to "value3",
            "key4" to "value4",
        )
    )

    class ConfigMapped(val key1: String,val key2: String, val section1: Map<String,String>)
}