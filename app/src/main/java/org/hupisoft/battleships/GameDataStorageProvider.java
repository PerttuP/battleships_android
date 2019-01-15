package org.hupisoft.battleships;

/**
 * Provider class for IGameDataStorage.
 */
class GameDataStorageProvider {

    static private IGameDataStorage mStorage = new GameDataStorage();

    /**
     * Get game data storage instance.
     * @return Game data storage.
     */
    static IGameDataStorage getStorage() {
        return mStorage;
    }

    /**
     * Set game data storage instance. THIS IS FOR TESTING PURPOSES ONLY!
     * @param storage Mock data storage.
     */
    static void setStorage(IGameDataStorage storage) {
        mStorage = storage;
    }

    /**
     * Inverts effects of setStorage(). THIS IS FOR TESTING PURPOSES ONLY!
     */
    static void reset() {
        mStorage = new GameDataStorage();
    }
}
