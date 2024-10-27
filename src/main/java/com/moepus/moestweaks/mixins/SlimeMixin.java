package com.moepus.moestweaks.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

@Mixin(Slime.class)
public abstract class SlimeMixin {
    @Inject(method = "checkSlimeSpawnRules", at = @At("HEAD"), cancellable = true, remap = true)
    private static void caveSlimeSpawns(EntityType<Slime> slime, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (level.getBiome(pos).is(BiomeTags.IS_OVERWORLD)) {
            if (pos.getY() >= 60) return;
            if (random.nextInt(112) < (pos.getY() + 64)) return;

            int light = level.getLightEmission(pos);
            if (light > 7) return;
            if(!level.getBlockState(pos).is(Blocks.DEEPSLATE) && light != 0) return;
            cir.setReturnValue(checkMobSpawnRules(slime, level, spawnType, pos, random));
        }
    }
}
