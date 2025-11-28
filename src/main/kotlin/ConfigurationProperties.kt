package eu.magicksource.kiniconfig

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EmptyConfiguration
import com.natpryce.konfig.Location
import java.net.URL
import java.util.Properties
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Create a configuration object by loading an INI file
 * @param filename: String - the path to the .ini file
 * @return A Configuration object
 */
fun ConfigurationProperties.Companion.fromIniConfig(filename: String): Configuration =
    KIniConfig.load(filename).toConfiguration("IniConfig: $filename")

/**
 * Create a configuration object by loading a .ini file that is a resource in the .jar of the project
 * @param resourceName: String - the name of the resource file
 * @return a Configuration object
 */
fun ConfigurationProperties.Companion.fromIniConfigResource(resourceName: String): Configuration {
    val classLoader = ClassLoader.getSystemClassLoader()
    return loadFromResource(resourceName, classLoader.getResource(resourceName))
}

private fun loadFromResource(resourceName: String, resourceUrl: URL?): Configuration =
    KIniConfig.load(resourceUrl?.openStream()).toConfiguration("resource $resourceName")


private fun KIniConfig?.toConfiguration(description: String): Configuration {
    val properties = this?.let {
        it.asMap()
            .flatMap { (section, entries) ->
                entries.map { (key,value) ->
                    "$section.$key" to value
                }
            }
    }
        ?.toMap()
        ?.let { Properties().apply { putAll(it) } }

    return properties?.let {
        ConfigurationProperties(it, Location(description))
    } ?: EmptyConfiguration;
}
