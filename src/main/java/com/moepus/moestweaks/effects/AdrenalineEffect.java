package com.moepus.moestweaks.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jetbrains.annotations.NotNull;

public class AdrenalineEffect extends MobEffect {
    public AdrenalineEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700); // 0xFFD700 为金色，表示肾上腺素效果
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (player.level().isClientSide)
                return;

            FoodData foodData = player.getFoodData();
            if (foodData.getFoodLevel() > 6 && player.getHealth() <= player.getMaxHealth() * 0.36f) {
                foodData.addExhaustion(7);
                player.heal(1.0F);
            }
        }
    }

    private float absorptionAmount(int amplifier)
    {
        return 2.0f;
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attr, int amplifier) {
        if (entity instanceof Player player) {
            float absorption = player.getAbsorptionAmount();
            if (absorption <= 0)
                player.setAbsorptionAmount(absorptionAmount(amplifier));
        }
        super.addAttributeModifiers(entity, attr, amplifier);
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attr, int amplifier) {
        if (entity instanceof Player player) {
            player.setAbsorptionAmount(absorptionAmount(amplifier) - 2);
        }
        super.removeAttributeModifiers(entity, attr, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 7 == 1;
    }
}
