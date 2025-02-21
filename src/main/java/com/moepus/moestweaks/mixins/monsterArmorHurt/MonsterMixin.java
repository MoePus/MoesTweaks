package com.moepus.moestweaks.mixins.monsterArmorHurt;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Monster.class)
public abstract class MonsterMixin extends LivingEntity {
    protected MonsterMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damage) {
        if(damage <= 0)
            return;

        damage = Math.max(damage, 1.0F);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorItem = this.getItemBySlot(slot);
                if(!(armorItem.getItem() instanceof ArmorItem))
                    continue;

                if (armorItem.isEmpty())
                    continue;

                if(!armorItem.isDamageableItem())
                    continue;

                if(damageSource.is(DamageTypeTags.IS_FALL))
                    continue;

                if(damageSource.is(DamageTypeTags.IS_FIRE) && armorItem.getItem().isFireResistant())
                    continue;

                armorItem.hurtAndBreak((int) damage, this, (entity) -> {
                    entity.broadcastBreakEvent(slot);
                });
            }
        }
    }
}
