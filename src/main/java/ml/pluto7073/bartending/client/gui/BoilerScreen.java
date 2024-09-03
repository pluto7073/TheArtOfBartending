package ml.pluto7073.bartending.client.gui;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.gui.BoilerMenu;
import ml.pluto7073.bartending.foundations.BrewingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BoilerScreen extends AbstractContainerScreen<BoilerMenu> {

    public static final ResourceLocation TEXTURE = TheArtOfBartending.asId("textures/gui/container/boiler.png");

    public BoilerScreen(BoilerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX >= i + 23 && mouseX <= i + 23 + 18) {
            if (mouseY >= j + 35 && mouseY <= j + 35 + 4) {
                guiGraphics.renderTooltip(font, Component.translatable("tooltip.bartending.water", menu.getWater() / 81)
                        .withStyle(ChatFormatting.GRAY), mouseX, mouseY);
            }
        }
        if (mouseX >= i + 75 && mouseX <= i + 75 + 8) {
            if (mouseY >= j + 28 && mouseY <= j + 28 + 11) {
                guiGraphics.renderTooltip(font, Component.translatable("tooltip.bartending.boilTime",
                        BrewingUtil.getTimeString(menu.getBoilTime() / 20)).withStyle(ChatFormatting.GRAY), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, imageWidth, imageHeight);

        // Fire
        if (menu.isHeated()) {
            guiGraphics.blit(TEXTURE, i + 48, j + 60, 176, 0, 17, 11);
        }
        if (menu.isSuperHeated()) {
            guiGraphics.blit(TEXTURE, i + 48, j + 60, 176, 56, 17, 11);
        }

        // Water
        int water = menu.getWater();
        int clamped = Mth.clamp((18 * water + 81000 - 1) / 81000, 0, 18);
        if (clamped > 0) {
            guiGraphics.blit(TEXTURE, i + 23, j + 35, 176, 52, clamped, 4);
        }

        // Time
        int timeSeconds = menu.getBoilTime() / 20;
        if (timeSeconds > 0) {
            guiGraphics.blit(TEXTURE, i + 75, j + 27, 176, 32, 8, 11);
        }
    }

}
