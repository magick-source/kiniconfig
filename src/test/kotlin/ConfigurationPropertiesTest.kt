package eu.magicksource.kiniconfig

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EmptyConfiguration
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ConfigurationPropertiesTest {

    @Test
    fun loadMissingResourceTest() {
        val config = ConfigurationProperties.fromIniConfigResource("missing-file.ini")
        assertEquals(EmptyConfiguration, config)
    }

    @Test
    fun loadResourceTest() {
        val config = ConfigurationProperties.fromIniConfigResource("testfile.ini")

        val configvalue:String = config.get(Key("section1.key1", stringType))
        assertEquals("section1value1", configvalue)

        val weirdkey = config.get(Key("section1.[key3]", stringType))
        assertEquals("section1value3", weirdkey)
    }
}