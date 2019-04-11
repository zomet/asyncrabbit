package fr.zomet.tuto.rabbit;

import java.util.Objects;
import java.util.UUID;

public class CorrelationId {

    private final UUID value;

    public CorrelationId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
