package ml.pluto7073.bartending.content.item;

import ml.pluto7073.bartending.foundations.step.BoilingBrewerStep;
import ml.pluto7073.bartending.foundations.step.FermentingBrewerStep;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MethodsReturnNonnullByDefault
public class ConcoctionItem extends Item {

    public ConcoctionItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player) user : null;
        if (playerEntity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        if (!level.isClientSide) {

        }

        if (playerEntity != null) {

        }

        if (playerEntity == null || !playerEntity.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        user.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        ListTag steps = stack.getOrCreateTag().getList("BrewingSteps", ListTag.TAG_COMPOUND);
        for (Tag tag : steps) {
            if (!(tag instanceof CompoundTag data)) continue;
            String type = data.getString("type");
            switch (type) {
                case BoilingBrewerStep.TYPE_ID -> BoilingBrewerStep.appendInProgressText(data, tooltip);
                case FermentingBrewerStep.TYPE_ID -> FermentingBrewerStep.appendInProgressText(data, tooltip);
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (stack.getOrCreateTag().contains("BrewingSteps")) {
            ListTag list = stack.getOrCreateTag().getList("BrewingSteps", Tag.TAG_COMPOUND);
            if (!list.isEmpty()) {
                ResourceLocation id = new ResourceLocation(list.getCompound(0).getString("item"));
                return "concoction.alcohol." + id.getNamespace() + "." + id.getPath();
            }
        }
        return super.getDescriptionId(stack);
    }
}
