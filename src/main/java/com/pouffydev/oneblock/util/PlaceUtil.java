package com.pouffydev.oneblock.util;

import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.oneblock.entry.types.BlockEntry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;

public class PlaceUtil {

    public static OBEntry selectByWeight(RandomSource random, OBStage stage) {
        int totalWeight = stage.entries().stream().mapToInt(OBEntry::getWeight).sum();
        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (OBEntry entry : stage.entries()) {
            currentWeight += entry.getWeight();
            if (randomValue < currentWeight) {
                return entry;
            }
        }

        return new BlockEntry(Blocks.BEDROCK.defaultBlockState(), 1); // This should never happen if the weights are set up correctly
    }

    public static OBEntry endGift(OBStage stage) {
        return stage.endGift();
    }
}
