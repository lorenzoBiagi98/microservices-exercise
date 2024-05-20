package org.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }


    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // send out an email notification
        log.info("Received Notification from order - {}", orderPlacedEvent.getOrderNumber());
    }

}

/*
Riassunto: il metodo handleNotification(OrderPlacedEvent orderPlacedEvent) è usato ogni volta che
un messaggio viene pubblicato sul topic "notificationTopic" (specificato in application.properties).
Riceve in ingresso un oggetto di tipo OrderPlacedEvent, che tecnicamente rappresenta un' ordinazione
piazzata.
NB: l' annotazione @KafkaListener(topics="topic") indica che il metodo è un ascoltatore di Kafka.
Ascolterà il topic chiamato "notificationTopic" (specificato in application.properties).
 */
