package com.pouffydev.oneblock.event;

import com.pouffydev.oneblock.handler.LevelHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockEvents {
    @SubscribeEvent
    public void blockBroken(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            //Increase the block break count if the block is a OneBlock
            var saveData = LevelHandler.getSaveData(serverLevel);
            if (saveData.get(pos) != null) {
                saveData.updateBlocksBroken(pos, event.getPlayer());
            }
        }
    }
}
