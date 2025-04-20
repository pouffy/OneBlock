package com.pouffydev.oneblock.data;

import com.pouffydev.oneblock.OneBlock;
import com.pouffydev.oneblock.core.init.data.OBStages;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OneBlockGenEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(OneBlockRegistries.STAGE, OBStages::bootstrap);

    public OneBlockGenEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(OneBlock.ID));
    }

    @Override
    public String getName() {
        return "OneBlock's Generated Registry Entries";
    }
}
