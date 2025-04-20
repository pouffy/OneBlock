package com.pouffydev.oneblock.core.oneblock.entry.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.oneblock.core.init.OneBlockEntryTypes;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntryType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public record GiftEntry(BlockState blockState, ResourceLocation lootTable, int weight) implements OBEntry {
    public static final MapCodec<GiftEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("block_state").forGetter(GiftEntry::blockState),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(GiftEntry::lootTable),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(GiftEntry::weight)
    ).apply(instance, GiftEntry::new));

    @Override
    public void apply(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, blockState);
        if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity containerBlockEntity)
            containerBlockEntity.setLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTable()), level.getRandom().nextLong());
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public OBEntryType<? extends OBEntry> getType() {
        return OneBlockEntryTypes.GIFT.get();
    }
}
