package slarper.constantcost.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public class MixinAnvilScreenHandler {
    @Inject(method = "getNextCost", at = @At("HEAD"),cancellable = true)
    private static void constantCost(int cost, CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(cost);
    }
}
