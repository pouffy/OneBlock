package com.pouffydev.oneblock.util;

import com.pouffydev.oneblock.OneBlock;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModUtils {
    private static final Consumer<?> NO_ACTION = (a) -> {};

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(OneBlock.ID, path);
    }

    public static <DR extends DeferredRegister<T>, T> DR createRegister(Function<String, DR> factory) {
        return registerToBus(factory.apply(OneBlock.ID));
    }

    public static <T> DeferredRegister<T> createRegister(ResourceKey<Registry<T>> registry) {
        return registerToBus(DeferredRegister.create(registry, OneBlock.ID));
    }

    private static <DR extends DeferredRegister<T>, T> DR registerToBus(DR deferredRegister) {
        deferredRegister.register(OneBlock.getEventBus());
        return deferredRegister;
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> noAction() {
        return ((Consumer<T>) NO_ACTION);
    }

    //@SuppressWarnings("unchecked")
    //public static <T extends ManicBlockEntity> T locateBlockEntityInRange(Level world, BlockPos pos, int range) {
    //    for (int x = -range; x <= range; x++) {
    //        for (int y = -range; y <= range; y++) {
    //            for (int z = -range; z <= range; z++) {
    //                BlockPos checkPos = pos.offset(x, y, z);
    //                BlockEntity blockEntity = world.getBlockEntity(checkPos);
    //                if (blockEntity instanceof ManicBlockEntity) {
    //                    return (T) blockEntity;
    //                }
    //            }
    //        }
    //    }
    //    return null;
    //}

    //public static List<BlockPos> getManicBlockEntitiesInRange(Level world, BlockPos pos, int range) {
    //    List<BlockPos> positions = new ArrayList<>();
    //    for (int x = -range; x <= range; x++) {
    //        for (int y = -range; y <= range; y++) {
    //            for (int z = -range; z <= range; z++) {
    //                BlockPos checkPos = pos.offset(x, y, z);
    //                BlockEntity blockEntity = world.getBlockEntity(checkPos);
    //                if (blockEntity instanceof ManicBlockEntity) {
    //                    positions.add(checkPos);
    //                }
    //            }
    //        }
    //    }
    //    return positions;
    //}

    public static <T extends Block> List<BlockPos> getBlocksOfTypeInRange(Level world, BlockPos pos, int range, T type) {
        List<BlockPos> positions = new ArrayList<>();
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState blockState = world.getBlockState(checkPos);
                    if (blockState.getBlock() == type) {
                        positions.add(checkPos);
                    }
                }
            }
        }
        return positions;
    }

    public static <T extends LivingEntity> void playLoopSound(SoundEvent soundEvent, int durationSecs, T entity) {
        //this is called in tick, so we need to wait for the sound to finish before playing it again
        if (entity.tickCount % (durationSecs * 20) == 0) {
            entity.playSound(soundEvent, 1.0F, 1.0F);
        }
    }

    public static void playAmbientSound(SoundEvent soundEvent, Level world, BlockPos pos) {
        //called every tick, so we need to make sure we don't spam the sound but play it at random intervals instead
        int randomInt = world.random.nextInt(100);
        float randomVolume = world.random.nextFloat();
        if (randomInt > 50 && randomInt < 75) {
            world.playSound(null, pos, soundEvent, SoundSource.AMBIENT, randomVolume, 1.0F);
        }
    }

    public static void awardAdvancement(Player player, ResourceLocation advancementLoc) {
        if (player instanceof ServerPlayer serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server == null)
                return;
            AdvancementHolder advancement = server.getAdvancements().get(advancementLoc);
            if (advancement == null)
                return;
            AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
            if (progress.isDone()) {
                return;
            }
            for (String criterion : progress.getRemainingCriteria()) {
                serverPlayer.getAdvancements().award(advancement, criterion);
            }
        }
    }

    public static void runEverySecond(long gameTime, int seconds, Consumer<Integer> action) {
        if (gameTime % seconds == 0) {
            action.accept(seconds);
        }
    }


    //Logging Utils
    public static void logInfo(String message, Object... arguments) {
        OneBlock.LOGGER.info("[{}] {}", OneBlock.NAME, message.formatted(arguments));
    }
    public static void logDebug(String message, Object... arguments) {
        OneBlock.LOGGER.debug("[{}] {}", OneBlock.NAME, message.formatted(arguments));
    }
    public static void logError(String message, Object... arguments) {
        OneBlock.LOGGER.error("[{}] {}", OneBlock.NAME, message.formatted(arguments));
    }
    public static void logWarn(String message, Object... arguments) {
        OneBlock.LOGGER.warn("[{}] {}", OneBlock.NAME, message.formatted(arguments));
    }
    public static void logTrace(String message, Object... arguments) {
        OneBlock.LOGGER.trace("[{}] {}", OneBlock.NAME, message.formatted(arguments));
    }
}
