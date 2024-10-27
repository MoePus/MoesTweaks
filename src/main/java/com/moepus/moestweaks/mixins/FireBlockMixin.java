package com.moepus.moestweaks.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends Block {
    @Unique
    private static final Set<Block> fireblocks = new HashSet<>(Arrays.asList(Blocks.NETHERRACK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL));

    public FireBlockMixin(Properties p_60452_) {
        super(p_60452_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState)
    {
        return true;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void injected_tick(CallbackInfo ci) {
        ci.cancel();
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, ServerLevel level, BlockPos pos, @NotNull RandomSource randomSource) {
        Block belowblock = level.getBlockState(pos.below()).getBlock();
        if(fireblocks.contains(belowblock)) return;
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
}
