package org.hupisoft.battleships;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GameDataStorageProviderTest {

    @Test
    public void getStorageReturnsActualStorageInstance() {
        IGameDataStorage storage = GameDataStorageProvider.getStorage();
        assertNotNull(storage);
        GameDataStorage converted = (GameDataStorage)storage;
        assertNotNull(converted);
        IGameDataStorage storage2 = GameDataStorageProvider.getStorage();
        assertEquals(storage, storage2);
    }

    @Test
    public void setStorageSetsNewStorageInstance() {
        IGameDataStorage mockStorage = mock(IGameDataStorage.class);
        GameDataStorageProvider.setStorage(mockStorage);
        IGameDataStorage storage = GameDataStorageProvider.getStorage();
        assertEquals(mockStorage, storage);

        GameDataStorageProvider.reset();
        IGameDataStorage storage2 = GameDataStorageProvider.getStorage();
        assertNotEquals(storage, storage2);
        GameDataStorage converted = (GameDataStorage)storage2;
        assertNotNull(converted);
    }
}