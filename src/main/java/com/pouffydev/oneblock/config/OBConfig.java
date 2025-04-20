package com.pouffydev.oneblock.config;

import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class OBConfig {
    public static ModConfigSpec COMMON_CONFIG;
    public static ModConfigSpec.ConfigValue<String> startingStage;
    public static ModConfigSpec.BooleanValue usePreviousStagesForEntries;

    public static ResourceKey<OBStage> getStartingStage() {
        return ResourceKey.create(OneBlockRegistries.STAGE, ResourceLocation.parse(startingStage.get()));
    }

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("Play settings").push("Play");
        startingStage = COMMON_BUILDER.comment("Defines the stage at which the OneBlock should start with. Defaults to oneblock:00")
                .define("StartingStage", "oneblock:00", o -> o instanceof String);
        usePreviousStagesForEntries = COMMON_BUILDER.comment("If true, the OneBlock will also use the entries from previous stages.")
                .define("UsePreviousStagesForEntries", false);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
