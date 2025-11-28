package eu.magicksource.kiniconfig

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.InputStream
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.reflect.KClass

/**
 * Class to load and access configurations from .ini files
 */
class KIniConfig(private val config: Map<String, Map<String,String>>) {

    /**
     * Get a value for a configuration in the root section of the configuration
     * @param key: String - the key of the configuration value to get
     * @return String - the value of the setting - null if missing
     */
    fun getValue(key: String) = config[""]?.getOrDefault(key, null)

    /**
     * Get a value for a configuration from a specified section of the configuration
     * @param section: String - the section of the configuration to get the value from
     * @param key: String - the key to get the value from
     * @return String - the value of the settings - null if missing
     */
    fun getValue(section: String, key: String) = config[section]?.getOrDefault(key, null)

    /**
     * Get a KIniConfig object containing only a subsection of the configuration
     * @param section: String - the name of the section to return - "" for the root section
     * @return KIniConfig - a KIniConfig that only contains the settings of the section requests
     */
    fun getSection(section: String) = config[section]?.let { KIniConfig(mapOf("" to it)) }

    /**
     * Returns the whole configuration as a map
     * @return Map<String, Map<String, String>> - the first level is the section,
     *      the second level the configuration key and value
     */
    fun asMap(): Map<String, Map<String, String>> = config

    /**
     * map a KIniConfig object to a more specific class
     * @param <T> - the class of the object to be returned
     * @param clazz - the class to map the configuration to
     * @return T -  an object of the passed class
     */
    @Suppress("OutdatedDocumentation")
    fun <T: Any> mappedTo(clazz: KClass<T>): T {
        val cfg: MutableMap<String, Any> = mutableMapOf()
        cfg += config.filterNot { (k, _) -> k == "" }
        config.getOrDefault("", null)?.let {
            cfg += it
        }

        return objectMapper.convertValue(cfg, clazz.java)
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy())

        /**
         * Load a Ini configuration file
         * @param filename: String - the path to the .ini file to load
         * @return KIniConfig - a KIniConfig object
         */
        fun load(filename: String): KIniConfig? {
            val file = File(filename)
            if (!file.exists()) {
                return null
            }
            return load(file.inputStream())
        }

        /**
         * Load an ini file from an InputStream
         * @param stream: InputStream - the input stream to load the configuration from
         * @return KIniConfig - the configuration loaded
         */
        fun load(stream: InputStream?): KIniConfig? {
            if (stream == null) {
                return null
            }
            val ini = mutableMapOf<String, MutableMap<String, String>>()
            var currentSection = ""

            stream.bufferedReader().forEachLine { line ->
                val trimmed = line.trim().substringBefore(" #")
                when {
                    trimmed.startsWith('#') -> {}
                    trimmed.startsWith(';') -> {}
                    trimmed.isEmpty() -> {}
                    trimmed.startsWith('[') && trimmed.endsWith(']')-> {
                        currentSection = trimmed.substring(1, trimmed.length - 1)
                        ini[currentSection] = mutableMapOf()
                    }

                    else -> {
                        val (key, value) = trimmed.split("=", limit = 2).map { it.trim() }
                        ini.getOrPut(currentSection) { mutableMapOf() }[key] = value
                    }
                }

            }
            return KIniConfig(ini.toMap())
        }

        /**
         * load an .ini file and map it into an object of the class passed.
         * @param filename: String - the path of the .ini file to load
         * @param clazz: KClass<T> - the class of the object to return
         * @return T - an object of the class passed
         */
        @Suppress("OutdatedDocumentation") //I'm not sure how to document generics like this
        fun <T: Any> loadMapped(filename: String, clazz: KClass<T>): T? =
            load(filename)?.mappedTo(clazz)

    }
}