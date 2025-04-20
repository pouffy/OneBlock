package com.pouffydev.oneblock.core.oneblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class GiftEntry extends AbstractOBEntry<GiftEntry> {

    public static final Codec<GiftEntry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("block").forGetter(GiftEntry::getBlockState),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(GiftEntry::getLootTable),
            Codec.INT.fieldOf("weight").orElse(1).forGetter(GiftEntry::getWeight)
    ).apply(instance, GiftEntry::new));

    private final BlockState blockState;
    private final ResourceLocation lootTable;

    public GiftEntry(BlockState blockState, ResourceLocation lootTable, int weight) {
        super(weight);
        this.blockState = blockState;
        this.lootTable = lootTable;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public ResourceLocation getLootTable() {
        return lootTable;
    }

    @Override
    public Codec<GiftEntry> getDirectCodec() {
        return DIRECT_CODEC;
    }

    public static GiftEntryBuilder getBuilder() {
        return new GiftEntryBuilder();
    }

    public static class GiftEntryBuilder extends AbstractOBEntryBuilder<GiftEntry> {
        private BlockState blockState = null;
        private ResourceLocation lootTable = null;
        private int weight = 1;

        public GiftEntryBuilder() {}

        public GiftEntryBuilder blockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        public GiftEntryBuilder lootTable(ResourceLocation lootTable) {
            this.lootTable = lootTable;
            return this;
        }

        public GiftEntryBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        @Override
        public GiftEntry build() {
            if (blockState == null) {
                throw new IllegalStateException("BlockState cannot be null");
            }
            if (lootTable == null) {
                throw new IllegalStateException("LootTable cannot be null");
            }
            return new GiftEntry(blockState, lootTable, weight);
        }
    }
}
