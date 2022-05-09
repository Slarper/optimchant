package slarper.anvilapi.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.tag.BlockTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slarper.anvilapi.AnvilTakeCallback;
import slarper.anvilapi.AnvilUpdateCallback;
import slarper.anvilapi.Result;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(
            method = "updateResult",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void onUpdateResult(CallbackInfo ci){
        AnvilScreenHandler handler = (AnvilScreenHandler)(Object)this;
        Result result = AnvilUpdateCallback.EVENT.invoker().onUpdateResult(handler);
        if (result == Result.MATCHED){
            ci.cancel();
        }
    }

    @Inject(
            method = "onTakeOutput",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci){
        AnvilScreenHandler handler = (AnvilScreenHandler)(Object)this;
        Result result = AnvilTakeCallback.EVENT.invoker().onTakeOutput(player, stack, handler);
        if (result == Result.MATCHED){
            if (!player.getAbilities().creativeMode) {
                player.addExperienceLevels(-this.levelCost.get());
            }
            this.levelCost.set(0);
            this.context.run((world, pos) -> {
                BlockState blockState = world.getBlockState(pos);
                if (!player.getAbilities().creativeMode && blockState.isIn(BlockTags.ANVIL) && player.getRandom().nextFloat() < 0.12F) {
                    BlockState blockState2 = AnvilBlock.getLandingState(blockState);
                    if (blockState2 == null) {
                        world.removeBlock(pos, false);
                        world.syncWorldEvent(1029, pos, 0);
                    } else {
                        world.setBlockState(pos, blockState2, 2);
                        world.syncWorldEvent(1030, pos, 0);
                    }
                } else {
                    world.syncWorldEvent(1030, pos, 0);
                }

            });
            this.updateResult();
            ci.cancel();
        }
    }
}
