package com.pouffydev.oneblock.handler;

import com.pouffydev.oneblock.OneBlock;
import com.pouffydev.oneblock.core.OneBlockData;
import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GlobalDataManager extends SavedData {

    private final Map<BlockPos, OneBlockData> chunkPosData = new HashMap<>();

    private String hashStageVersion="";

    public GlobalDataManager() {
    }

    public GlobalDataManager(HolderLookup.Provider provider, CompoundTag tag) {
        ListTag list = tag.getList(OneBlock.ID, Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag oneBlockDataTag = (CompoundTag) t;
            BlockPos chunkPos = new BlockPos(oneBlockDataTag.getInt("x"), oneBlockDataTag.getInt("y"), oneBlockDataTag.getInt("z"));
            ResourceLocation stageLoc = ResourceLocation.parse(oneBlockDataTag.getString("stage"));
            ResourceKey<OBStage> stage = ResourceKey.create(OneBlockRegistries.STAGE, stageLoc);
            var data = new OneBlockData(stage, oneBlockDataTag.getInt("counter"));
            if (oneBlockDataTag.contains("bedrockLastTime"))
                data.counter = oneBlockDataTag.getInt("bedrockLastTime");
            if (oneBlockDataTag.contains("timesBroken"))
                data.timesBroken = oneBlockDataTag.getInt("timesBroken");
            if (oneBlockDataTag.contains("remainCounter"))
                data.remainCounter = oneBlockDataTag.getList("remainCounter", ListTag.TAG_COMPOUND);
            if (oneBlockDataTag.contains("quotaCounter"))
                data.quotaCounter = oneBlockDataTag.getList("quotaCounter", ListTag.TAG_COMPOUND);
            if (oneBlockDataTag.contains("precedenceCounter"))
                data.precedenceCounter = oneBlockDataTag.getList("precedenceCounter", ListTag.TAG_COMPOUND);
            if (oneBlockDataTag.contains("participatingPlayers"))
                data.participatingPlayers = oneBlockDataTag.getList("participatingPlayers", ListTag.TAG_COMPOUND);
            chunkPosData.put(chunkPos, data);
        }

        if (tag.contains("oneblockVersion")){
            hashStageVersion=tag.getString("oneblockVersion");
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        chunkPosData.forEach((chunkPos, oneBlockData) -> {
            CompoundTag oneBlockDataTag = new CompoundTag();
            oneBlockDataTag.putInt("x", chunkPos.getX());
            oneBlockDataTag.putInt("y", chunkPos.getY());
            oneBlockDataTag.putInt("z", chunkPos.getZ());
            oneBlockDataTag.putString("stage", oneBlockData.stage.location().toString());
            oneBlockDataTag.putInt("counter", oneBlockData.counter);
            oneBlockDataTag.putInt("timesBroken", oneBlockData.timesBroken);
            oneBlockDataTag.put("remainCounter", oneBlockData.remainCounter);
            oneBlockDataTag.put("quotaCounter", oneBlockData.quotaCounter);
            oneBlockDataTag.put("precedenceCounter", oneBlockData.precedenceCounter);
            oneBlockDataTag.put("participatingPlayers", oneBlockData.participatingPlayers);
            list.add(oneBlockDataTag);
        });
        compoundTag.put(OneBlock.ID, list);
        compoundTag.putString("hashStageVersion","");
        return compoundTag;
    }

    public void update(BlockPos blockPos, OneBlockData data) {
        chunkPosData.put(blockPos, data);
        setDirty();
    }

    public OneBlockData getOrDefault(BlockPos pos) {
        return chunkPosData.getOrDefault(pos, new OneBlockData());
    }

    public OneBlockData get(BlockPos pos) {
        return chunkPosData.get(pos);
    }

    public BlockPos remove(BlockPos blockPos) {
        var remove = chunkPosData.remove(blockPos);
        setDirty();
        return remove == null ? null : blockPos;
    }

    public Set<BlockPos> getBlockPos() {
        return chunkPosData.keySet();
    }

    public void updateBlocksBroken(BlockPos pos, Player player) {
        OneBlockData data = getOrDefault(pos);
        data.updateTimesBroken(player);
        update(pos, data);
    }

    public static GlobalDataManager get(ServerLevel serverLevel) {
        DimensionDataStorage storage = serverLevel.getDataStorage();
        return storage.computeIfAbsent(new Factory<>(() -> create(serverLevel), ((compoundTag, provider) -> load(serverLevel, compoundTag, provider))), OneBlock.ID);
    }

    private static GlobalDataManager load(ServerLevel serverLevel, CompoundTag compoundTag, HolderLookup.Provider provider) {
        return new GlobalDataManager(serverLevel.registryAccess(), compoundTag);
    }

    private static GlobalDataManager create(ServerLevel serverLevel) {
        return new GlobalDataManager();
    }
}
