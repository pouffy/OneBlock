package com.pouffydev.oneblock;

import com.mojang.logging.LogUtils;
import com.pouffydev.oneblock.config.OBConfig;
import com.pouffydev.oneblock.core.init.OneBlockEntryTypes;
import com.pouffydev.oneblock.core.init.OneBlockObjects;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import com.pouffydev.oneblock.data.OneBlockDataGen;
import com.pouffydev.oneblock.event.OneBlockEventHandler;
import com.pouffydev.oneblock.handler.LevelHandler;
import com.pouffydev.oneblock.util.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(OneBlock.ID)
public class OneBlock {
    private static OneBlock INSTANCE;
    public static final String ID = "oneblock";
    public static final String NAME = "One Block";
    public static final Logger LOGGER = LogUtils.getLogger();
    private final IEventBus modEventBus;

    public OneBlock(IEventBus modEventBus, ModContainer modContainer) {
        this.modEventBus = modEventBus;
        INSTANCE = this;
        new OneBlockEventHandler(modEventBus).register();
        OneBlockRegistries.register();
        OneBlockEntryTypes.staticInit();
        OneBlockObjects.staticInit();

        NeoForge.EVENT_BUS.register(LevelHandler.instance);

        modContainer.registerConfig(ModConfig.Type.COMMON, OBConfig.COMMON_CONFIG);
        this.modEventBus.addListener(EventPriority.LOWEST, OneBlockDataGen::gatherData);
        this.modEventBus.register(this);
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        ModUtils.logInfo("Load Complete!");
    }

    public static IEventBus getEventBus() {
        return INSTANCE.modEventBus;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}