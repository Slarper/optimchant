package slarper.anvilapi.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilScreenHandler.class)
public interface AnvilScreenHandlerAccessor {
    @Accessor("levelCost")
    Property getLevelCost();

    @Accessor("repairItemUsage")
    int getRepairItemUsage();

    @Accessor("repairItemUsage")
    void setRepairItemUsage(int repairItemUsage);
}
