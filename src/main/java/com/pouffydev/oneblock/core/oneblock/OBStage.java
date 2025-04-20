package com.pouffydev.oneblock.core.oneblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Optional;

public record OBStage(int amount, List<OBEntry> entries, OBEntry endGift, Optional<ResourceKey<OBStage>> nextStage) {
    public static final Codec<OBStage> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(OBStage::amount),
            OBEntry.DIRECT_CODEC.listOf().fieldOf("entries").forGetter(s -> s.entries),
            OBEntry.DIRECT_CODEC.fieldOf("end_gift").forGetter(OBStage::endGift),
            ResourceKey.codec(OneBlockRegistries.STAGE).optionalFieldOf("next_stage").forGetter(OBStage::nextStage)
    ).apply(instance, OBStage::new));

    public static final Codec<OBStage> NETWORK_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(OBStage::amount),
            OBEntry.DIRECT_CODEC.listOf().fieldOf("entries").forGetter(s -> s.entries),
            OBEntry.DIRECT_CODEC.fieldOf("end_gift").forGetter(OBStage::endGift),
            ResourceKey.codec(OneBlockRegistries.STAGE).optionalFieldOf("next_stage").forGetter(OBStage::nextStage)
    ).apply(instance, OBStage::new));

}
