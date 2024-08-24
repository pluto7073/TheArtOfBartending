package ml.pluto7073.bartending.foundations.mixin;

import ml.pluto7073.bartending.content.enchantment.AlcoholEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WitherRoseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherRoseBlock.class)
public class WitherRoseBlockMixin {

    @Inject(at = @At("HEAD"), method = "entityInside", cancellable = true)
    public void bartending$NegateDamageAndSlownessForVineyardWalker(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player && EnchantmentHelper.getEnchantments(player.getItemBySlot(EquipmentSlot.LEGS))
                .containsKey(AlcoholEnchantments.VINEYARD_WALKER)) {
            ci.cancel();
        }
        if (entity instanceof Mob mob && EnchantmentHelper.getEnchantments(mob.getItemBySlot(EquipmentSlot.LEGS))
                .containsKey(AlcoholEnchantments.VINEYARD_WALKER)) {
            ci.cancel();
        }
    }

}
