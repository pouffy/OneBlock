package com.pouffydev.oneblock.core;

import com.pouffydev.oneblock.config.OBConfig;
import com.pouffydev.oneblock.core.oneblock.OBStage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OneBlockData {
    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_COUNT = "count";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";
    public static final String KEY_PARTICIPATING_PLAYER = "player";

    public ResourceKey<OBStage> stage;
    public int counter;
    public int bedrockLastTime;
    public int timesBroken;
    public ListTag remainCounter;
    public ListTag quotaCounter;
    public ListTag precedenceCounter;
    private static final int TAG_REMAIN = 1;
    private static final int TAG_QUOTA = 2;
    private static final int TAG_precedence_LOCK = 3;
    private static final int TAG_PARTICIPATING_PLAYERS = 4;
    public ListTag participatingPlayers = new ListTag();

    public OneBlockData(ResourceKey<OBStage> stage, int blocksLeft) {
        this.stage = stage;
        this.counter = blocksLeft;
        this.bedrockLastTime = 0;
        this.remainCounter = new ListTag();
        this.quotaCounter = new ListTag();
        this.precedenceCounter = new ListTag();
    }

    public OneBlockData(ResourceKey<OBStage> stage, int blocksLeft, int bedrockLastTime, ListTag remainCounter, ListTag quotaCounter, ListTag precedenceCounter) {
        this.stage = stage;
        this.counter = blocksLeft;
        this.bedrockLastTime = bedrockLastTime;
        this.remainCounter = remainCounter;
        this.quotaCounter = quotaCounter;
        this.precedenceCounter = precedenceCounter;
    }

    public OneBlockData() {
        this(OBConfig.getStartingStage(), 0);
    }

    public void updateStage(ResourceKey<OBStage> stage) {
        this.stage = stage;
        this.counter = 0;
    }

    public void addParticipatingPlayer(UUID playerUUID) {
        var tag = new CompoundTag();
        tag.putUUID(KEY_PARTICIPATING_PLAYER, playerUUID);
        this.participatingPlayers.add(tag);
    }

    public List<UUID> getParticipatingPlayers() {
        List<UUID> players = new ArrayList<>();
        for (int i = 0; i < this.participatingPlayers.size(); i++) {
            var tag = this.participatingPlayers.getCompound(i);
            players.add(tag.getUUID(KEY_PARTICIPATING_PLAYER));
        }
        return players;
    }

    public int getRemainAmount() {
        int amount = 0;
        for (int i = 0; i < this.remainCounter.size(); i++) {
            var tag = this.remainCounter.getCompound(i);
            amount += tag.getInt(KEY_COUNT);
        }
        return amount;
    }

    public void addListTag(int tagType, String type, String id, int count) {
        var tag = new CompoundTag();
        tag.putString(KEY_TYPE, type);
        tag.putString(KEY_ID, id);
        tag.putInt(KEY_COUNT, count);
        if (tagType == TAG_REMAIN)
            this.remainCounter.add(tag);
        else if (tagType == TAG_QUOTA) {
            this.quotaCounter.add(tag);
        }
    }

    public void addRemain(String type, String id, int count) {
        addListTag(TAG_REMAIN, type, id, count);
    }

    public void addQuota(String type, String id, int count) {
        addListTag(TAG_QUOTA, type, id, count);
    }

    public void addPrecedence(String type, String id, int start, int end) {
        var tag = new CompoundTag();
        tag.putString(KEY_TYPE, type);
        tag.putString(KEY_ID, id);
        tag.putInt(KEY_START, start);
        tag.putInt(KEY_END, end);
        this.precedenceCounter.add(tag);
    }

    public int updateListTag(int tagType, String type, String id) {
        var listTag = tagType == TAG_REMAIN ? this.remainCounter : this.quotaCounter;
        int removeIndex = -1;
        for (int i = 0; i < listTag.size(); i++) {
            var tag = listTag.getCompound(i);
            if (tag.getString(KEY_TYPE).equals(type)
                    && tag.getString(KEY_ID).equals(id)) {
                int count = tag.getInt(KEY_COUNT) - 1;
                if (count > 0) {
                    tag.putInt(KEY_COUNT, tag.getInt(KEY_COUNT) - 1);
                } else {
                    removeIndex = i;
                }
                break;
            }
        }
        return removeIndex;
    }

    public void updateRemain(String type, String id) {
        int removeIndex = updateListTag(TAG_REMAIN, type, id);
        // it's not safe to delete anything in a cycle
        if (removeIndex >= 0) this.remainCounter.remove(removeIndex);
    }

    public void updateQuota(String type, String id) {
        int toZeroIndex = updateListTag(TAG_REMAIN, type, id);
        // just record the entry count 0
    }

    public int indexOfListTag(int tagType, String type, String id) {
        var listTag = tagType == TAG_REMAIN ? this.remainCounter : this.quotaCounter;
        int index = -1;
        for (int i = 0; i < listTag.size(); i++) {
            var tag = listTag.getCompound(i);
            if (tag.getString(KEY_TYPE).equals(type)
                    && tag.getString(KEY_ID).equals(id)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int indexOfRemain(String type, String id) {
        return indexOfListTag(TAG_REMAIN, type, id);
    }

    public int indexOfQuota(String type, String id) {
        return indexOfListTag(TAG_QUOTA, type, id);
    }

    public boolean checkQuota(String type, String id) {
        int index = indexOfQuota(type, id);
        boolean result = true;
        if (index >= 0) {
            result = this.quotaCounter.getCompound(index).getInt(KEY_COUNT) > 0;
        }
        return result;
    }

    public String checkPrecedence(int localCount) {
        String uid = null;
        for (int i = 0; i < precedenceCounter.size(); i++) {
            var tag = precedenceCounter.getCompound(i);
            int start = tag.getInt(KEY_START);
            start = start != 0 ? start : Integer.MIN_VALUE;
            int end = tag.getInt(KEY_END);
            end = end != 0 ? end : Integer.MAX_VALUE;
            if (start <= localCount && localCount <= end) {
                uid = tag.getString(KEY_ID);
                break;
            }
        }
        return uid;
    }

    public void cleanRemain() {
        this.remainCounter.clear();
    }

    public void cleanQuota() {
        this.quotaCounter.clear();
    }

    public void cleanPrecedence() {
        this.precedenceCounter.clear();
    }

    public void cleanLocalCounter() {
        this.cleanRemain();
        this.cleanQuota();
        this.cleanPrecedence();
    }

    public void updateBedrockLastTime() {
        this.bedrockLastTime--;
    }

    public void setBedrockLastTime(int tick) {
        this.bedrockLastTime = tick;
    }

    public void updateCounter() {
        this.counter++;
    }

    public void updateTimesBroken(Player player) {
        this.timesBroken++;
        if (player != null) {
            if (!this.participatingPlayers.isEmpty()) {
                for (int i = 0; i < this.participatingPlayers.size(); i++) {
                    var tag = this.participatingPlayers.getCompound(i);
                    if (tag.getUUID(KEY_PARTICIPATING_PLAYER).equals(player.getUUID())) {
                        return;
                    }
                }
            }
            this.addParticipatingPlayer(player.getUUID());
        }
    }

    public void updateLocalCounter(String type, String id) {
        this.updateRemain(type, id);
        this.updateQuota(type, id);
    }
}
