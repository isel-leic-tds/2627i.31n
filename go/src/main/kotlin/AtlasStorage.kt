package pt.isel.tds2627

import okio.use




class AtlasStorage : IStorage {
    override fun create(key: String, data: Game) {

        MongoDriver(nameDb = "test").use {
            val docs = it.getCollection<Game>("game")
            docs.apply {
                check(insertDocument(data))
            }
        }
    }

    override fun read(key: String): Game? {
        TODO("Not yet implemented")
    }

    override fun update(key: String, data: Game) {
        TODO("Not yet implemented")
    }

    override fun delete(key: String) {
        TODO("Not yet implemented")
    }


}