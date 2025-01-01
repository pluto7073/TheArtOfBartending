package ml.pluto7073.bartending.compat.rei.category;

import com.mojang.datafixers.util.Pair;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.bartending.client.gui.BoilerScreen;
import ml.pluto7073.bartending.compat.rei.TextWidget;
import ml.pluto7073.bartending.compat.rei.TheArtOfREI;
import ml.pluto7073.bartending.compat.rei.display.AlcoholBrewingDisplay;
import ml.pluto7073.bartending.content.block.BartendingBlocks;
import ml.pluto7073.bartending.content.item.BartendingItems;
import ml.pluto7073.bartending.foundations.step.BoilingBrewerStep;
import ml.pluto7073.bartending.foundations.step.BrewerStep;
import ml.pluto7073.bartending.foundations.step.DistillingBrewerStep;
import ml.pluto7073.bartending.foundations.step.FermentingBrewerStep;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlcoholBrewingCategory implements DisplayCategory<AlcoholBrewingDisplay> {
    @Override
    public CategoryIdentifier<? extends AlcoholBrewingDisplay> getCategoryIdentifier() {
        return TheArtOfREI.ALCOHOL_BREWING;
    }

    @Override
    public List<Widget> setupDisplay(AlcoholBrewingDisplay display, Rectangle bounds) {
        ArrayList<Widget> widgets = new ArrayList<>();
        int y = bounds.y - 32;

        widgets.add(Widgets.createRecipeBase(bounds));

        for (BrewerStep step : display.drink.steps()) {
            if (step instanceof BoilingBrewerStep boiling) {
                setupBoilingDisplay(widgets, y += 40, boiling, bounds);
            } else if (step instanceof FermentingBrewerStep fermenting) {
                setupFermentingDisplay(widgets, y += 52, fermenting, bounds);
            } else if (step instanceof DistillingBrewerStep distilling) {
                setupDistillingDisplay(widgets, y += 40, distilling, bounds);
            }
        }

        widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.getWidth() - 64, bounds.getMaxY() - 32))
                .markInput().entry(EntryStacks.of(display.drink.bottle())));
        widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + bounds.getWidth() - 32, bounds.getMaxY() - 32)));
        widgets.add(Widgets.createSlot(new Point(bounds.x + bounds.getWidth() - 32, bounds.getMaxY() - 32))
                .markOutput().disableBackground().entries(display.getOutputEntries().get(0)));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return DisplayCategory.super.getDisplayHeight() * 2 + 64;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.bartending.brewing");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BartendingItems.RED_WINE);
    }

    private static void setupFermentingDisplay(ArrayList<Widget> widgets, int baseY, FermentingBrewerStep step, Rectangle bounds) {
        widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, baseY - 12, bounds.width, 52)));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 8, baseY + 6))
                .entries(EntryIngredients.ofIngredient(step.predicate.asIngredient())).markInput());
        widgets.add(new TextWidget(Component.translatable("tooltip.bartending.fermenting_for", step.years),
                ChatFormatting.WHITE, new Point(bounds.x + 28, baseY + 8)));
    }

    private static void setupDistillingDisplay(ArrayList<Widget> widgets, int baseY, DistillingBrewerStep step, Rectangle bounds) {
        Component text = Component.translatable("tooltip.bartending.distilled_times", step.runs);
        widgets.add(new TextWidget(text, ChatFormatting.WHITE, new Point(bounds.x + 8, baseY + 8)));
    }

    private static void setupBoilingDisplay(ArrayList<Widget> widgets, int baseY, BoilingBrewerStep step, Rectangle bounds) {
        widgets.add(Widgets.createRecipeBase(new Rectangle(bounds.x, baseY - 12, bounds.width, 52)));
        int tx = bounds.x + 8;
        for (Map.Entry<Ingredient, Pair<Integer, Integer>> e : step.ingredients.entrySet()) {
            List<ItemStack> stacks = new ArrayList<>();

            for (ItemStack itemStack : e.getKey().getItems()) {
                ItemStack stack = itemStack.copy();
                stack.setCount(e.getValue().getFirst());
                stacks.add(stack);
            }

            widgets.add(Widgets.createSlot(new Point(tx, baseY)).entries(EntryIngredients.ofItemStacks(stacks)).markInput());

            tx += 20;
        }

        widgets.add(Widgets.createTexturedWidget(BoilerScreen.TEXTURE, new Rectangle(bounds.x + 8, baseY + 20, 17, 11), 176, 0));
        widgets.add(new TextWidget(Component.translatable("tooltip.bartending.boiling_in_progress", BrewingUtil.getTimeString(step.wantedTicks / 20)),
                ChatFormatting.WHITE, new Point(bounds.x + 28, baseY + 24)));
    }

}
