package com.pouffydev.oneblock.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class OneBlockEventHandler {
    private final IEventBus modEventBus;

    public OneBlockEventHandler(IEventBus modEventBus) {
        this.modEventBus = modEventBus;
    }

    public void registerModEvents(IEventBus eventBus) {
        eventBus.register(new RegistryEvents());
        //eventBus.register(new CommonEvents());
        //eventBus.register(new CapabilityEvents());
        //eventBus.register(new NetworkEvents());

    }

    public void registerForgeEvents(IEventBus eventBus) {
        eventBus.register(new BlockEvents());
        //eventBus.register(new ClientEvents());
        //eventBus.register(new EntityEvents());
    }

    public void register() {
        registerModEvents(modEventBus);
        registerForgeEvents(NeoForge.EVENT_BUS);
    }
}
