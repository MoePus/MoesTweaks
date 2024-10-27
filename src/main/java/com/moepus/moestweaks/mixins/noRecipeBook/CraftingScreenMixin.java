package com.moepus.moestweaks.mixins.noRecipeBook;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingScreen.class)
public abstract class CraftingScreenMixin {
    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/CraftingScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener redirectAddRenderableWidget(CraftingScreen instance, GuiEventListener guiEventListener) {
        return null;
    }
}
