package ml.pluto7073.bartending.foundations.item;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.BartendingStats;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.pdapi.addition.DrinkAddition;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import ml.pluto7073.pdapi.util.DrinkUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

@MethodsReturnNonnullByDefault
public class AlcoholicDrinkItem extends AbstractCustomizableDrinkItem {

    public final int alcohol;
    public final AlcoholicDrink source;
    public final Item bottle;
    public final int sipAmount;

    public AlcoholicDrinkItem(AlcoholicDrink source, Item bottle, Properties settings) {
        super(bottle, Temperature.NORMAL, settings);
        this.source = source;
        this.bottle = bottle;
        alcohol = BrewingUtil.getStandardAlcohol(source);
        sipAmount = source.standardOunces() >= 10 ? 2 : 1;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        Player player = user instanceof Player ? (Player) user : null;
        if (player == null) return stack;

        //stack.hurtAndBreak(sipAmount, player, p -> {});
        player.awardStat(BartendingStats.CONSUME_ALCOHOL.get(),
                (int) Math.ceil(getChemicalContent(AlcoholHandler.INSTANCE.getId(), stack)));


        //if (!stack.isEmpty()) return stack;

        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
        }

        if (!world.isClientSide) {
            DrinkAddition[] additions = DrinkUtil.getAdditionsFromStack(stack);
            for (DrinkAddition addition : additions) {
                addition.onDrink(stack, world, user);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        user.gameEvent(GameEvent.DRINK);

        if (!player.getAbilities().instabuild) {
            return new ItemStack(baseItem);
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public float getChemicalContent(ResourceLocation id, ItemStack stack) {
        return super.getChemicalContent(id, stack) + ("bartending:alcohol".equals(id.toString()) ? alcohol +
                (stack.getOrCreateTagElement(DRINK_DATA_NBT_KEY).contains("Deviation") ?
                        stack.getOrCreateTagElement(DRINK_DATA_NBT_KEY).getInt("Deviation") : 0) : 0);
    }

}
