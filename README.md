# Unit

*Simplified file operations for Kotlin.* Unit Kotlin is a library inspired by its sister Golang library,
[`siopao`](https://github.com/ShindouMihou/siopao) and [`Reakt.Discord`](https://github.com/ShindouMihou/reakt.discord), 
that aims to implement simpler and developer-friendly abstractions for simple file operations.

# demo
```kotlin
@Serializable
data class Hello(name: String)
```
```kotlin
// Automatically appends `.json` and related extensions (will respect if you put `.json`, etc. extension, but currently won't support other extensions)
val json = "example".json<Hello>() // pw.mihou.unit.json dependency needed
val yaml = "example".yaml<Hello>() // pw.mihou.unit.yaml dependency needed
val toml = "example".toml<Hello>() // pw.mihou.unit.toml dependency needed
val text = "example".txt

// Uses the entire provided path as address, this doesn't append any extension or anything.
val unit = "example.txt".unit
```

```kotlin
// Writes, or overwrites the file with the new content (this will stringify the value automatically).
json.content = Hello("galaxy")
yaml.content = Hello("galaxy")
toml.content = Hello("galaxy")
text.content = "Hello galaxy"

// content is cached up to a specified cache time (10 seconds, by default) unless the 
// cache time is set to a negative or zero value.
println(text.content)

// You can set the cache time by setting the `cacheTime` variable on the instance (uses kotlinx.datetime).
text.cacheTime = 0.milliseconds

// Getting the value of the content will also automatically decode the content of the file using the decoder.
// So it will automatically try to decode the JSON file to the type.
println(json.content)

// Appends to the file, currently only text files supports this operation.
// This will add a newline unless you have `appendNewLineOnAppend` disabled on the instance.
text.append("Hello world")

// Overwrites files during a move operation (rename, new directory, etc.) when it already exists.
text.overwriteExistingFilesOnMove = true

// Renames the file to a new name.
text.name = "hello"

// Changes the extension of the file. This doesn't change the encoder, or decoder.
text.extension = "md"

// Changes the  directory, this will create the directories if it doesn't exist already.
text.directory = "tests/"
```

# installation
To install `unit`, simply follow the steps below:

**1. Add the Jitpack Repository**

Follow the one for your build tool, if you are using Gradle, then follow the top example otherwise for Maven users,
follow the bottom example.

```groovy
repositories {
  maven { url 'https://jitpack.io' }
}
```
```maven
<repositories>
	<repository>
	  <id>jitpack.io</id>
	  <url>https://jitpack.io</url>
	</repository>
</repositories>
```

**2. Install the core library**

To get the latest release, simply head to [`Jitpack Releases`](https://jitpack.io/#pw.mihou/unit) and
select the `Releases` tab. You can then copy the latest `Version` with the `Get it` green button.

```groovy
dependencies {
    implementation 'pw.mihou.unit:core:<version>'
}
```

```xml
<dependency>
  <groupId>pw.mihou.unit</groupId>
  <artifactId>unit</artifactId>
  <version>Tag</version>
</dependency>
```

**3. Start using the library.**

You can now use the library after installing and reloading your build tool. A quick example can be seen from the
[`Demo`](#demo) section which shows some really simple examples.

**4. (Optional) Add other file types.**

You can also add other file types by adding their respective dependencies:
- **YAML**: `pw.mihou.unit.yaml`
- **JSON**: `pw.mihou.unit.json`
- **TOML**: `pw.mihou.unit.toml`
- **ENV**: `pw.mihou.unit.env`

You can also create your own encoder or decoder, we recommend taking a look into our own coders as a reference 
as to how to create your own!

## License

As part of Qucy Studios and Shindou Mihou's Open-Source Libraries, 
Unit will be MIT-licensed permanently and irrevocably, which means that this license shall not change. You and enterprises are free to use, redistribute, and modify the code for any purposes, although we hold no responsibility for any harm conducted by the use of the library.