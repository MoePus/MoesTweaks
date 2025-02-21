package com.moepus.moestweaks.mixins;

import com.moepus.moestweaks.ConfigParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Mob {
    @Unique
    final boolean piglinWearNetherite = ConfigParser.getConfig().piglinWearNetherite;

    @Shadow
    public abstract void setImmuneToZombification(boolean pImmuneToZombification);

    @Shadow
    protected abstract boolean isImmuneToZombification();

    @Shadow
    public abstract boolean isAdult();

    @Shadow
    protected int timeInOverworld;

    protected AbstractPiglinMixin(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void inject_defineSynchedData(CallbackInfo ci) {
        setImmuneToZombification(true);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL")) // Structure Mobs
    public void readAdditionalSaveData(CallbackInfo ci) {
        setImmuneToZombification(true);
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    protected void inject_customServerAiStep(CallbackInfo ci) {
        if (isImmuneToZombification() && hasEffect(MobEffects.WEAKNESS))
            setImmuneToZombification(false);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (piglinWearNetherite && isAdult()) {
            RandomSource random = pLevel.getRandom();
            int rand = random.nextInt(8);

            switch (rand) {
                case 0 -> {
                    if (getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                        ItemStack item = new ItemStack(Items.NETHERITE_HELMET);
                        item.setDamageValue(item.getMaxDamage() - random.nextInt(2, 100));
                        setItemSlot(EquipmentSlot.HEAD, item);
                        setDropChance(EquipmentSlot.HEAD, 0.0f);
                    }
                }
                case 1 -> {
                    if (getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                        ItemStack item = new ItemStack(Items.NETHERITE_CHESTPLATE);
                        item.setDamageValue(item.getMaxDamage() - random.nextInt(2, 100));
                        setItemSlot(EquipmentSlot.CHEST, item);
                        setDropChance(EquipmentSlot.CHEST, 0.0f);
                    }
                }
                case 2 -> {
                    if (getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
                        ItemStack item = new ItemStack(Items.NETHERITE_LEGGINGS);
                        item.setDamageValue(item.getMaxDamage() - random.nextInt(2, 100));
                        setItemSlot(EquipmentSlot.LEGS, item);
                        setDropChance(EquipmentSlot.LEGS, 0.0f);
                    }
                }
                case 3 -> {
                    if (getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
                        ItemStack item = new ItemStack(Items.NETHERITE_BOOTS);
                        item.setDamageValue(item.getMaxDamage() - random.nextInt(2, 100));
                        setItemSlot(EquipmentSlot.FEET, item);
                        setDropChance(EquipmentSlot.FEET, 0.0f);
                    }
                }
                case 4 -> {
                    if (getItemBySlot(EquipmentSlot.MAINHAND).is(Items.GOLDEN_AXE)) {
                        ItemStack item = new ItemStack(Items.NETHERITE_AXE);
                        item.setDamageValue(item.getMaxDamage() - random.nextInt(100, 200));
                        setItemSlot(EquipmentSlot.MAINHAND, item);
                        setDropChance(EquipmentSlot.MAINHAND, 0.0f);
                    } else if (getItemBySlot(EquipmentSlot.MAINHAND).is(Items.GOLDEN_SWORD)) {
                        ItemStack item = new ItemStack(Items.NETHERITE_SWORD);
                        item.setDamageValue(item.getMaxDamage() - random.nextInt(100, 200));
                        setItemSlot(EquipmentSlot.MAINHAND, item);
                        setDropChance(EquipmentSlot.MAINHAND, 0.0f);
                    }
                }
            }
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}
