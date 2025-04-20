package com.pouffydev.oneblock.objects;

import com.pouffydev.oneblock.core.init.OneBlockObjects;
import com.pouffydev.oneblock.handler.LevelHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class DebugWrenchItem extends Item {
    public DebugWrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack itemstack = player.getItemInHand(hand);

            BlockHitResult hitresult = getPlayerPOVHitResult(serverLevel, player, ClipContext.Fluid.NONE);
            if (hitresult.getType() == HitResult.Type.MISS) {
                return InteractionResultHolder.pass(itemstack);
            } else {
                if (hitresult.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = hitresult.getBlockPos();
                    var save = LevelHandler.getSaveData(serverLevel);
                    if (itemstack.getDamageValue() != 0) {
                        var oldPos = save.remove(pos);
                        if (oldPos != null) {
                            if (!player.isCreative())
                                itemstack.setDamageValue(0);
                            level.removeBlock(pos, false);
                        }

                    } else {
                        if (!player.isCreative()) itemstack.setDamageValue(1);
                        if (player.isCrouching())
                            save.remove(pos);
                        else
                            level.setBlockAndUpdate(pos, OneBlockObjects.theOneBlock.get().defaultBlockState());
                    }
                    return InteractionResultHolder.success(itemstack);
                } else {
                    return InteractionResultHolder.pass(itemstack);
                }
            }

        }
        return super.use(level, player, hand);
    }
}
