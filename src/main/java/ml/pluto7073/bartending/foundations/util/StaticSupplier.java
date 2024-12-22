package ml.pluto7073.bartending.foundations.util;

import java.util.function.Supplier;

public class StaticSupplier<T> implements Supplier<T> {

    private final Supplier<T> factory;
    private T value;

    public StaticSupplier(Supplier<T> factory) {
        this.factory = factory;
    }

    @Override
    public T get() {
        if (value == null) {
            value = factory.get();
        }
        return value;
    }
}
