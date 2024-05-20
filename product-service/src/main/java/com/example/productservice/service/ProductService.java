package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
       List<Product> products = productRepository.findAll();
       return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}

/*
Riassunto:
- Metodo createProduct(ProductRequest productRequest), chiamato dall' endpoint POST del controller.
Esso semplicemente, tramite l' oggetto di tipo ProductRequest (DTO) ricevuto in ingresso, costruisce
un' istanza di Product (Entità) e lo salva nel repository. Produce poi un log che notifica il corretto
salvataggio mostrando l' id del prodotto salvato.

- Metodo getAllProducts(), chiamato dall' endpoint GET del controller.
Esso si fa restituire una lista di Product andando a chiamare il repository con il metodo findAll();
poichè i richiesta una lista di ProductResponse e non di Product, creo una stream dei prodotti resituiti e
chiamo il metodo mapToProductResponse(Product product); questo metodo compie il processo inverso rispetto alla normale
costruzione di un' entità a partire dal dto: costruisce infatti il dto a partire dall' entità che gli
viene passata, e restituisce un oggetto di tipo ProductResponse, che viene mappato a lista nel metodo
getAllProducts().
 */


