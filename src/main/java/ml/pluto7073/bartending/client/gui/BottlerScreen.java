package ml.pluto7073.bartending.client.gui;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.block.entity.BottlerBlockEntity;
import ml.pluto7073.bartending.content.gui.BottlerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class BottlerScreen extends AbstractContainerScreen<BottlerMenu> {

    public static final ResourceLocation TEXTURE = TheArtOfBartending.asId("textures/gui/container/bottler.png");

    public BottlerScreen(BottlerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = imageWidth / 2 - font.width(title) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, imageWidth, imageHeight);

        // Time
        int timeDisplay = Mth.clamp((int) (17 * ((float) menu.bottleTime() / (BottlerBlockEntity.MAX_BOTTLE_TIME))), 0, 17);
        if (menu.bottleTime() > 0) {
            guiGraphics.blit(TEXTURE, i + 77, j + 30, 176, 0, timeDisplay, 10);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

}
