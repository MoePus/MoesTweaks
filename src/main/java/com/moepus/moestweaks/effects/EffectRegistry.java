package com.moepus.moestweaks.effects;

import com.moepus.moestweaks.MoesTweaks;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MoesTweaks.MODID);
    public static final RegistryObject<MobEffect> ADRENALINE = MOB_EFFECTS.register("adrenaline", AdrenalineEffect::new);
}
