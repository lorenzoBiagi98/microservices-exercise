package com.example.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();


        Span inventoryServiceLookUp = tracer.nextSpan().name("InventoryServiceLookUp");
        try {
            Tracer.SpanInScope spanInScope = tracer.withSpanInScope(inventoryServiceLookUp.start());
        } finally {
            inventoryServiceLookUp.finish();
        }

        InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        order.setOrderLineItemsList(orderLineItemsList);

        assert inventoryResponsArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponsArray).allMatch(InventoryResponse::isInStock);

        if (Boolean.TRUE.equals(allProductsInStock)) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed succesfully!";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}

    /*
    Riassunto del metodo placeOrder(OrderRequest orderRequest), chiamato dal controller.
    1) Creazione di un oggetto di tipo Order (Entità), con la generazione di un orderNumber univoco
    e casuale
    2) Creo una lista di oggetti di tipo OrderLineItems (Entità), chiamando prima il metodo
    getOrderLineItemsDtoList(), che restituisce una lista di OrderLineItemsDto (DTO) e poi il metodo
    mapToDto(OrderLineItemsDto orderLineItemsDto), che richiede un oggetto di tipo OrderLineItemsDto
    in ingresso; Ritorna un oggetto di tipo orderLineItems (Entità), creato dai parametri dell'oggetto
    passato. Mappo gli oggetti e li raggruppo in una lista di tipo OrderLineItems.
    3) Settò questi OrderLineItems all' Entità principale Order, chiamando il suo setter
    4) Ho bisogno di una lista di stringhe contente gli skuCodes:chiamo il metodo getOrderLineItemsList
    di Order, lo rendo una stream e lo mappo, andando, tramite un referenceMethod, a prendere solo gli skuCodes,
    che sono un attributo dell' oggetto OrderLineItems, di cui l' oggetto Order contiene una lista.
    5) Creo uno span tramite Zipkin, con il nome di  "InventoryServiceLookUp". E' un operazione di
    tracciamento che inizia con start() e finisce con finish() ---DA RIVEDERE
    6) Creo un array di tipo inventoryResponsArray: faccio una richiesta, tramite webClientBuilder,
    al controller dell' applicazione "Inventory" e gli passo, tramite "queryParam" i parametri che
    mi servono. Retrieve() serve per ottenere la risposta della richiesta, bodyToMono() invece converte
    il corpo della richiesta http in un oggetto di tipo mono, block() blocca il thread corrente fino
    a quando la risposta non è completa.
    7) Controllo che la risposta dell' Inventory non sia null con Assert e mi faccio restituire un booleano,
    andando a streammare l' array di risposte ricevute: se tutti gli oggetti di tipo InventoryResponse
    (classe creata "mockando" quella presente in Inventory) hanno l' attributo isInStock impostato a true
    (allMatch()), allora restituisci TRUE;
    8) Se tutti i prodotti sono in stock, chiamo il repository con save() e salvo l' ordine
    corrente nel database: con Kafka, con il metodo send(), chiamo l'oggetto OrderPlacedEvent(),
    impostandogli, con order.getOrderNumber(), il numero di ordine, che verrà mostrato nella notifica;
    se uno dei prodotti non è in stock invece, viene lanciata un' eccezione
    ------> Chiamo il repository, orderRepository.save()
     */