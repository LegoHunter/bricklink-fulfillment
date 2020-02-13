package com.bricklink.fulfillment.shipstation.model;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Holder<T> implements Consumer<T>, Supplier<T> {
    private T t;

    @Override
    public void accept(T t) {
        this.t = t;
    }

    @Override
    public T get() {
        return t;
    }
}
