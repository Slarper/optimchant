package slarper.stackable;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import slarper.anvilapi.AnvilUpdateCallback;
import slarper.anvilapi.Result;
import slarper.anvilapi.mixin.AnvilScreenHandlerAccessor;
import slarper.anvilapi.mixin.ForgingScreenHandlerAccessor;
import slarper.stackable.mixin.ItemAccessor;

public class Stackable implements ModInitializer, AnvilUpdateCallback {
    @Override
    public void onInitialize() {
        ((ItemAccessor)Items.ENCHANTED_BOOK).setMaxCount(64);
        AnvilUpdateCallback.EVENT.register(this);
    }

    @Override
    public Result onUpdateResult(AnvilScreenHandler handler) {
        ItemStack input1 = ((ForgingScreenHandlerAccessor)handler).getInput().getStack(1);
        if (input1.getItem() == Items.ENCHANTED_BOOK){
            ((AnvilScreenHandlerAccessor)handler).setRepairItemUsage(1);
        }
        return Result.PASS;
    }
}
