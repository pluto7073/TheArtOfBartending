package ml.pluto7073.bartending.foundations.alcohol;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ml.pluto7073.pdapi.addition.chemicals.ConsumableChemicalHandler;
import ml.pluto7073.pdapi.addition.chemicals.ConsumableChemicalRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AlcoholHandler implements ConsumableChemicalHandler {

    public static final ConsumableChemicalHandler INSTANCE = ConsumableChemicalRegistry.register(new AlcoholHandler());

    @Override
    public void tickPlayer(Player player) {

    }

    @Override
    public float get(Player player) {
        return 0;
    }

    @Override
    public void add(Player player, float amount) {

    }

    @Override
    public void set(Player player, float amount) {

    }

    @Override
    public Collection<MobEffectInstance> getEffectsForAmount(float amount, Player player) {
        return List.of();
    }

    @Override
    public String getName() {
        return "alcohol";
    }

    @Override
    public void saveToTag(SynchedEntityData data, CompoundTag tag) {

    }

    @Override
    public void loadFromTag(SynchedEntityData data, CompoundTag tag) {

    }

    @Override
    public void defineDataForPlayer(SynchedEntityData data) {

    }

    @Override
    public void appendTooltip(List<Component> tooltip, float amount) {

    }

    @Override
    public @Nullable LiteralArgumentBuilder<CommandSourceStack> getDrinkSubcommand() {
        return null;
    }

    public static void init() {
        INSTANCE.appendTooltip(new ArrayList<>(), 0);
    }

}
