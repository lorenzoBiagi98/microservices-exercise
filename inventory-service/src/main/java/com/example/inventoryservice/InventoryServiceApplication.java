package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
	return args -> {
		Inventory inventory = new Inventory();
		inventory.setSkuCode("Iphone 15");
		inventory.setQuantity(10);
		inventoryRepository.save(inventory);

		Inventory inventory1 = new Inventory();
		inventory1.setSkuCode("Iphone 12");
		inventory1.setQuantity(0);
		inventoryRepository.save(inventory1);
	};
	}

}
