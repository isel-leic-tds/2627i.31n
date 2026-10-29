package pt.isel.tds2627

interface IStorage {
    /**
     * Creates a new entry in the storage.
     * @param key the key of the entry.
     * @param data the data to store.
     * @throws IllegalStateException if the key already exists.
     */
    fun create(key: String, data: Game)
    /**
     * Reads an entry from the storage.
     * @param key the key of the entry.
     * @return the data stored or null if the key does not exist.
     */
    fun read(key: String): Game?
    /**
     * Updates an entry in the storage.
     * @param key the key of the entry.
     * @param data the new data to store.
     * @throws IllegalStateException if the key does not exist.
     */
    fun update(key: String, data: Game)
    /**
     * Deletes an entry from the storage.
     * @param key the key of the entry.
     * @throws IllegalStateException if the key does not exist.
     */
    fun delete(key: String)
}
