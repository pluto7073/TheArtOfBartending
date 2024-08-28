package ml.pluto7073.bartending.client.block.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import ml.pluto7073.bartending.content.block.BottlerBlock;
import ml.pluto7073.bartending.content.block.entity.BottlerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import static ml.pluto7073.bartending.content.block.entity.BottlerBlockEntity.*;

import javax.annotation.ParametersAreNonnullByDefault;

@Environment(EnvType.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BottlerBlockEntityRenderer implements BlockEntityRenderer<BottlerBlockEntity> {

    public BottlerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BottlerBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (entity.getLevel() != null && !entity.isEmpty()) {
            BlockState state = entity.getLevel().getBlockState(entity.getBlockPos());
            if (!state.isAir()) {
                ItemStack input = entity.getItem(CONCOCTION_INPUT_SLOT);
                ItemStack display = entity.getItem(RESULT_DISPLAY_SLOT);
                ItemStack result = entity.getItem(RESULT_SLOT);
                ItemStack bottle = entity.getItem(BOTTLE_INSERT_SLOT);
                final ItemStack stackToDisplay;
                if (!result.isEmpty()) stackToDisplay = result;
                else if (!display.isEmpty()) stackToDisplay = display;
                else if (!input.isEmpty() || !input.is(Items.GLASS_BOTTLE)) stackToDisplay = input;
                else if (!bottle.isEmpty()) stackToDisplay = bottle;
                else return;

                Direction dir = state.getValue(BottlerBlock.FACING);
                stack.pushPose();
                stack.translate(0.375f, 0.375f, 0.25f);

                int deg = switch (dir) {
                    case SOUTH -> 180;
                    case EAST -> 90;
                    case WEST -> 270;
                    default -> 0;
                };

                stack.mulPose(Axis.YP.rotationDegrees(deg));

                stack.scale(0.5f, 0.5f, 0.5f);

                Minecraft.getInstance().getItemRenderer()
                        .renderStatic(stackToDisplay, ItemDisplayContext.HEAD, packedLight, packedOverlay, stack,
                                buffer, entity.getLevel(), (int) entity.getBlockPos().asLong());

                stack.popPose();
            }
        }
    }
}
