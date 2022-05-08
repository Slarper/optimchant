package slarper.stackable;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import slarper.stackable.mixin.ItemAccessor;

public class Stackable implements ModInitializer {
    @Override
    public void onInitialize() {
        ((ItemAccessor)Items.ENCHANTED_BOOK).setMaxCount(64);
    }
}
