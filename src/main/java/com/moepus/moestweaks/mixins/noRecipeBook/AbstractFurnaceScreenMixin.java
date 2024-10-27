package com.moepus.moestweaks.mixins.noRecipeBook;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceScreen.class)
public abstract class AbstractFurnaceScreenMixin {
    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractFurnaceScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"
            )
    )
    private GuiEventListener redirectAddRenderableWidget(AbstractFurnaceScreen instance, GuiEventListener guiEventListener) {
        return null;
    }
}
