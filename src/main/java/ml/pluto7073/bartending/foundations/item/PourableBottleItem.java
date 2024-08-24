package ml.pluto7073.bartending.foundations.item;

import ml.pluto7073.bartending.content.sound.BartendingSounds;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.alcohol.AlcDisplayType;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MethodsReturnNonnullByDefault
public class PourableBottleItem extends Item {

    public final Item servingItem, emptyBottleItem;
    public final AlcoholicDrink drink;

    public PourableBottleItem(Item servingItem, Item emptyBottleItem, AlcoholicDrink drink, Properties properties) {
        super(properties);
        this.servingItem = servingItem;
        this.emptyBottleItem = emptyBottleItem;
        this.drink = drink;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return BartendingSounds.LIQUID_POUR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return canPour(user, hand) ? ItemUtils.startUsingInstantly(world, user, hand) :
                InteractionResultHolder.pass(user.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack offHand = livingEntity.getItemInHand(InteractionHand.OFF_HAND);
        if (!offHand.is(Items.GLASS_BOTTLE) || offHand.getCount() != 1) return stack;
        offHand = new ItemStack(servingItem, 1);
        if (stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).contains("Deviation")) {
            offHand.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).putInt("Deviation",
                    stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).getInt("Deviation"));
        }
        stack.hurtAndBreak(1, livingEntity, entity -> {/*TODO Alc bottles drank stat*/});
        livingEntity.setItemInHand(InteractionHand.OFF_HAND, offHand);
        Player player = livingEntity instanceof Player ? (Player) livingEntity : null;
        if (player == null || !player.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(emptyBottleItem, 1);
            }
        }
        livingEntity.gameEvent(GameEvent.FLUID_PLACE);
        return stack;
    }

    public static boolean canPour(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return false;
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        return offHand.is(Items.GLASS_BOTTLE) && offHand.getCount() == 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        int amount = BrewingUtil.getStandardAlcohol(drink);
        amount += BrewingUtil.getAlcoholDeviation(stack);
        AlcoholHandler.INSTANCE.appendTooltip(tooltip, amount, stack, AlcDisplayType.PROOF);
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }
}
