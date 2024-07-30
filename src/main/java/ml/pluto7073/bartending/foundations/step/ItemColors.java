package ml.pluto7073.bartending.foundations.step;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;

public class ItemColors {

    public static final HashMap<String, Integer> COLORS_REGISTRY = new HashMap<>();

    static {
        COLORS_REGISTRY.put("minecraft:wheat", 9402184);
        COLORS_REGISTRY.put("minecraft:sweet_berries", 9321518);
        COLORS_REGISTRY.put("minecraft:glow_berries", 12223780);
    }

}
