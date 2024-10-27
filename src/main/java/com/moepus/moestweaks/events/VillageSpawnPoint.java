package com.moepus.moestweaks.events;

import com.moepus.moestweaks.utils.ChunkStuff;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraftforge.event.level.LevelEvent;


public class VillageSpawnPoint {
    public static void onCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        LevelAccessor _level = event.getLevel();
        if (_level instanceof ServerLevel level) {
            WorldOptions worldGeneratorOptions = level.getServer().getWorldData().worldGenOptions();
            if (!worldGeneratorOptions.generateStructures()) return;

            BlockPos initialPos = BlockPos.ZERO.above(80);
            BlockPos village = ChunkStuff.locateStructure(level, initialPos, StructureTags.VILLAGE);
            if (village == null) return;

            level.setDefaultSpawnPos(village, 1.0f);

            if (worldGeneratorOptions.generateBonusChest()) {
                level.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap((feature) -> feature.getHolder(MiscOverworldFeatures.BONUS_CHEST)).ifPresent((feature) -> {
                    feature.value().place(level, level.getChunkSource().getGenerator(), level.random, village);
                });
            }
            event.setCanceled(true);
        }
    }
}
