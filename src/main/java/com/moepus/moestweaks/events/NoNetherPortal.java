package com.moepus.moestweaks.events;

import net.minecraftforge.event.level.BlockEvent;

public class NoNetherPortal {
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event)
    {
        event.setCanceled(true);
    }
}
