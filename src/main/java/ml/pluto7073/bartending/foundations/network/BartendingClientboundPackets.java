package ml.pluto7073.bartending.foundations.network;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.config.BartendingGameRules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class BartendingClientboundPackets {

    public static final ResourceLocation UPDATE_YEAR_LENGTH = TheArtOfBartending.asId("clientbound/update_year_length");

    @Environment(EnvType.CLIENT)
    public static void registerReceivers() {

        ClientPlayConnectionEvents.INIT.register((listener, minecraft) -> {
            ClientPlayNetworking.registerGlobalReceiver(UPDATE_YEAR_LENGTH, (mc, handler, buf, sender) -> {
                int ticks = buf.readInt();
                assert mc.level != null;
                mc.level.getGameRules().getRule(BartendingGameRules.YEAR_LENGTH_TICKS).set(ticks, null);
            });
        });

    }

}
