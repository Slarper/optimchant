package slarper.rubbing.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.tag.BlockTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class MixinAnvilScreenHandler extends ForgingScreenHandler {
    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    public MixinAnvilScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At("HEAD"),cancellable = true)
    private void rubbing(CallbackInfo ci){
        ItemStack input0 = this.input.getStack(0);
        ItemStack input1 = this.input.getStack(1);
        if (input0.getItem() == Items.BOOK && input1.getItem() == Items.ENCHANTED_BOOK){
            this.levelCost.set(1);
            this.repairItemUsage = 0;
            this.output.setStack(0, input1.copy());
            this.sendContentUpdates();
            ci.cancel();
        }
    }

    @Inject(method = "onTakeOutput",at = @At("HEAD"),cancellable = true)
    private void onTakeOutput2(PlayerEntity player, ItemStack stack, CallbackInfo ci){
        ItemStack input0 = this.input.getStack(0);
        ItemStack input1 = this.input.getStack(1);
        if (input0.getItem() == Items.BOOK && input1.getItem() == Items.ENCHANTED_BOOK){
            if (!player.getAbilities().creativeMode) {
                player.addExperienceLevels(-this.levelCost.get());
            }

            if (!input0.isEmpty()){
                input0.decrement(1);
                this.input.setStack(0,input0);
            } else {
                this.input.setStack(0, ItemStack.EMPTY);
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
