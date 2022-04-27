package slarper.notenoughexp.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {


    @Shadow protected int enchantmentTableSeed;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "addExperienceLevels",at = @At("HEAD"),cancellable = true)
    private void banExpLevelCost(int levels, CallbackInfo ci){
        if (levels < 0){
            ci.cancel();
        }
    }
    @Inject(method = "applyEnchantmentCosts", at = @At("HEAD"),cancellable = true)
    private void banEnchantmentCost(ItemStack enchantedItem, int experienceLevels, CallbackInfo ci){
        this.enchantmentTableSeed = this.random.nextInt();
        ci.cancel();
    }
}
