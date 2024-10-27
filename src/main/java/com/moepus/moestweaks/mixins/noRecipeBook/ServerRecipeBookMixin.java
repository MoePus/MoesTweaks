package com.moepus.moestweaks.mixins.noRecipeBook;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ServerRecipeBook.class)
public abstract class ServerRecipeBookMixin {
    @Inject(
            method = {"addRecipes"},
            at = {@At("HEAD")},
            cancellable = true
    )
    void onAddRecipes(Collection<Recipe<?>> p_12792_, ServerPlayer p_12793_, CallbackInfoReturnable<Integer> cir)
    {
        cir.setReturnValue(0);
    }

    @Inject(
            method = {"toNbt"},
            at = {@At("HEAD")},
            cancellable = true
    )
    void onToNbt(CallbackInfoReturnable<CompoundTag> cir)
    {
        cir.setReturnValue(new CompoundTag());
    }

    @Inject(
            method = {"fromNbt"},
            at = {@At("HEAD")},
            cancellable = true
    )
    void onFromNbt(CallbackInfo ci)
    {
        ci.cancel();
    }
}
