package com.pouffydev.oneblock.core.init.data;

import com.pouffydev.oneblock.OneBlock;
import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.oneblock.entry.types.BlockEntry;
import com.pouffydev.oneblock.core.oneblock.entry.types.GiftEntry;
import com.pouffydev.oneblock.core.oneblock.entry.types.MobEntry;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OBStages {

    public static final ResourceKey<OBStage> stg00 = create("00");
    public static final ResourceKey<OBStage> stg01 = create("01");

    private static ResourceKey<OBStage> create(String name) {
        return ResourceKey.create(OneBlockRegistries.STAGE, OneBlock.asResource(name));
    }

    private static void registerStages(BootstrapContext<OBStage> context) {
        register(context, builder(stg00, new GiftEntry(Blocks.CHEST.defaultBlockState(), ResourceLocation.parse("chests/ruined_portal"), 1), 45)
                .addEntry(new BlockEntry(Blocks.STONE.defaultBlockState(), 10))
                .addEntry(new BlockEntry(Blocks.DIRT.defaultBlockState(), 15))
                .addEntry(new BlockEntry(Blocks.GRAVEL.defaultBlockState(), 13))
                .addEntry(new BlockEntry(Blocks.SAND.defaultBlockState(), 12))
                .addEntry(new GiftEntry(Blocks.CHEST.defaultBlockState(), ResourceLocation.parse("chests/abandoned_mineshaft"), 2))
                .addEntry(new MobEntry(EntityType.PIG, 6))
                .setNextStage(stg01)
        );
        register(context, builder(stg01, new GiftEntry(Blocks.CHEST.defaultBlockState(), ResourceLocation.parse("chests/pillager_outpost"), 1), 100)
                .addEntry(new BlockEntry(Blocks.COAL_ORE.defaultBlockState(), 10))
                .addEntry(new BlockEntry(Blocks.STONE.defaultBlockState(), 15))
                .addEntry(new BlockEntry(Blocks.GRAVEL.defaultBlockState(), 13))
                .addEntry(new BlockEntry(Blocks.SAND.defaultBlockState(), 12))
                .addEntry(new GiftEntry(Blocks.CHEST.defaultBlockState(), ResourceLocation.parse("chests/shipwreck_supply"), 1))
        );
    }

    private static List<ResourceKey<OBStage>> stages = new ArrayList<>();

    private static Builder builder(ResourceKey<OBStage> key, OBEntry endGift, int blocksPerStage) {
        return new Builder(key, blocksPerStage, endGift);
    }

    private static void register(BootstrapContext<OBStage> context, Builder builder) {
        context.register(builder.key, builder.build());
        stages.add(builder.key);
    }

    private static class Builder {
        private final ResourceKey<OBStage> key;
        private final List<OBEntry> entries = new ArrayList<>();
        private final int blocksPerStage;
        private ResourceKey<OBStage> nextStage;
        private final OBEntry endGift;

        public Builder(ResourceKey<OBStage> key, int blocksPerStage, OBEntry endGift) {
            this.key = key;
            this.blocksPerStage = blocksPerStage;
            this.endGift = endGift;
        }

        public Builder addEntry(OBEntry entry) {
            this.entries.add(entry);
            return this;
        }

        public Builder setNextStage(ResourceKey<OBStage> nextStage) {
            this.nextStage = nextStage;
            return this;
        }

        public OBStage build() {
            return new OBStage(blocksPerStage, entries, endGift, Optional.ofNullable(nextStage));
        }
    }

    public static void bootstrap(BootstrapContext<OBStage> context) {
        registerStages(context);
    }

    public static Collection<ResourceKey<OBStage>> getStages() {
        return stages;
    }
}
