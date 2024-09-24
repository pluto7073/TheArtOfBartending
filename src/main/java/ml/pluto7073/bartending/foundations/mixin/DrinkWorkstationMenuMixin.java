package ml.pluto7073.bartending.foundations.mixin;

import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.pdapi.client.gui.DrinkWorkstationMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DrinkWorkstationMenu.class)
public class DrinkWorkstationMenuMixin {

    @Inject(at = @At("RETURN"), method = "isValidBlock", cancellable = true)
    private void bartending$TestValidCounter(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) cir.setReturnValue(state.is(BartendingBlocks.COUNTER_TOP));
    }

}
