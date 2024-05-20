package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Ooops! Something went wrong, please order after some time!");
    }

}
/*
Riassunto: le annotazioni @CircuitBraker, @TimeLimiter e @Retry appartengono a Resilience4j;
la prima protegge il metodo da un circuitBraker, che in caso di errori re-indirizzerà il circuito
al metodo di fallback, fallbackMethod(); la seconda imposta un un limite massimo entro il quale
il metodo deve completarsi, altrimenti verrà interrotto. La terza, in base alla configurazione,
"ritenta" il metodo se vengono generate eccezioni.
#
ComletableFuture<String> è un'operazione asincrona che restiruirà una stringa solo quando l' operazione
verrà completata. Queste operazioni non bloccano il programma, esso continua ad essere in esecuzione
mentre il metodo chiamato viene processato
CompletableFuture.supplyAsync(()->...) è un' operazione asincrona, viene quindi eseguita su un thread
separato rispetto al principale. Essa restituisce il risultato solo quando l' operazione è completata.
#
placeOrder(OrderRequest orderRequest), chiamata post usata per piazzare un ordine, che ne riceve
appunto uno, di tipo OrderRequest(DTO) in ingresso.
1) Chiama il service, il metodo placeOrder(orderRequest) passando un oggetto di tipo OrderRequest.
---------> Se vuoi approfondire DTO, vedi dto.OrderRequest
---------> Continua su orderService
 */
