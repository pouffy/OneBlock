package com.pouffydev.oneblock.core.oneblock.entry;

import com.mojang.serialization.Codec;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface OBEntry {
    Codec<OBEntry> DIRECT_CODEC = Codec.lazyInitialized(OneBlockRegistries.ENTRY_TYPE_REGISTRY::byNameCodec)
            .dispatch(OBEntry::getType, OBEntryType::codec);

    void apply(Level level, BlockPos pos);

    int getWeight();
    /**
     * @return the type which serializes and deserializes this result
     */
    OBEntryType<? extends OBEntry> getType();
}
