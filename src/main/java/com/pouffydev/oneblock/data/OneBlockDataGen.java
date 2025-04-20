package com.pouffydev.oneblock.data;

import com.pouffydev.oneblock.OneBlock;
import com.pouffydev.oneblock.util.ModUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class OneBlockDataGen {
    public static void gatherData(GatherDataEvent event) {
        ModUtils.logInfo("Data Generation Starts...");
        String modId = OneBlock.ID;
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        OneBlockGenEntriesProvider generatedEntriesProvider = new OneBlockGenEntriesProvider(packOutput, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        dataGenerator.addProvider(event.includeServer(), generatedEntriesProvider);

    }
}
