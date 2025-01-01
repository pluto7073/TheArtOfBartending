package ml.pluto7073.bartending.foundations.config;

import ml.pluto7073.bartending.foundations.network.BartendingClientboundPackets;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

public class BartendingGameRules {

    public static final GameRules.Key<GameRules.BooleanValue> DO_BLACKOUT =
            GameRuleRegistry.register("doBlackout", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.IntegerValue> YEAR_LENGTH_TICKS =
            GameRuleRegistry.register("yearLengthTicks", GameRules.Category.UPDATES,
                    GameRuleFactory.createIntRule(24000, 1, (server, value) -> {
                        if (server == null) return;
                        FriendlyByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(value.get());
                        for (ServerPlayer player : PlayerLookup.all(server)) {
                            ServerPlayNetworking.send(player, BartendingClientboundPackets.UPDATE_YEAR_LENGTH, buf);
                        }
                    }));

    public static void init() {}

}
