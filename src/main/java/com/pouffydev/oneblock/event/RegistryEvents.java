package com.pouffydev.oneblock.event;

import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class RegistryEvents {
    @SubscribeEvent
    public void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(OneBlockRegistries.STAGE, OBStage.DIRECT_CODEC, OBStage.NETWORK_CODEC);
    }

    @SubscribeEvent
    public void newRegistry(NewRegistryEvent event) {
        event.register(OneBlockRegistries.ENTRY_TYPE_REGISTRY);
    }
}
