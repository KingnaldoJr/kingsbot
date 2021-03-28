package dev.rmjr.kingsbot.listeners;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public  interface EventListener<E extends Event> {
    Class<E> getEventType();
    <T extends Event> Mono<Void> execute(T event);
    Mono<Void> handleError(Throwable error);
}
