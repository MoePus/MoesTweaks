package com.moepus.moestweaks.events;

import com.google.common.collect.ImmutableList;
import com.moepus.moestweaks.Config;
import com.moepus.moestweaks.ConfigParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraftforge.event.level.LevelEvent;

public class NoCustomSpawner {
    public static void onLevelLoaded(LevelEvent.Load event)
    {
        LevelAccessor _level = event.getLevel();
        if(_level instanceof ServerLevel level)
        {
            Config config = ConfigParser.getConfig();
            ImmutableList.Builder<CustomSpawner> builder = ImmutableList.builder();
            for (CustomSpawner spawner : level.customSpawners) {
                if(config.noCatSpawner && spawner instanceof CatSpawner) continue;
                if(config.noPhantomSpawner && spawner instanceof PhantomSpawner) continue;
                if(config.noPatrolSpawner && spawner instanceof PatrolSpawner) continue;
                if(config.noVillageSiege && spawner instanceof VillageSiege) continue;
                if(config.noWanderingTraderSpawner && spawner instanceof WanderingTraderSpawner) continue;
                builder.add(spawner);
            }
            level.customSpawners = builder.build();
        }
    }
}
