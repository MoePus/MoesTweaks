package com.moepus.moestweaks.mixins.itemEntityOptimization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRenderMixin {
    @Redirect(
            method = {"render"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemEntityRenderer;getRenderAmount(Lnet/minecraft/world/item/ItemStack;)I"
            )
    )
    private int redirectGetRenderAmount(ItemEntityRenderer instance, ItemStack itemstack, ItemEntity itemEntity) {
        int count = itemstack.getCount();
        LocalPlayer player = Minecraft.getInstance().player;
        Vec3 eye = new Vec3(player.getX(), player.getY(), player.getZ());
        int maxRender = (int) (300 / eye.distanceToSqr(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ()));
        return Math.min((count - 1) / 16 + 2, Math.min(maxRender, count));
    }
}
