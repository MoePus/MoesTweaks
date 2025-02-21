package com.moepus.moestweaks.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(VillagerMakeLove.class)
public class VillagerMakeLoveMixin {
    @Inject(method = "breed", at = @At(value = "RETURN", ordinal = 1))
    private void modifySetAge(ServerLevel p_24656_, Villager p_24657_, Villager p_24658_, CallbackInfoReturnable<Optional<Villager>> cir) {
        Optional<Villager> result = cir.getReturnValue();
        if (result.isPresent()) {
            p_24657_.setAge(600);
            p_24658_.setAge(600);
        }
    }
}