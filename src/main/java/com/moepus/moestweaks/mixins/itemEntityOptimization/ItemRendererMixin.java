package com.moepus.moestweaks.mixins.itemEntityOptimization;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Final
    @Shadow
    private ItemColors itemColors;

    @Unique
    Vector3f moesTweaks$cameraDirection = new Vector3f(0, 0, -1);

    @Unique
    void moesTweaks$putBulkData(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f transferred_normal, BakedQuad bakedQuad, float r, float g, float b, int packedLight, int packedOverlay) {
        int[] vertices = bakedQuad.getVertices();
        Matrix4f pose_matrix = pose.pose();

        final int count = vertices.length / 8;
        try (MemoryStack memorystack = MemoryStack.stackPush()) {
            ByteBuffer bytebuffer = memorystack.malloc(32); // hardcode DefaultVertexFormat.BLOCK.getVertexSize()
            IntBuffer intbuffer = bytebuffer.asIntBuffer();
            Vector3f position = new Vector3f();
            final float scaled_r = r / 255.0F;
            final float scaled_g = g / 255.0F;
            final float scaled_b = b / 255.0F;
            final float scaled_a = 1.0F / 255.0F;

            for (int i = 0; i < count; ++i) {
                intbuffer.clear();
                intbuffer.put(vertices, i * 8, 8);

                float vertex_r = scaled_r * (float) (bytebuffer.get(12) & 255);
                float vertex_g = scaled_g * (float) (bytebuffer.get(13) & 255);
                float vertex_b = scaled_b * (float) (bytebuffer.get(14) & 255);
                float vertex_a = scaled_a * (float) (bytebuffer.get(15) & 255);

                int l = vertexConsumer.applyBakedLighting(packedLight, bytebuffer);
                float u = bytebuffer.getFloat(16);
                float v = bytebuffer.getFloat(20);
                vertexConsumer.applyBakedNormals(transferred_normal, bytebuffer, pose.normal());
                position.set(bytebuffer.getFloat(0), bytebuffer.getFloat(4), bytebuffer.getFloat(8)).mulPosition(pose_matrix);
                vertexConsumer.vertex(position.x(), position.y(), position.z(), vertex_r, vertex_g, vertex_b, vertex_a, u, v, packedOverlay, l, transferred_normal.x(), transferred_normal.y(), transferred_normal.z());
            }
        }
    }

    @Unique
    public void moesTweaks$renderQuadList(PoseStack.Pose pose, Vector3f view, VertexConsumer vertexConsumer, List<BakedQuad> bakedQuads, ItemStack itemStack, int packedLight, int packedOverlay) {
        boolean isNotEmpty = !itemStack.isEmpty();

        int lastTintIndex = -1;
        float lastr = 1.0F, lastg = 1.0F, lastb = 1.0F;
        Vector3f normal = new Vector3f();
        for (BakedQuad bakedquad : bakedQuads) {
            if (view.z < 0) {
                Vec3i face_normal = bakedquad.getDirection().getNormal();
                normal.set((float) face_normal.getX(), (float) face_normal.getY(), (float) face_normal.getZ());
                // function storage translates pose but not normal. so...
                normal.mulDirection(pose.pose());
                if (normal.dot(view) > 0.1F) {
                    continue;
                }
            }

            final int tintIndex = bakedquad.getTintIndex();
            if (isNotEmpty && tintIndex != -1) {
                float r = lastr;
                float g = lastg;
                float b = lastb;
                if (tintIndex != lastTintIndex) {
                    int i = this.itemColors.getColor(itemStack, tintIndex);
                    lastr = r = (float) (i >> 16 & 255) / 255.0F;
                    lastg = g = (float) (i >> 8 & 255) / 255.0F;
                    lastb = b = (float) (i & 255) / 255.0F;
                    lastTintIndex = tintIndex;
                }
                moesTweaks$putBulkData(vertexConsumer, pose, normal, bakedquad, r, g, b, packedLight, packedOverlay);
            } else {
                moesTweaks$putBulkData(vertexConsumer, pose, normal, bakedquad, 1.0F, 1.0F, 1.0F, packedLight, packedOverlay);
            }
        }
    }

    @Inject(method = "renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V", at = @At("HEAD"), cancellable = true)
    public void renderModelLists(BakedModel model, ItemStack itemStack, int packedLight, int packedOverlay, PoseStack poseStack, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if (model.getClass() == SimpleBakedModel.class) {
            PoseStack.Pose pose = poseStack.last();
            Matrix4f _pose = pose.pose();
            Vector3f view = new Vector3f(_pose.m30(), _pose.m31(), _pose.m32()).normalize();
            for (Direction direction : Direction.values()) {
                this.moesTweaks$renderQuadList(pose, view, vertexConsumer, model.getQuads(null, direction, null), itemStack, packedLight, packedOverlay);
            }
            this.moesTweaks$renderQuadList(pose, view, vertexConsumer, model.getQuads(null, null, null), itemStack, packedLight, packedOverlay);
            ci.cancel();
        }
    }
}
