package ml.pluto7073.bartending.foundations.alcohol;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.client.TheArtOfClient;
import ml.pluto7073.bartending.content.entity.effect.BartendingMobEffects;
import ml.pluto7073.bartending.foundations.command.BartendingCommands;
import ml.pluto7073.bartending.foundations.item.AlcoholicDrinkItem;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.bartending.foundations.item.PourableBottleItem;
import ml.pluto7073.chemicals.Chemicals;
import ml.pluto7073.chemicals.handlers.HalfLifeChemicalHandler;
import ml.pluto7073.pdapi.item.PDItems;
import ml.pluto7073.pdapi.util.DrinkUtil;
import ml.pluto7073.pdapi.item.AbstractCustomizableDrinkItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AlcoholHandler extends HalfLifeChemicalHandler {

    public static final int ALCOHOL_HALF_LIFE_TICKS = 4500;
    public static final AlcoholHandler INSTANCE = new AlcoholHandler(ALCOHOL_HALF_LIFE_TICKS);
    public static final StatFormatter ALCOHOL_FORMATTER = value -> {
        AlcDisplayType type = AlcDisplayType.GRAMS;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            type = TheArtOfClient.config().getAlcoholDisplayType();
            if (type == AlcDisplayType.PROOF) type = AlcDisplayType.OUNCES;
        }
        float amount = BrewingUtil.convertType(value, AlcDisplayType.GRAMS, type);
        return type.format(amount);
    };

    public AlcoholHandler(int halfLifeTicks) {
        super(halfLifeTicks);
    }

    @Override
    public Collection<MobEffectInstance> getEffectsForAmount(float amount, Level level) {
        float bac = BrewingUtil.calculateBAC(amount);
        List<MobEffectInstance> list = new ArrayList<>();
        if (bac >= 0.01f) {
            list.add(new MobEffectInstance(MobEffects.WEAKNESS, 30 * 20, 0, true, true));
        }
        if (bac > 0.05f) {
            list.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 0, true, true));
        }
        if (bac > 0.11f) { // Blackouts here
            list.add(new MobEffectInstance(MobEffects.CONFUSION, 30 * 20, 0, true, true));
            list.add(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30 * 20, 0, true, true));
            list.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 1, true, true));
        }
        if (bac > 0.21f) {
            list.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30 * 20, 2, true, true));
            list.add(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30 * 20, 1, true, true));
        }
        if (bac > 0.35f) {
            list.add(new MobEffectInstance(BartendingMobEffects.ALCOHOL_POISON, 30 * 20, 0, true, true));
        }
        return list;
    }

    @Override
    public void appendTooltip(List<Component> tooltip, float amount, ItemStack stack) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            appendTooltip(tooltip, amount, stack, AlcDisplayType.GRAMS);
            return;
        }
        appendTooltip(tooltip, amount, stack, TheArtOfClient.config().getAlcoholDisplayType());
    }

    public void appendTooltip(List<Component> tooltip, float amount, ItemStack stack, AlcDisplayType display) {
        if (stack.getItem() instanceof AlcoholicDrinkItem item) {
            amount = item.getBaseChemicalContent(getId(), stack);
        }
        if (amount <= 0) return;
        if (DrinkUtil.getAdditionsFromStack(stack).length > 0 || stack.is(PDItems.SPECIALTY_DRINK)) {
            tooltip.add(Component.translatable("tooltip.bartending.alcohol_content", amount + "g").withStyle(ChatFormatting.GOLD));
            return;
        }
        String content = switch (display) {
            case GRAMS -> amount + "g";
            case ML -> BrewingUtil.convertType(amount, AlcDisplayType.GRAMS, AlcDisplayType.ML) + "mL";
            case OUNCES -> BrewingUtil.convertType(amount, AlcDisplayType.GRAMS, AlcDisplayType.OUNCES) + "oz";
            case PROOF -> {
                if (!stack.getOrCreateTagElement(AbstractCustomizableDrinkItem.DRINK_DATA_NBT_KEY).contains("Deviation")) {
                    yield stack.getItem() instanceof AlcoholicDrinkItem drink ? drink.source.standardProof() + "" :
                            stack.getItem() instanceof PourableBottleItem pourable ? pourable.drink.standardProof() + "" : "";
                }
                amount += BrewingUtil.getAlcoholDeviation(stack);
                if (stack.getItem() instanceof PourableBottleItem pourable) {
                    AlcoholicDrink drink = pourable.drink;
                    yield BrewingUtil.getProof(drink, amount) + "";
                } else if (stack.getItem() instanceof AlcoholicDrinkItem drink) {
                    AlcoholicDrink alcDrink = drink.source;
                    yield BrewingUtil.getProof(alcDrink, amount) + "";
                }
                yield "";
            }
        };
        if (display == AlcDisplayType.PROOF) {
            tooltip.add(Component.translatable("tooltip.bartending.proof", content).withStyle(ChatFormatting.GOLD));
            return;
        }
        tooltip.add(Component.translatable("tooltip.bartending.alcohol_content", content).withStyle(ChatFormatting.GOLD));
    }

    @Override
    public @Nullable LiteralArgumentBuilder<CommandSourceStack> createCustomChemicalCommandExtension() {
        return BartendingCommands.alcohol();
    }

    public static void init() {
        Registry.register(Chemicals.REGISTRY, TheArtOfBartending.asId("alcohol"), INSTANCE);
    }

}
