package ml.pluto7073.bartending.foundations.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import ml.pluto7073.bartending.foundations.step.BrewerStep;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import ml.pluto7073.chemicals.handlers.HalfLifeChemicalHandler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class BartendingCommands {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) -> {
            dispatcher.register(concoction(context));
        });
    }

    private static LiteralArgumentBuilder<CommandSourceStack> concoction(CommandBuildContext context) {
        return literal("concoction").requires(source -> source.hasPermission(2)).requires(CommandSourceStack::isPlayer)
                .then(argument("target", EntityArgument.player())
                .then(argument("drink", AlcoholArgument.drink(context))
                        .executes(ctx -> {
                            CommandSourceStack stack = ctx.getSource();
                            ItemStack concoction = new ItemStack(BartendingItems.CONCOCTION);
                            ListTag tag = new ListTag();
                            AlcoholicDrink drink = AlcoholArgument.getDrink(ctx, "drink");
                            for (BrewerStep step : drink.steps()) {
                                CompoundTag data = new CompoundTag();
                                data.putString("type", step.id());
                                step.createExactMatchData(data);
                                tag.add(data);
                            }
                            concoction.getOrCreateTag().put("BrewingSteps", tag);
                            ServerPlayer serverPlayer = EntityArgument.getPlayer(ctx, "target");
                            serverPlayer.getInventory().add(concoction);
                            stack.sendSuccess(() ->
                                    Component.translatable("command.concoction.response", serverPlayer.getName())
                                            .append(Component.translatable(drink.getLanguageKey())), true);
                            return 1;
                        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> alcohol(HalfLifeChemicalHandler handler) {
        return literal(handler.getId().toString()).then(alcoholGet(handler)).then(alcoholSet(handler));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> alcoholGet(HalfLifeChemicalHandler handler) {
        return literal("get")
                .requires(source -> source.hasPermission(2))
                .then(argument("target", EntityArgument.player())
                        .executes(ctx -> {
                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
                            float bac = BrewingUtil.calculateBAC(handler.get(target));
                            if (bac < 0.01f) bac = 0.00f;
                            String s = String.valueOf(bac);
                            s = s.length() >= 4 ? s.substring(0, 4) : s;
                            final String finalS = s + "%";
                            ctx.getSource().sendSuccess(() -> Component.translatable("command.drink.alcohol.get",
                                    target.getDisplayName(), finalS), true);
                            return 1;
                        }));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> alcoholSet(HalfLifeChemicalHandler handler) {
        return literal("set")
                .requires(source -> source.hasPermission(2))
                .then(argument("target", EntityArgument.player())
                .then(argument("bac", FloatArgumentType.floatArg(0))
                        .executes(ctx -> {
                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
                            float bac = FloatArgumentType.getFloat(ctx, "bac");
                            float grams = BrewingUtil.gramsFromBAC(bac);
                            handler.set(target, grams);
                            ctx.getSource().sendSuccess(() -> Component.translatable("command.drink.alcohol.set",
                                    target.getDisplayName(), bac), true);
                            return 1;
                        })));
    }

}
