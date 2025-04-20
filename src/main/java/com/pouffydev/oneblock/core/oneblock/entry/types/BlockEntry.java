package com.pouffydev.oneblock.core.oneblock.entry.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.oneblock.core.init.OneBlockEntryTypes;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntryType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record BlockEntry(BlockState blockState, int weight) implements OBEntry {

    public static final MapCodec<BlockEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("block_state").forGetter(BlockEntry::blockState),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(BlockEntry::weight)
    ).apply(instance, BlockEntry::new));

    @Override
    public void apply(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, blockState);
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public OBEntryType<? extends OBEntry> getType() {
        return OneBlockEntryTypes.BLOCK.get();
    }
}
