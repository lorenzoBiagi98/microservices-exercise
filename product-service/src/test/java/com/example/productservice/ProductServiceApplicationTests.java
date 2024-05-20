package com.example.productservice;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

//Se avessi dei blocchi if/else o dei blocchi try/catch dovrei fare tanti mockMVC.perform quanti blocchi
	//else/catch ci sono, passando ogni volta il dato che manda in errore quello specifico blocco

	//Il metodo per creare l' utente puoi parametrizzrlo, per evitare di rifare ogni volta un nuovo metodo
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
	}

	@Test
	void shouldGetProduct() throws Exception{
		int allProducts = productRepository.findAll().size();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
				.andExpect(status().isOk());
		Assertions.assertEquals(allProducts, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 15")
				.description("Il telefono più costoso")
				.price(BigDecimal.valueOf(1300))
				.build();
	}
}
