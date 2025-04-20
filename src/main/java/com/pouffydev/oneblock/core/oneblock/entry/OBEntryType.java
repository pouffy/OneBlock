package com.pouffydev.oneblock.core.oneblock.entry;

import com.mojang.serialization.MapCodec;

public record OBEntryType<T extends OBEntry>(MapCodec<T> codec) {
}
