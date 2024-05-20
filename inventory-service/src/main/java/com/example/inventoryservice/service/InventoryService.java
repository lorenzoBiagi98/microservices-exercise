package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Wait started");
        Thread.sleep(1000); //Ferma il funzionamento per 1 secondo
        log.info("Wait ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).toList();
    }

    /*
    Riassunto: utilizzo l' annotazione @Transactional per far si che questa transazione venga eseguita
    "per tutto" o "per niente";
    1) Ll metodo richiede in ingresso una lista di stringhe che contiene gli skuCod
    2) Pause di un secondo data da Thread.sleep(1000)
    3) Chiamo il repository, con il metodo findBySkyCodeIn(skuCode), che mi restituirà, in base alla
    lista che gli passo, una lista di oggetti Inventory.
    4) Costruisco una lista di oggetti di tipo InvenoryResponse(DTO), andando ad utilizzare i valori
    degli oggetti di tipo Inventory restituiti dal repository
        ----------> Parte 3 in InventoryRepository
     */
}
