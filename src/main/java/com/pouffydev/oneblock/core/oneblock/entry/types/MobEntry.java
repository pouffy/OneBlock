package com.pouffydev.oneblock.core.oneblock.entry.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pouffydev.oneblock.core.init.OneBlockEntryTypes;
import com.pouffydev.oneblock.core.oneblock.OBStage;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntry;
import com.pouffydev.oneblock.core.oneblock.entry.OBEntryType;
import com.pouffydev.oneblock.core.registry.OneBlockRegistries;
import com.pouffydev.oneblock.handler.GlobalDataManager;
import com.pouffydev.oneblock.util.ModUtils;
import com.pouffydev.oneblock.util.PlaceUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record MobEntry(EntityType<?> entityType, int weight) implements OBEntry {
    public static final MapCodec<MobEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(MobEntry::entityType),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(MobEntry::weight)
    ).apply(instance, MobEntry::new));

    @Override
    public void apply(Level level, BlockPos pos) {
        // Spawn the entity above the block position
        if (level instanceof ServerLevel serverLevel) {
            GlobalDataManager dataManager = GlobalDataManager.get(serverLevel);
            var stageKey = dataManager.get(pos).stage;
            Optional<Registry<OBStage>> stageRegistry = serverLevel.registryAccess().registry(OneBlockRegistries.STAGE);
            if (stageRegistry.isPresent()) {
                var stage = stageRegistry.get().get(stageKey);
                if (stage == null) {
                    ModUtils.logError("Stage not found: {}", stageKey);
                    return;
                }
                var entity = entityType.create(serverLevel);
                if (entity == null) {
                    ModUtils.logError("Entity not found: {}", entityType);
                    return;
                }
                OBEntry randomEntry = PlaceUtil.selectByWeight(serverLevel.getRandom(), stage);
                while (randomEntry instanceof MobEntry) {
                    randomEntry = PlaceUtil.selectByWeight(serverLevel.getRandom(), stage);
                }
                randomEntry.apply(level, pos);
                entity.setPos(pos.getX(), pos.getY() + 1.5, pos.getZ());
                level.addFreshEntity(entity);
            }
        }
    }

    @Override
    public int getWeight() {
        return weight();
    }

    @Override
    public OBEntryType<? extends OBEntry> getType() {
        return OneBlockEntryTypes.MOB.get();
    }
}
