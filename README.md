# KIniConfig

KIniConfig is a kotlin library to load .ini files with a few small
extensions to the basic .ini format.

```kotlin
val config = KIniConfig.load("/path/to/file.ini")
val dbname = config.get("database","dbname") ?: "default_db"
```

### TL;DR

.ini file load in Kotlin

Source Code: [GitHub](https://github.com/magick-source/kiniconfig)

## Companion Methods

### KIniConfig.load

```kotlin
fun load(filename: String): KIniConfig?
```

Loads a config file, parses it and returns the respective KIniConfig
object.

If the file does not exist, returns null.

Usage:
```kotlin
val config = KIniConfig.load(filename)
```

Alternatively, instead of a filename, you can load the configuration
from any `InputStream`:

```kotlin
fun load(stream: InputStream?): KIniConfig?
```

loads the configuration from an InputStream.

Usage:
```kotlin
val file = File(filename)
if (file.exists()) {
  val config = KIniConfig.load(file.inputStream())
}
```

### KIniConfig.loadMapped

```kotlin
fun loadMapped(filename: String, clazz: KClass<T>): T?
```

Tries to loads a config file, and if successful, calls
[mappedTo()]({{<relref "#mappedto">}}) in the loaded object.

```kotlin
data class Config(val path:String, val maxSize: Int);

val config = KIniConfig.loadMapped(configFilename, Config::class)

```

## KIniConfig Methods

Consider the following content of `config.ini`:

```ini
key1 = value1
key2 = value2

[section1]
key1 = section1value1
key2 = section1value2
```
And the code to load it:

```kotlin
val config = KIniConfig.load("config.ini")
```

### getValue

```kotlin
fun getValue(key: String): String?
```

returns a value from the root section of the configuration. If the
value is not present, returns null. The root section of the
configuration includes the properties that are defined before a
section starts.

```kotlin
config.getValue("key1") // value1
```

On the other hand, you can also get configurations from inside a
section direction, but calling `getValue` with two parameters:

```kotlin
fun getValue(section:String, key: String): String?
```

returns a value from a configuration inside of a section. If either
the section or the key are missing, returns null.


```kotlin
config.getValue("section1", "key1") // section1value1
```

### getSection

```kotlin
fun getSection(section: String): KIniConfig?
```

Returns a KIniConfig object with the keys of the section passed as
keys for the root section. Returns null if the section is not present.
This can be used to pass a subset of the configurations to code that
expects its own configurations only.

```kotlin

val section1config = config.getSection('section1')
section1config.getValue('key1') // section1key1
```

### asMap

```kotlin
fun asMap(): Map<String, Map<String, String>>
```

Returns all the configurations as a Map of Maps. The first level key
is the section name, while the second level key is the configuration
name.

```kotlin
config.asMap()
/*
{ "": {"key1": "value1", "key2": "value2"},
  "section1": {"key1": "section1value1", "key2": "section1value2"}
}
*/
```

### mappedTo

```kotlin
fun mappedTo(clazz: KClass<T>): T?
```

Map a KIniConfig object to an object of the provided class.

## ConfigurationProperties

`KIniConfig` provides two extension static method for
`ConfigurationProperties` that allow loading ConfigurationProperties
from .ini files.

```kotlin
val config: Configuration = systemProperties overriding
    ConfigurationProperties.fromIniConfig("/etc/kiniconfig.ini") overriding
    ConfigurationProperties.fromIniConfigResource("kiniconfig.ini")
```

### fromIniConfig

```kotlin
fun fromIniConfig(filename: String): Configuration
```

Loads a .ini file and if the .ini file is not found, then a
EmptyConfiguration is returned.

### fromIniConfigResource

```kotlin
fun fromIniConfigResource(resourceName: String): Configuration
```

Loads an .ini file from the resources of the packages loaded, and
returns the ConfigurationProperties for them. If the resource is not
found, then an EmptyConfiguration is returned.


## Bug Reports and Feature Requests

Please report any bugs or request features in:

- github: https://github.com/magick-source/kiniconfig
