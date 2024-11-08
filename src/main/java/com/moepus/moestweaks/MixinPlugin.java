package com.moepus.moestweaks;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    private Config config;

    @Override
    public void onLoad(String mixinPackage) {
        config = ConfigParser.getConfig();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
            case "com.moepus.moestweaks.mixins.FireBlockMixin" -> config.stopFire;
            case "com.moepus.moestweaks.mixins.HoglinMixin" -> config.hoglinTweaks;
            case "com.moepus.moestweaks.mixins.AbstractPiglinMixin" -> config.piglinTweaks;
            case "com.moepus.moestweaks.mixins.SlimeMixin" -> config.moreSlime;
            case "com.moepus.moestweaks.mixins.MobMixin" -> config.mobThrowsMustDrop;
            case "com.moepus.moestweaks.mixins.PlayerMixinBetterKeep", "com.moepus.moestweaks.mixins.ServerPlayerMixinBetterKeep" ->
                    config.betterKeepInv;
            case "com.moepus.moestweaks.mixins.LocalPlayerMixinDoubleJmp", "com.moepus.moestweaks.mixins.ServerPlayerMixinDoubleJmp" ->
                    config.doubleJump;
            case "com.moepus.moestweaks.mixins.noRecipeBook.ClientPacketListenerMixin", "com.moepus.moestweaks.mixins.noRecipeBook.ServerRecipeBookMixin", "com.moepus.moestweaks.mixins.noRecipeBook.AbstractFurnaceScreenMixin", "com.moepus.moestweaks.mixins.noRecipeBook.CraftingScreenMixin", "com.moepus.moestweaks.mixins.noRecipeBook.InventoryScreenMixin" ->
                    config.noRecipeBook;
            case "com.moepus.moestweaks.mixins.itemEntityOptimization.ItemRendererMixin", "com.moepus.moestweaks.mixins.itemEntityOptimization.ItemEntityRenderMixin", "com.moepus.moestweaks.mixins.itemEntityOptimization.ItemTransformMixin" -> config.itemEntityRenderOptimization;
            default -> true;
        };
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}