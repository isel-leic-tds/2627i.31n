package pt.isel.tds2627
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath


class FileStorage (
    private val serializer: GameSerializer
) : IStorage {

    override fun create(key: String, data: Game) {
        val path : Path = key.toPath()

        with (path) {
            FileSystem.SYSTEM.write(path,false) {
                writeUtf8(GameSerializer.serialize(data) )
            }
        }
    }

    override fun read(key: String): Game? {
        val path = key.toPath()
        val bytes: ByteArray = FileSystem.SYSTEM.read(path) {
            readByteArray()
        }
        val str = String(bytes, Charsets.UTF_8)


        return GameSerializer.deserialize(str)

    }

    override fun update(key: String, data: Game) {
        TODO("Not yet implemented")
    }

    override fun delete(key: String) {
        TODO("Not yet implemented")
    }


}
