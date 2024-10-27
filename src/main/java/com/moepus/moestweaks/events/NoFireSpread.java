package com.moepus.moestweaks.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;

public class NoFireSpread {
    public static void onLevelLoaded(LevelEvent.Load event)
    {
        LevelAccessor _level = event.getLevel();
        if(_level instanceof ServerLevel level)
        {
            level.getGameRules().getRule(GameRules.RULE_DOFIRETICK).set(false, level.getServer());
        }
    }
}
