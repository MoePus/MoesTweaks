package com.moepus.moestweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AllTags {
    public static final TagKey<Item> DEATH_KEEP_TAG = ItemTags.create(new ResourceLocation(MoesTweaks.MODID, "death_keep"));

    public static boolean hasDeathKeepTag(ItemStack itemStack) {
        return itemStack.is(DEATH_KEEP_TAG);
    }

}
