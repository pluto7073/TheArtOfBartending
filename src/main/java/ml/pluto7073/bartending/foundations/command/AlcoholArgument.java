package ml.pluto7073.bartending.foundations.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import ml.pluto7073.bartending.foundations.BartendingRegistries;
import ml.pluto7073.bartending.foundations.alcohol.AlcoholicDrink;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class AlcoholArgument implements ArgumentType<AlcoholicDrink> {

    private final HolderLookup<AlcoholicDrink> lookup;

    public AlcoholArgument(CommandBuildContext context) {
        lookup = context.holderLookup(BartendingRegistries.ALCOHOLIC_DRINK_KEY);
    }

    public static AlcoholArgument drink(CommandBuildContext context) {
        return new AlcoholArgument(context);
    }

    public static <S> AlcoholicDrink getDrink(CommandContext<S> context, String name) {
        return BartendingRegistries.ALCOHOLIC_DRINK.get(context.getArgument(name, ResourceLocation.class));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(lookup.listElementIds().map(ResourceKey::location), builder);
    }

    @Override
    public AlcoholicDrink parse(StringReader reader) throws CommandSyntaxException {
        return BartendingRegistries.ALCOHOLIC_DRINK.get(ResourceLocation.read(reader));
    }
}
