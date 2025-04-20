package com.pouffydev.oneblock.handler;

import com.pouffydev.oneblock.OneBlock;
import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import com.pouffydev.oneblock.util.ModUtils;
import com.pouffydev.oneblock.util.PlaceUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LevelHandler {
    public static LevelHandler instance = new LevelHandler();

    // public static OneBlockSave oneBlockSave;
    public static final Map<ServerLevel, GlobalDataManager> oneBlockSaveHolder = new HashMap<>();


    public static GlobalDataManager getSaveData(ServerLevel level) {
        GlobalDataManager globalDataManager = oneBlockSaveHolder.get(level);
        if(globalDataManager==null){
            ModUtils.logError("Null One Block Level Data from Level : {}", level);
        }
        return globalDataManager != null ?
                globalDataManager : oneBlockSaveHolder.put(level, GlobalDataManager.get(level));
    }

    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide()  && event.getLevel() instanceof ServerLevel serverLevel)
            oneBlockSaveHolder.putIfAbsent(serverLevel, GlobalDataManager.get(serverLevel));
    }

    @SubscribeEvent
    public void onLevelSave(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverLevel) {
            oneBlockSaveHolder.remove(serverLevel);
        }
    }

    @SubscribeEvent
    public void onTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            var oneBlockSave = getSaveData(serverLevel);
            for (BlockPos pos : oneBlockSave.getBlockPos()) {
                if (serverLevel.isLoaded(pos))
                    generateBlock(serverLevel.getServer(), oneBlockSave, serverLevel, pos);
            }
        }
    }

    private boolean add = false;

    private void generateBlock(MinecraftServer server, GlobalDataManager globalDataManager, ServerLevel level, BlockPos pos) {
        if (level.isEmptyBlock(pos)) {
            var nowProgress = globalDataManager.getOrDefault(pos);
            var stageKey = nowProgress.stage;

            Optional<Registry<OBStage>> stageRegistry = server.registryAccess().registry(OneBlockRegistries.STAGE);
            if (stageRegistry.isPresent()) {
                var stage = stageRegistry.get().get(stageKey);
                if (stage == null) {
                    ModUtils.logError("Stage not found: {}", stageKey);
                    return;
                }
                if (nowProgress.counter >= stage.amount()) {
                    if (stage.nextStage().isPresent()) {
                        nowProgress.updateStage(stage.nextStage().get());
                    } else {
                        globalDataManager.remove(pos);
                    }
                } else if (nowProgress.counter + 1 == stage.amount()) {
                    PlaceUtil.endGift(stage).apply(level, pos);
                }
                PlaceUtil.selectByWeight(level.getRandom(), stage).apply(level, pos);
                nowProgress.updateCounter();
                globalDataManager.update(pos, nowProgress);
            }
        }
    }

}
