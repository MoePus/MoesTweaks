package com.moepus.moestweaks.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

public class ChunkStuff {
    @Nullable
    public static BlockPos locateStructure(ServerLevel level, BlockPos initialPos, TagKey<Structure> target) {
        Registry<Structure> registry = level.registryAccess().registry(Registries.STRUCTURE).orElse(null);
        if(registry == null) return null;
        HolderSet<Structure> holderset = registry.getTag(target).orElse(null);
        if(holderset == null) return null;
        Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource().getGenerator().findNearestMapStructure(level, holderset, initialPos, 100, false);
        if (pair == null) {
            return null;
        } else {
            return pair.getFirst();
        }
    }
}
