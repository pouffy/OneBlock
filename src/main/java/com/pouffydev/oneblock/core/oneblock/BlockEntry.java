package com.pouffydev.oneblock.core.oneblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntry extends AbstractOBEntry<BlockEntry> {

    public static final Codec<BlockEntry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("block").forGetter(BlockEntry::getBlockState),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(AbstractOBEntry::getWeight)
    ).apply(instance, BlockEntry::new));


    private final BlockState blockState;

    public BlockEntry(BlockState blockState, int weight) {
        super(weight);
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public Codec<BlockEntry> getDirectCodec() {
        return DIRECT_CODEC;
    }

    public static BlockEntryBuilder getBuilder() {
        return new BlockEntryBuilder();
    }

    public static class BlockEntryBuilder extends AbstractOBEntryBuilder<BlockEntry> {
        private BlockState blockState = null;
        private int weight = 1;

        public BlockEntryBuilder() {}

        public BlockEntryBuilder blockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        public BlockEntryBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        @Override
        public BlockEntry build() {
            if (blockState == null) {
                throw new IllegalStateException("BlockState cannot be null");
            }
            return new BlockEntry(blockState, weight);
        }
    }
}
