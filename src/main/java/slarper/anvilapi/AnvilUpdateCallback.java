package slarper.anvilapi;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.screen.AnvilScreenHandler;

@FunctionalInterface
public interface AnvilUpdateCallback {

    Event<AnvilUpdateCallback> EVENT = EventFactory.createArrayBacked(
            AnvilUpdateCallback.class,
            listeners ->
                    handler -> {
                        for (AnvilUpdateCallback event : listeners){
                            Result result = event.onUpdateResult(handler);
                            if (result == Result.MATCHED){
                                return result;
                            }
                        }
                        return  Result.PASS;
                    }
    );

    Result onUpdateResult(AnvilScreenHandler handler);
}
