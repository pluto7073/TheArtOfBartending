package ml.pluto7073.bartending.client.gui;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.content.gui.BoilerMenu;
import ml.pluto7073.bartending.foundations.util.BrewingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

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
        this.titleLabelY -= 12;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xEEEEEE, true);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX >= i + 78 && mouseX <= i + 99) {
            if (mouseY >= j + 45 && mouseY <= j + 50) {
                guiGraphics.renderTooltip(font, Component.translatable("tooltip.bartending.water", menu.getWater() / 81)
                        .withStyle(ChatFormatting.GRAY), mouseX, mouseY);
            }
        }
        if (mouseX >= i + 84 && mouseX <= i + 92) {
            if (mouseY >= j + 9 && mouseY <= j + 17) {
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
            guiGraphics.blit(TEXTURE, i + 80, j + 55, 176, 0, 17, 11);
        }
        if (menu.isSuperHeated()) {
            guiGraphics.blit(TEXTURE, i + 80, j + 55, 176, 56, 17, 11);
        }

        // Water
        int water = menu.getWater();
        int clamped = Mth.clamp((18 * water + 81000 - 1) / 81000, 0, 18);
        if (clamped > 0) {
            guiGraphics.blit(TEXTURE, i + 79, j + 46, 176, 53, clamped, 2);
        }
    }

}
