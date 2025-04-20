package com.pouffydev.oneblock.core.oneblock;

import com.mojang.serialization.Codec;

public abstract class AbstractOBEntry<T extends AbstractOBEntry<?>> {
    private final int weight;

    public AbstractOBEntry(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public abstract Codec<T> getDirectCodec();

    public static AbstractOBEntryBuilder<? extends AbstractOBEntry<?>> getBuilder() {
        return null;
    }

    public abstract static class AbstractOBEntryBuilder<T extends AbstractOBEntry<?>> {
        public abstract T build();
    }
}
