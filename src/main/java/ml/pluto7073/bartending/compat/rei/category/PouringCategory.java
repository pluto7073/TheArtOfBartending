package ml.pluto7073.bartending.compat.rei.category;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import ml.pluto7073.bartending.compat.rei.TheArtOfREI;
import ml.pluto7073.bartending.compat.rei.display.PouringDisplay;
import ml.pluto7073.bartending.content.item.BartendingItems;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PouringCategory implements DisplayCategory<PouringDisplay> {

    @Override
    public List<Widget> setupDisplay(PouringDisplay display, Rectangle bounds) {
        ArrayList<Widget> widgets = new ArrayList<>();
        Point base = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(base.x + 27, base.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(base.x + 61, base.y + 5)));
        widgets.add(Widgets.createSlot(new Point(base.x + 61, base.y + 5))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(base.x + 4, base.y + 5))
                .entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(base.x - 16, base.y + 5))
                .entries(display.getInputEntries().get(0)).markInput());
        return widgets;
    }

    @Override
    public CategoryIdentifier<? extends PouringDisplay> getCategoryIdentifier() {
        return TheArtOfREI.POURING;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.bartending.pouring");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BartendingItems.LIQUOR_BOTTLE);
    }
}
