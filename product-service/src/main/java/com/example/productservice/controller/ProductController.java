package com.example.productservice.controller;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProducts();
    }
}

/*
Riassunto:
- metodo createProduct()
Classiche annotazioni, @ResponseBody converte in automatico il valore restituito in un formato appropriato.
Molto semplicemente, il metodo chiama il service con createProduct(productRequest) e gli passa
l' oggetto di tipo productRequest ricevuto in ingresso.
--------> Continua su productService

- metodo getAllProduct()
Chiama semplicemente il service con getAllProducts() e si fa restituire una lista di ProductResponse.
--------> Continua su productService
 */
