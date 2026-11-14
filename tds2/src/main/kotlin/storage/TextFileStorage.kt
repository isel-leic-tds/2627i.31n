package pt.isel.tds2627.storage

import okio.*
import okio.Path.Companion.toPath
import pt.isel.tds2627.model.Serializer


class TextFileStorage<Key,Data>(
    baseFolderName: String,
    private val serializer: Serializer<Data>
) : Storage<Key,Data> {
    val fs = FileSystem.SYSTEM
    val basePath: Path = baseFolderName.toPath()


    private fun Path.exists() = fs.exists(this)
    private fun Path.delete() = fs.delete(this)
    private fun Path.createDirectory() = fs.createDirectories(this)
    private fun Path.isDirectory() = fs.metadata(this).isDirectory
    private fun Path.writeText(data: Data) {
        fs.write(this) { writeUtf8(serializer.serialize(data)) }
    }
    private fun Path.readText(): Data =
        fs.read(this) { serializer.deserialize(readUtf8()) }


    init { with(basePath) { // Create base folder if it does not exist
        if (!exists()) createDirectory()
        else check(isDirectory()) { "$name is not a directory" }
    } }


    // Apply a function to the path of the file for the given key.
    private inline fun <R> withPath(key: Key, fx: Path.() -> R): R =
        (basePath / "$key.txt").fx()


    override fun create(key: Key, data: Data) {
        val path = basePath / "$key.txt"
        check(!fs.exists(path)) { "File $key exists" }
        fs.write(path) {
            writeUtf8(serializer.serialize(data))
        }
    }

    override fun read(key: Key): Data? = withPath(key) {
        try { readText() }
        catch (_: IOException) { null }
    }
    override fun update(key: Key, data: Data) = withPath(key) {
        check(exists()) { "File $key does not exist" }
        writeText(data)
    }
    override fun delete(key: Key) = withPath(key) {
        check(exists()) { "File $key does not exist" }
        delete()
    }
}
