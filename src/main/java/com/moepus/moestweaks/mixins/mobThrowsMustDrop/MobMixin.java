package com.moepus.moestweaks.mixins.mobThrowsMustDrop;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    protected boolean canReplaceCurrentItem(ItemStack pCandidate, ItemStack pExisting){
        return true;
    };

    @Shadow
    protected void setItemSlotAndDropWhenKilled(EquipmentSlot pSlot, ItemStack pStack) {
    }

    /**
     * @author MoePus
     * @reason MustDrop if change equip
     */
    @Overwrite
    public @NotNull ItemStack equipItemIfPossible(ItemStack pStack) {
        Mob mob = (Mob)(Object)this;
        EquipmentSlot equipmentslot = getEquipmentSlotForItem(pStack);
        ItemStack itemstack = this.getItemBySlot(equipmentslot);
        boolean flag = this.canReplaceCurrentItem(pStack, itemstack);
        if (equipmentslot.isArmor() && !flag) {
            equipmentslot = EquipmentSlot.MAINHAND;
            itemstack = this.getItemBySlot(equipmentslot);
            flag = itemstack.isEmpty();
        }

        if (flag && mob.canHoldItem(pStack)) {
            if (!itemstack.isEmpty()) {
                this.spawnAtLocation(itemstack);
            }

            if (equipmentslot.isArmor() && pStack.getCount() > 1) {
                ItemStack itemstack1 = pStack.copyWithCount(1);
                this.setItemSlotAndDropWhenKilled(equipmentslot, itemstack1);
                return itemstack1;
            } else {
                this.setItemSlotAndDropWhenKilled(equipmentslot, pStack);
                return pStack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }
}
