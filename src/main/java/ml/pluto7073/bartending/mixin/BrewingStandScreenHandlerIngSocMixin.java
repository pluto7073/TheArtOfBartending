package ml.pluto7073.bartending.mixin;

import ml.pluto7073.bartending.alcohol.ItemToAlcoholRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandScreenHandler.IngredientSlot.class)
public class BrewingStandScreenHandlerIngSocMixin {

    @Inject(at = @At("HEAD"), method = "canInsert", cancellable = true)
    public void brewing$overrideInputSlot(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ItemToAlcoholRegistry.get(stack.getItem()) != null) cir.setReturnValue(true);
    }

}
