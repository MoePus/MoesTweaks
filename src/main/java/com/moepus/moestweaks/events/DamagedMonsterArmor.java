package com.moepus.moestweaks.events;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

public class DamagedMonsterArmor {
    public static void onMobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        if (mob != null && mob.getArmorSlots() != null) {
            for (ItemStack armor : mob.getArmorSlots()) {
                if (armor.isDamageableItem() && armor.getDamageValue() == 0) {
                    int newDurability = armor.getMaxDamage() - Mth.floor(mob.getMaxHealth() / 2 * Mth.randomBetween(mob.getRandom(), 0.75f, 1.3f));
                    armor.setDamageValue(newDurability);
                }
            }
        }
    }
}
