package com.pouffydev.oneblock.core.init;

import com.mojang.serialization.MapCodec;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntryType;
import com.pouffydev.oneblock.core.oneblock.entry.types.BlockEntry;
import com.pouffydev.oneblock.core.oneblock.entry.types.GiftEntry;
import com.pouffydev.oneblock.core.oneblock.entry.types.MobEntry;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import com.pouffydev.oneblock.util.ModUtils;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OneBlockEntryTypes {
    public static final DeferredRegister<OBEntryType<?>> ENTRY_TYPES = ModUtils.createRegister(OneBlockRegistries.ENTRY_TYPE);

    public static final DeferredHolder<OBEntryType<?>, OBEntryType<BlockEntry>> BLOCK = register("block", BlockEntry.CODEC);
    public static final DeferredHolder<OBEntryType<?>, OBEntryType<GiftEntry>> GIFT = register("gift", GiftEntry.CODEC);
    public static final DeferredHolder<OBEntryType<?>, OBEntryType<MobEntry>> MOB = register("mob", MobEntry.CODEC);

    public static <T extends OBEntry> DeferredHolder<OBEntryType<?>, OBEntryType<T>> register(String name, MapCodec<T> codec) {
        return ENTRY_TYPES.register(name, () -> new OBEntryType<>(codec));
    }

    public static void staticInit() {}
}
