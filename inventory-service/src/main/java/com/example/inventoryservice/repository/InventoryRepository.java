package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}

/*
Riassunto: classico Repository JPA.
1) Chiamo il db e mi faccio restituire, in base alla lista di stringhe che passo al metodo
findBySkuCodeIn, una lista di oggetti di tipo Inventory; importante notare che nel repository andrò
a manipolare l' entità stessa.
 */
