package slarper.anvilapi;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;

@FunctionalInterface
public interface AnvilTakeCallback {

    Event<AnvilTakeCallback> EVENT = EventFactory.createArrayBacked(
            AnvilTakeCallback.class,
            listeners ->
                    (player, stack, handler) -> {
                        for (AnvilTakeCallback event : listeners){
                            Result result = event.onTakeOutput(player, stack, handler);
                            if (result == Result.MATCHED){
                                return result;
                            }
                        }
                        return Result.PASS;
                    }
    );

    Result onTakeOutput(PlayerEntity player, ItemStack stack, AnvilScreenHandler handler);
}
