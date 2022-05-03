package slarper.stackable.mixin;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class MixinItems {
    @Shadow
    private static Item register(Identifier id, Item item) {
        return null;
    }

    @Inject(
            method = "register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void registerEnchantedBook(String id, Item item, CallbackInfoReturnable<Item> cir){
        if (id.equals("enchanted_book")){
            Item enchantedBook = (new EnchantedBookItem((new Item.Settings()).maxCount(64).rarity(Rarity.UNCOMMON)));
            cir.setReturnValue(register(new Identifier(id),enchantedBook));
        }
    }
}
