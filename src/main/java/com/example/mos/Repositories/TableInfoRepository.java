package com.example.mos.Repositories;

import com.example.mos.Models.Store;
import com.example.mos.Models.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableInfoRepository extends JpaRepository<TableInfo, Long> {
    List<TableInfo> findByStore(Store store);
    List<TableInfo> findByStoreAndFloor(Store store, Integer floor);
    List<TableInfo> findByStoreAndStatus(Store store, TableInfo.TableStatus status);
    Optional<TableInfo> findByQrCode(String qrCode);
    Optional<TableInfo> findByStoreAndTableNumber(Store store, String tableNumber);
}
