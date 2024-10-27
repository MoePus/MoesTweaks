package com.moepus.moestweaks.events;

import net.minecraft.world.entity.monster.Silverfish;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;

public class SilverFishNoExp {
    public static void onExpDrop(LivingExperienceDropEvent event)
    {
        if(event.getEntity() instanceof Silverfish)
        {
            event.setCanceled(true);
        }
    }
}
