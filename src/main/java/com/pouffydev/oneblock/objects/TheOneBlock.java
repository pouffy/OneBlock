package com.pouffydev.oneblock.objects;

import com.pouffydev.oneblock.handler.LevelHandler;
import com.pouffydev.oneblock.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class TheOneBlock extends Block {
    public TheOneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockBlockStateBuilder) {
        super.createBlockStateDefinition(blockBlockStateBuilder);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos pos, BlockPos pos1) {
        if (accessor instanceof ServerLevel serverLevel) {
            serverLevel.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, blockState, accessor, pos, pos1);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state1, boolean p_60570_) {
        super.onPlace(state, level, pos, state1, p_60570_);

        if (level instanceof ServerLevel serverLevel) {
            // it may be clear but be cautious on remove method here so we have a fallon to help it
            serverLevel.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.tick(state, level, pos, randomSource);
        // if (level instanceof ServerLevel serverLevel)
        {
            ModUtils.logInfo("Loading a stage at {}", pos);
            var save = LevelHandler.getSaveData(level);
            save.remove(pos);
            save.update(pos, save.getOrDefault(pos));
            level.removeBlock(pos, false);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float v) {
        super.fallOn(level, state, pos, entity, v);
        if (level instanceof ServerLevel serverLevel)
            this.tick(state, serverLevel, pos, level.getRandom());
    }
}
