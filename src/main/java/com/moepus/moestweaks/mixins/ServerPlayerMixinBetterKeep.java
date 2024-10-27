package com.moepus.moestweaks.mixins;

import com.moepus.moestweaks.AllTags;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixinBetterKeep extends Player {
    public ServerPlayerMixinBetterKeep(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(
            method = {"restoreFrom"},
            at = {@At("TAIL")}
    )
    public void onRestoreFrom(ServerPlayer player, boolean p_9017_, CallbackInfo ci) {
        int containerSize = player.getInventory().getContainerSize();

        final TagKey<Item> toolTag = ItemTags.create(new ResourceLocation("forge", "tools"));
        for (int i = 0; i < containerSize; ++i) {
            ItemStack itemstack = player.getInventory().getItem(i);
            if (itemstack.isEmpty()) continue;
            if (!getInventory().getItem(i).isEmpty()) continue;

            if ((i >= 36 && i <= 39) || (i <= 9 && (itemstack.is(toolTag)) || AllTags.hasDeathKeepTag(itemstack))) {
                getInventory().setItem(i, itemstack);
            }
        }
    }
}
