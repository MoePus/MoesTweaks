package com.moepus.moestweaks.mixins.modelPartOptimization;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SpriteCoordinateExpander;
import org.joml.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.Cube.class)
public abstract class CubeMixin {
    @Final
    @Shadow
    private ModelPart.Polygon[] polygons;

    @Inject(method = "compile", at = @At("HEAD"), cancellable = true)
    public void renderModelLists(PoseStack.Pose pose, VertexConsumer vertexConsumer, int uv2, int overlayCoords, float r, float g, float b, float a, CallbackInfo ci) {
        if (vertexConsumer instanceof SpriteCoordinateExpander) {
            return;
        }
        Matrix4f matrix4f = pose.pose();
        if (matrix4f.m32() > 0) return;
        Matrix3f matrix3f = pose.normal();
        Vector3f centre = new Vector3f(matrix4f.m30(), matrix4f.m31(), matrix4f.m32()).normalize();

        for (ModelPart.Polygon modelpart$polygon : this.polygons) {
            Vector3f normal = matrix3f.transform(new Vector3f(modelpart$polygon.normal));
            if (normal.dot(centre) > 0.1) {
                continue;
            }
            float f = normal.x();
            float f1 = normal.y();
            float f2 = normal.z();

            for (ModelPart.Vertex modelpart$vertex : modelpart$polygon.vertices) {
                Vector3f pos = matrix4f.transformPosition(new Vector3f(modelpart$vertex.pos).div(16.0F));
                vertexConsumer.vertex(pos.x(), pos.y(), pos.z(), r, g, b, a, modelpart$vertex.u, modelpart$vertex.v, overlayCoords, uv2, f, f1, f2);
            }
        }
        ci.cancel();
    }
}
