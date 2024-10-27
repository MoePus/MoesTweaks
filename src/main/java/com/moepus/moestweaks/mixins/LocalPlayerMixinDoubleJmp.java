package com.moepus.moestweaks.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixinDoubleJmp extends AbstractClientPlayer {
    @Shadow
    public Input input;
    @Unique
    private int extrajumpCount = 0;
    @Unique
    private final int totalExtraJumpCount = 1;

    @Unique
    private boolean lastJumpInput = false;

    public LocalPlayerMixinDoubleJmp(ClientLevel p_250460_, GameProfile p_249912_) {
        super(p_250460_, p_249912_);
    }

    @Override
    public void jumpFromGround()
    {
        super.jumpFromGround();
        lastJumpInput = true;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if(getAbilities().flying)
        {
            extrajumpCount = totalExtraJumpCount;
            lastJumpInput = true;
            return;
        }

        if (!input.jumping) {
            lastJumpInput = false;
            if (onGround()) {
                extrajumpCount = totalExtraJumpCount;
            }
            return;
        }

        if (onGround()) return;

        if (lastJumpInput) return;

        if (extrajumpCount == 0 || foodData.getFoodLevel() <= 2) return;

        jumpFromGround();
        extrajumpCount--;
        resetFallDistance();
    }
}
