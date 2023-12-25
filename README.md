# Kotlin Cacheable

Kotlin Cacheable is an annotation-based caching library for Kotlin Multiplatform.

## Setup

This library is published to Maven Central.
Add the Maven Central repository to your project's `pluginManagement` and
`dependencyResolutionManagement` sections.

```kotlin
// setting.gradle.kts
pluginManagement {
    repositories {
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenCentral()
    }
}
```

Enable the Gradle plugin for the module where you want to use this library.

```kotlin
// build.gradle.kts
plugins {
    kotlin(/* ... */)
    id("com.moriatsushi.cacheable") version "0.0.3"
}
```

You also need to add the `cacheable-core` dependency to the module.

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.moriatsushi.cacheable:cacheable-core:0.0.3")
}
```

## Usage

You can use `@Cacheable` annotation to cache the result of specified functions.
When you call a function with this annotation, it will return the cached value if the function is
called with the same arguments.

Here is an example:

```kotlin
@Cacheable
fun getSomething(key: String): Something {
    /* ... */
}
```

This is equivalent to the following code:

```kotlin
private var cache = mutableMapOf<String, Something>()

fun getSomething(key: String): Something =
    cache.getOrPut(key) { /* ... */ }
```

If you use the cacheable function within a class, the cache is only shared within an instance of the
class.

In addition, if you specify `maxCount` parameter, the number of cached values will be limited to the
specified value.
If the number of cached values exceeds the specified value, the cache with the oldest access time
will be removed.

```kotlin
class SomeClass {
    @Cacheable(maxCount = 10)
    fun getSomething(key: String): Something {
        // ...
    }
}
```

You can also use `@Cacheable` annotation for a suspend function.

```kotlin
class Repository(private val api: Api) {
    @Cacheable(maxCount = 10)
    suspend fun getUser(id: String): User =
        api.getUser(id)
}
```
