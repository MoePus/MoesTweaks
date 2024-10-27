package com.moepus.moestweaks;

import com.moepus.moestweaks.effects.EffectRegistry;
import com.moepus.moestweaks.events.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoesTweaks.MODID)
public class MoesTweaks {
    public static final String MODID = "moestweaks";
    public static final Logger LOGGER = LogUtils.getLogger();
    Config config = ConfigParser.getConfig();

    public MoesTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::loadComplete);

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.addListener(NoCustomSpawner::onLevelLoaded);
        if (config.stopFire)
            forgeEventBus.addListener(NoFireSpread::onLevelLoaded);

        if (config.villageSpawnPoint)
            forgeEventBus.addListener(VillageSpawnPoint::onCreateSpawnPosition);

        if (config.stopNetherPortal)
            forgeEventBus.addListener(NoNetherPortal::onPortalSpawn);

        if (config.bakaSilverFish)
            forgeEventBus.addListener(SilverFishNoExp::onExpDrop);

        if(config.adrenalineEffect)
        {
            EffectRegistry.MOB_EFFECTS.register(modEventBus);
            forgeEventBus.addListener(EventPriority.LOWEST, AdrenalineEffectGiver::onLivingHurt);
        }

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }
    private void loadComplete(final FMLLoadCompleteEvent event) {
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
            if (config.hideShield) forgeEventBus.addListener(HideShield::onHandRender);
        }
    }
}
