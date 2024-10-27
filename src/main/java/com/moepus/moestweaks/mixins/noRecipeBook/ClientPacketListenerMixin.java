package com.moepus.moestweaks.mixins.noRecipeBook;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Redirect(
            method = "handleUpdateRecipes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/ClientRecipeBook;setupCollections(Ljava/lang/Iterable;Lnet/minecraft/core/RegistryAccess;)V"
            )
    )
    private void redirectSetupCollections(ClientRecipeBook instance, Iterable<Recipe<?>> p_266814_, RegistryAccess p_266878_) {
    }

    @Inject(
            method = {"handleAddOrRemoveRecipes", "handlePlaceRecipe"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void onhandleRecipes(CallbackInfo ci) {
        ci.cancel();
    }

}

