package ml.pluto7073.bartending.foundations.config;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class BartendingGameRules {

    public static final GameRules.Key<GameRules.BooleanValue> DO_BLACKOUT =
            GameRuleRegistry.register("doBlackout", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    public static void init() {}

}
