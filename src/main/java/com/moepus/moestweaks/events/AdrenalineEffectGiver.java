package com.moepus.moestweaks.events;

import com.moepus.moestweaks.effects.EffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AdrenalineEffectGiver {
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            Holder<DamageType> dmgType = event.getSource().typeHolder();
            if(!dmgType.is(DamageTypes.MOB_ATTACK) && !dmgType.is(DamageTypes.MOB_PROJECTILE) && !dmgType.is(DamageTypes.MOB_ATTACK_NO_AGGRO) && !dmgType.is(DamageTypes.EXPLOSION)&& !dmgType.is(DamageTypes.MAGIC))
                return;

            if (player.isAlive() && player.getHealth() - event.getAmount() <= 1.5F && !player.hasEffect(EffectRegistry.ADRENALINE.get())) {
                player.addEffect(new MobEffectInstance(EffectRegistry.ADRENALINE.get(), 70));
            }
        }
    }
}
