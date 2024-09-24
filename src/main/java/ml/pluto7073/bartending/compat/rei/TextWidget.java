package ml.pluto7073.bartending.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TextWidget extends Widget {

    private final Component component;
    private final ChatFormatting color;
    private final Point pos;

    public TextWidget(Component component, ChatFormatting color, Point pos) {
        this.component = component;
        this.color = color;
        if (!this.color.isColor()) throw new IllegalArgumentException("color must be a color format");
        this.pos = pos;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawString(Minecraft.getInstance().font, component, pos.x, pos.y, color.getColor());
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of();
    }
}
