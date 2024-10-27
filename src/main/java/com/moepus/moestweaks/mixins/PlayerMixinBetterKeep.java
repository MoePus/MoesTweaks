package com.moepus.moestweaks.mixins;

import com.moepus.moestweaks.AllTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class PlayerMixinBetterKeep extends LivingEntity {
    @Shadow
    private Inventory inventory;

    @Shadow
    @Nullable
    public abstract ItemEntity drop(ItemStack p_36179_, boolean p_36180_, boolean p_36181_);

    protected PlayerMixinBetterKeep(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(
            method = {"dropEquipment"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void onDropEquipment(CallbackInfo ci) {
        Level level = level();
        boolean keepInventory = level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get();
        if (keepInventory) return;

        int containerSize = inventory.getContainerSize();

        final TagKey<Item> toolTag = ItemTags.create(new ResourceLocation("forge", "tools"));
        for (int i = 0; i < containerSize; ++i) {
            ItemStack itemstack = inventory.getItem(i);
            if (itemstack.isEmpty()) continue;

            if (EnchantmentHelper.hasVanishingCurse(itemstack)) inventory.removeItemNoUpdate(i);

            if (i >= 36 && i <= 39) continue; // Helmet - Boots

            if (i <= 9 && (itemstack.is(toolTag)) || AllTags.hasDeathKeepTag(itemstack)) continue;

            drop(itemstack, true, false);
            inventory.setItem(i, ItemStack.EMPTY);
        }

        ci.cancel();
    }
}
