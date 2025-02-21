package com.moepus.moestweaks.events;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class MonsterWearsArmor {

    @SubscribeEvent
    public static void onMobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        if(mob instanceof Zombie || mob instanceof Skeleton || mob instanceof AbstractIllager) {
            BlockPos levelSpawnPos = mob.level().getSharedSpawnPos();
            double distanceToSpawn = Math.sqrt(levelSpawnPos.distSqr(mob.blockPosition()));
            if(distanceToSpawn < 1000.0) return;
            RandomSource random = event.getLevel().getRandom();

            // Calculate probabilities based on distance
            double armorProbability = Math.min(1.0, distanceToSpawn / 100 / 100);

            // Equip armor based on probabilities
            if (random.nextDouble() < armorProbability) {
                double quality = Math.min(1.0, distanceToSpawn / 12000.0);
                equipArmor(mob, quality, random);
            }
        }
    }

    private static void equipArmor(Mob mob, double quality, RandomSource random) {
        if(mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && random.nextDouble() < 2 * quality) {
            mob.setItemSlot(EquipmentSlot.HEAD, getHelmetByQuality(quality, random));
        }
        quality = quality * quality;
        if(mob.getItemBySlot(EquipmentSlot.FEET).isEmpty() && random.nextDouble() < 2 * quality) {
            mob.setItemSlot(EquipmentSlot.FEET, getBootsByQuality(quality, random));
        }
        quality = quality * quality;
        if(mob.getItemBySlot(EquipmentSlot.LEGS).isEmpty() && random.nextDouble() < 2 * quality) {
            mob.setItemSlot(EquipmentSlot.LEGS, getLeggingsByQuality(quality, random));
        }
        quality = quality * 0.8;
        if(mob.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && random.nextDouble() < 2 * quality) {
            mob.setItemSlot(EquipmentSlot.CHEST, getChestplateByQuality(quality, random));
        }
    }

    private static ItemStack getHelmetByQuality(double quality, RandomSource random) {
        if (quality > 0.66) {
            return new ItemStack(Items.IRON_HELMET);
        }
        if (quality > 0.33) {
            return new ItemStack(Items.CHAINMAIL_HELMET);
        }
        return new ItemStack(Items.LEATHER_HELMET);
    }

    private static ItemStack getChestplateByQuality(double quality, RandomSource random) {
        if (quality > 0.66) {
            return new ItemStack(Items.IRON_CHESTPLATE);
        }
        if (quality > 0.33) {
            return new ItemStack(Items.CHAINMAIL_CHESTPLATE);
        }
        return new ItemStack(Items.LEATHER_CHESTPLATE);
    }

    private static ItemStack getLeggingsByQuality(double quality, RandomSource random) {
        if (quality > 0.66) {
            return new ItemStack(Items.IRON_LEGGINGS);
        }
        if (quality > 0.33) {
            return new ItemStack(Items.CHAINMAIL_LEGGINGS);
        }
        return new ItemStack(Items.LEATHER_LEGGINGS);
    }

    private static ItemStack getBootsByQuality(double quality, RandomSource random) {
        if (quality > 0.66) {
            return new ItemStack(Items.IRON_BOOTS);
        }
        if (quality > 0.33) {
            return new ItemStack(Items.CHAINMAIL_BOOTS);
        }
        return new ItemStack(Items.LEATHER_BOOTS);
    }
}