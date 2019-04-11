package fr.zomet.tuto.api;

import fr.zomet.tuto.rabbit.NoSuchElementException;
import fr.zomet.tuto.rabbit.Receiver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/messages/{correlationId}")
public class RabbitController {

    private final Receiver receiver;

    public RabbitController(Receiver receiver) {
        this.receiver = receiver;
    }

    @GetMapping
    public ResponseEntity<String> getMessageByCorrelationId(@PathVariable("correlationId") String correlationId) {
        try {
            return ResponseEntity.ok(receiver.receiveMessage(correlationId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.noContent().build();
        }
    }
}
