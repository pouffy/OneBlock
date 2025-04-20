package com.pouffydev.oneblock.core.registry;

import com.mojang.serialization.Lifecycle;
import com.pouffydev.oneblock.OneBlock;
import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntryType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class OneBlockRegistries {
    public static final ResourceKey<Registry<OBStage>> STAGE = OneBlockRegistries.createRegistryKey("oneblock/stage");
    public static final ResourceKey<Registry<OBEntryType<?>>> ENTRY_TYPE = OneBlockRegistries.createRegistryKey("entry_type");

    public static final Registry<OBEntryType<?>> ENTRY_TYPE_REGISTRY = OneBlockRegistries.makeSyncedRegistry(ENTRY_TYPE);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(OneBlock.asResource(name));
    }

    /**
     * Creates a {@link Registry} that get synchronised to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    /**
     * Creates a simple {@link Registry} that <B>won't</B> be synced to clients.
     *
     * @param <T> the entry of the registry.
     */
    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
    private static <T> Registry<T> registerSimpleWithIntrusiveHolders(ResourceKey<? extends Registry<T>> registryKey) {
        return new MappedRegistry<>(registryKey, Lifecycle.stable(), true);
    }

    public static void register() {

    }
}
