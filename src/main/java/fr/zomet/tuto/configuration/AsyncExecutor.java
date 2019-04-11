package fr.zomet.tuto.configuration;

import fr.zomet.tuto.rabbit.CorrelationId;
import fr.zomet.tuto.rabbit.Sender;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Service
public class AsyncExecutor<T> {

    private final Sender<T> sender;
    private final Executor executor;

    public AsyncExecutor(Sender<T> sender, Executor executor) {
        this.sender = sender;
        this.executor = executor;
    }

    public CorrelationId execute(Supplier<T> supplier) {
        CorrelationId correlationId = new CorrelationId(UUID.randomUUID());
        CompletableFuture
                .supplyAsync(supplier, executor)
                .whenComplete((t, throwable) -> {
            if (throwable == null) {
                sender.sendMessage(t, correlationId);
            }
        });
        return correlationId;
    }
}
