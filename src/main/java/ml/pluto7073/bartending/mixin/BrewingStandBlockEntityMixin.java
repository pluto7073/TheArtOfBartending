package ml.pluto7073.bartending.mixin;

import ml.pluto7073.bartending.BrewingUtil;
import ml.pluto7073.bartending.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.alcohol.ItemToAlcoholRegistry;
import ml.pluto7073.bartending.item.TAOBItems;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {

    @Shadow @Final private static int INPUT_SLOT_INDEX;

    @Inject(at = @At("HEAD"), method = "canCraft", cancellable = true)
    private static void bartending$canCraftBrewingItem(DefaultedList<ItemStack> slots, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = slots.get(INPUT_SLOT_INDEX);

        for (int i = 0; i < 3; i++) {
            ItemStack base = slots.get(i);
            if (base.isOf(Items.POTION)) {
                if (!Potions.WATER.equals(PotionUtil.getPotion(base))) return;
            } else {
                return;
            }
        }

        if (ItemToAlcoholRegistry.get(stack.getItem()) != null) cir.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "craft")
    private static void bartending$craftUnfinalizedAlcDrink(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
        ItemStack input = slots.get(INPUT_SLOT_INDEX);

        for (int i = 0; i < 3; i++) {
            ItemStack base = slots.get(i);
            if (base.isOf(Items.POTION) && Potions.WATER.equals(PotionUtil.getPotion(base))) {
                base = BrewingUtil.setBaseUnfinalizedData(new ItemStack(TAOBItems.UNFINALIZED_ALCOHOLIC_DRINK_ITEM), input.getItem());
                slots.set(i, base);
            }
        }
    }

}
