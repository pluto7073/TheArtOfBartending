package ml.pluto7073.bartending.foundations.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class TAOBCommands {

    public static LiteralArgumentBuilder<CommandSourceStack> alcohol() {
        return literal("alcohol").then(alcoholGet());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> alcoholGet() {
        return literal("get")
                .requires(source -> source.hasPermission(2))
                .then(argument("target", EntityArgument.player())
                        .executes(ctx -> {
                            ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
                            float bac = BrewingUtil.calculateBAC(AlcoholHandler.INSTANCE.get(target));
                            if (bac < 0.01f) bac = 0.00f;
                            String s = String.valueOf(bac);
                            s = s.length() >= 4 ? s.substring(0, 4) : s;
                            TheArtOfBartending.LOGGER.info(s);
                            String finalS = s;
                            ctx.getSource().sendSuccess(() -> Component.translatable("command.drink.alcohol.get", finalS), true);
                            return 1;
                        }));
    }

}
