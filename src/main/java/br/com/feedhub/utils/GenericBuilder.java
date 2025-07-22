package br.com.feedhub.utils;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

public class GenericBuilder<T> {

    private final Supplier<T> instantiator;
    private final List<ConsumerWithInstance<T>> modifiers = new ArrayList<>();

    private GenericBuilder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
        return new GenericBuilder<>(instantiator);
    }

    public static <T> GenericBuilder<T> of(T instance) {
        return new GenericBuilder<>(() -> instance);
    }

    public <U> GenericBuilder<T> with(BiConsumer<T, U> setter, U value) {
        modifiers.add(instance -> setter.accept(instance, value));
        return this;
    }

    public T build() {
        T instance = instantiator.get();
        modifiers.forEach(mod -> mod.accept(instance));
        return instance;
    }

    @FunctionalInterface
    private interface ConsumerWithInstance<T> {
        void accept(T instance);
    }

}