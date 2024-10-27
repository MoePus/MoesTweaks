package com.moepus.moestweaks.mixins;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hoglin.class)
public abstract class HoglinMixin extends Mob {
    @Shadow
    public abstract void setImmuneToZombification(boolean pImmuneToZombification);

    @Shadow
    protected abstract boolean isImmuneToZombification();

    @Shadow
    protected int timeInOverworld;

    protected HoglinMixin(EntityType<? extends Mob> pEntityType, Level pLevel) {
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
}
