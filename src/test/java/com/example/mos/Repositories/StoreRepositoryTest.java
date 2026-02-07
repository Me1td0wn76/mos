package com.example.mos.Repositories;

import com.example.mos.Models.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StoreRepository の統合テスト
 */
@DataJpaTest
@ActiveProfiles("test")
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("店舗の保存と取得")
    void testSaveAndFind() {
        // Arrange
        Store store = new Store();
        store.setStoreName("テスト店舗");
        store.setAddress("東京都渋谷区");
        store.setPhoneNumber("03-1234-5678");
        store.setIsActive(true);

        // Act
        Store savedStore = storeRepository.save(store);
        Optional<Store> foundStore = storeRepository.findById(savedStore.getStoreId());

        // Assert
        assertTrue(foundStore.isPresent());
        assertEquals("テスト店舗", foundStore.get().getStoreName());
        assertEquals("東京都渋谷区", foundStore.get().getAddress());
        assertEquals(true, foundStore.get().getIsActive());
    }

    @Test
    @DisplayName("アクティブな店舗のみ取得")
    void testFindByIsActive() {
        // Arrange
        Store activeStore = new Store();
        activeStore.setStoreName("アクティブ店舗");
        activeStore.setIsActive(true);
        storeRepository.save(activeStore);

        Store inactiveStore = new Store();
        inactiveStore.setStoreName("非アクティブ店舗");
        inactiveStore.setIsActive(false);
        storeRepository.save(inactiveStore);

        // Act
        List<Store> activeStores = storeRepository.findByIsActive(true);

        // Assert
        assertFalse(activeStores.isEmpty());
        assertTrue(activeStores.stream().allMatch(Store::getIsActive));
    }

    @Test
    @DisplayName("店舗の更新")
    void testUpdate() {
        // Arrange
        Store store = new Store();
        store.setStoreName("元の店舗名");
        store.setIsActive(true);
        Store savedStore = storeRepository.save(store);

        // Act
        savedStore.setStoreName("更新後の店舗名");
        savedStore.setPhoneNumber("03-9999-9999");
        Store updatedStore = storeRepository.save(savedStore);

        // Assert
        Optional<Store> foundStore = storeRepository.findById(updatedStore.getStoreId());
        assertTrue(foundStore.isPresent());
        assertEquals("更新後の店舗名", foundStore.get().getStoreName());
        assertEquals("03-9999-9999", foundStore.get().getPhoneNumber());
    }

    @Test
    @DisplayName("店舗の削除")
    void testDelete() {
        // Arrange
        Store store = new Store();
        store.setStoreName("削除する店舗");
        store.setIsActive(true);
        Store savedStore = storeRepository.save(store);
        Long storeId = savedStore.getStoreId();

        // Act
        storeRepository.delete(savedStore);

        // Assert
        Optional<Store> foundStore = storeRepository.findById(storeId);
        assertFalse(foundStore.isPresent());
    }
}
