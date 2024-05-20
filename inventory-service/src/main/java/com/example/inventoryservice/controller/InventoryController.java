package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
    /*
    Riassunto: tramite una richiesta GET all'endpoint ".../api/inventory, vado a controllare se
    un oggetto sia o meno nell' inventario:
    1) Richiesto in ingresso una lista di stringhe, contentente gli skuCode
    2) Chiamo il service, il particolare il metodo isInStock(skuCode) che ritorna una lista di
    InventoryResponse
    ----------> Parte 2 in InvetoryService
     */
}
