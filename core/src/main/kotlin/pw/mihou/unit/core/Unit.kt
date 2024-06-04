@file:Suppress("unused")

package pw.mihou.unit.core

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import pw.mihou.unit.coders.interfaces.*
import pw.mihou.unit.exceptions.PathAlreadyTakenException
import pw.mihou.unit.core.reactive.ActiveRecord
import java.io.File
import java.nio.charset.Charset
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.io.path.*
import kotlin.time.Duration.Companion.seconds

class Unit<T : Any>(
    private var address: String,
    private var charset: Charset = Charsets.UTF_8,
    private val encoder: Encoder,
    private val decoder: Decoder<T>,
    private val allowsAppend: Boolean
) {
    // Internal backing variables.
    private var ext: String
    private lateinit var _content: T

    private lateinit var nextRead: Instant

    private val lock = ReentrantReadWriteLock()

    // Initialization
    init {
        val normalizedAddress = Path(address).normalize()
        address = normalizedAddress.absolutePathString()
        ext = normalizedAddress.extension

        load()
    }

    /**
     * Sets the amount of time that the cache should be used. This means that up until the
     * specified duration has passed since the last read, the content resulted from the last read will be
     * used.
     *
     * You can set this to a negative or a zero millisecond value to skip caching.
     */
    var cacheTime = 10.seconds

    /**
     * Whether to overwrite the existing files when performing a move operation (such as a rename, a directory
     * change, or anything related to moving).
     */
    var overwriteExistingFilesOnMove = false

    /**
     * Whether to append a newline when using the [append] method.
     */
    var appendNewLineOnAppend = true

    /**
     * Gets or sets the content of the file. The content of the file is cached up to [cacheTime]
     * duration, which means that this may result in outdated content, unless you specify [cacheTime] to a
     * zero or negative value.
     *
     * Setting the content will immediately update the file's content, encoding the value when needed.
     */
    var content by ActiveRecord(
        get = get@{
            if (cacheTime.inWholeMilliseconds >= 0) {
                val now = Clock.System.now()
                if ((nextRead - now).inWholeMilliseconds > 0) {
                    return@get _content
                }
            }

            load()
            return@get _content
        },
        set = set@{
            write(it)
        }
    )

    /**
     * Gets or sets the name of the file.
     *
     * Setting a new file name will perform a move operation, and will throw a [PathAlreadyTakenException]
     * when the new file name is already used, and [overwriteExistingFilesOnMove] is disabled.
     */
    var name by ActiveRecord(
        get = get@{
            return@get File(address).nameWithoutExtension
        },
        set = set@{
            rename("$it.$ext")
        }
    )

    /**
     * Gets or sets the extension of the file.
     *
     * Setting a new file extension will perform a move operation, and will throw a [PathAlreadyTakenException]
     * when the new path is already used, and [overwriteExistingFilesOnMove] is disabled.
     */
    var extension by ActiveRecord(
        get = get@{
            return@get ext
        },
        set = set@{
            ext = it
            rename("$name.$it")
        }
    )

    /**
     * Gets or sets the directory of the file.
     *
     * Setting a new directory will perform a move operation, and will throw a [PathAlreadyTakenException]
     * when the new path is already used, and [overwriteExistingFilesOnMove] is disabled.
     *
     * This will also create the directories when the directories doesn't exist already.
     */
    var directory by ActiveRecord(
        get = get@{
            return@get Path(address).absolutePathString()
        },
        set = set@{
            changeDirectory(Path(it))
        }
    )

    /**
     * Gets or sets the full path of the file.
     *
     * Setting a new ptah will perform a move operation, and will throw a [PathAlreadyTakenException]
     * when the new path is already used, and [overwriteExistingFilesOnMove] is disabled.
     *
     * This will also create the directories when the directories don't exist already.
     */
    var path by ActiveRecord(
        get = get@{
            return@get Path(address)
        },
        set = set@{
            move(it)
        }
    )

    private fun createOrOpenFile(): File? {
        val file = File(address)
        if (!file.exists()) {
            val path = file.toPath()
            if (path.parent == null) {
                return file
            }

            File(path.parent.absolutePathString()).mkdirs()
        } else if (file.exists()) {
            if (file.isDirectory) {
                return null
            }
        }
        return file
    }

    /**
     * Creates the file with an empty content, this will also create
     * the parent directories when it doesn't exist yet.
     */
    fun create() {
        createOrOpenFile()
    }

    /**
     * Deletes the file, or folder.
     * When [recursive] is true, and the file is a directory, this will delete
     * all the files under the directory.
     *
     * @param recursive whether to delete recursively.
     */
    fun delete(recursive: Boolean = false) {
        val file = File(address)
        if (!file.exists()) {
            return
        }

        if (recursive && file.isDirectory) {
            file.deleteRecursively()
            return
        }

        file.delete()
    }

    /**
     * Forcibly skips the wait time till next re-read, and forcibly initiates
     * a read on the file. This is useful when you need the most up-to-date
     * content of the file.
     *
     * @return the latest content of the file.
     */
    fun latestContent(): T {
        load()
        return content
    }

    private fun write(content: T) {
        lock.write lock@{
            val file = createOrOpenFile() ?: return@lock
            val value = encoder.encode(content)
            file.writeText(value)
        }
        load()
    }

    /**
     * Appends the value to the file.
     * This may throw an [UnsupportedOperationException] when the file type doesn't support the append
     * operation, which more is based on the implementing encoder or decoder.
     *
     * @param value the value to append.
     */
    fun append(value: T) {
        if (!allowsAppend) {
            throw UnsupportedOperationException("This file type does not support the append operation.")
        }
        lock.write {
            val file = createOrOpenFile() ?: return@write
            val content = (if (appendNewLineOnAppend) "\n" else "") + encoder.encode(value)
            file.appendText(content, charset)
        }
        load()
    }

    private fun rename(name: String) {
        val path = Path(address)
        move(path.resolveSibling(name))
    }

    private fun changeDirectory(dir: Path) {
        if (!dir.exists()) {
            dir.createDirectories()
        } else {
            if (!dir.isDirectory()) {
                throw IllegalArgumentException("Path $dir is not a directory.")
            }
        }

        val path = dir.resolve("$name.$ext")
        move(path)
    }

    private fun move(dest: Path) {
        val file = File(address)
        if (!file.exists()) {
            throw IllegalStateException("File $address does not exist and cannot be renamed.")
        }
        val path = file.toPath()
        @Suppress("NAME_SHADOWING") val dest = dest.normalize()
        if (overwriteExistingFilesOnMove) {
            Files.move(path, dest, StandardCopyOption.REPLACE_EXISTING)
        } else {
            try {
                Files.move(path, dest)
            } catch (e: FileAlreadyExistsException) {
                throw PathAlreadyTakenException(dest.absolutePathString())
            }
        }
        address = dest.absolutePathString()
    }

    private fun load() = lock.read {
        val file = File(address)
        var value: T? = null
        if (file.exists() && !file.isDirectory) {
            when(decoder) {
                is BufferedReaderDecoder -> {
                    value = decoder.decode(file.bufferedReader(charset))
                }
                is LineDecoder -> {
                    val bufReader = file.bufferedReader(charset)
                    value =  decoder.decode { bufReader.readLine() }
                }
                is FileDecoder -> {
                    value = decoder.decode(file)
                }
            }
            if (value == null) {
                throw IllegalArgumentException("Cannot decode value of $address with decoder $decoder.")
            }
            _content = value
            nextRead = Clock.System.now() + cacheTime
        }
    }
}
