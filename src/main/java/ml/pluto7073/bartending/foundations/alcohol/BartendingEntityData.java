package ml.pluto7073.bartending.foundations.alcohol;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;

public class BartendingEntityData {

    public static final EntityDataAccessor<Float> PLAYER_ALCOHOL_CONTENT =
            SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);

    public static void init() {}

}
