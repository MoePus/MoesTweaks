package com.moepus.moestweaks.mixins.skipClientCollide;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Level level();

    @Shadow
    public abstract Vec3 position();

    @Shadow
    public int tickCount;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void onMove(MoverType moverType, Vec3 p_19974_, CallbackInfo ci) {
        if (this.level().isClientSide) {
            Entity camera = Minecraft.getInstance().cameraEntity;
            if (camera != null) {
                int tickInterval = (int) Math.floor(camera.distanceToSqr(this.position()) / 512.0) + 1;
                if (tickCount % tickInterval != 0)
                    ci.cancel();
            }
        }
    }

    @Inject(method = "isInWall", at = @At("HEAD"), cancellable = true)
    public void onIsInWall(CallbackInfoReturnable<Boolean> cir) {
        if (this.level().isClientSide) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(
            method = "collide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private List<VoxelShape> onGetEntityCollisions(Level instance, Entity entity, AABB aabb) {
        if (instance.isClientSide) return List.of();
        return instance.getEntityCollisions(entity, aabb);
    }
}
