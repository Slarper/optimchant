package slarper.rubbing;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import slarper.anvilapi.AnvilTakeCallback;
import slarper.anvilapi.AnvilUpdateCallback;
import slarper.anvilapi.Result;
import slarper.anvilapi.mixin.AnvilScreenHandlerAccessor;
import slarper.anvilapi.mixin.ForgingScreenHandlerAccessor;

public class Rubbing implements ModInitializer, AnvilUpdateCallback, AnvilTakeCallback {
    @Override
    public void onInitialize() {
        AnvilTakeCallback.EVENT.register(this);
        AnvilUpdateCallback.EVENT.register(this);
    }

    @Override
    public Result onTakeOutput(PlayerEntity player, ItemStack stack, AnvilScreenHandler handler) {
        ItemStack input0 = ((ForgingScreenHandlerAccessor)handler).getInput().getStack(0);
        ItemStack input1 = ((ForgingScreenHandlerAccessor)handler).getInput().getStack(1);
        if (input0.getItem() == Items.BOOK && input1.getItem() == Items.ENCHANTED_BOOK){
            if (!input0.isEmpty() && input0.getCount() > 1){
                input0.decrement(1);
                ((ForgingScreenHandlerAccessor)handler).getInput().setStack(0,input0);
            } else {
                ((ForgingScreenHandlerAccessor)handler).getInput().setStack(0, ItemStack.EMPTY);
            }
            return Result.MATCHED;
        }
        return Result.PASS;
    }

    @Override
    public Result onUpdateResult(AnvilScreenHandler handler) {
        ItemStack input0 = ((ForgingScreenHandlerAccessor)handler).getInput().getStack(0);
        ItemStack input1 = ((ForgingScreenHandlerAccessor)handler).getInput().getStack(1);
        if (input0.getItem() == Items.BOOK && input1.getItem() == Items.ENCHANTED_BOOK){
            ((AnvilScreenHandlerAccessor)handler).getLevelCost().set(1);
            ((AnvilScreenHandlerAccessor)handler).setRepairItemUsage(0);
            ItemStack output = input1.copy();
            output.setCount(1);
            ((ForgingScreenHandlerAccessor)handler).getOutput().setStack(0,output);
            handler.sendContentUpdates();
            return Result.MATCHED;
        } else {
            return Result.PASS;
        }
    }
}
