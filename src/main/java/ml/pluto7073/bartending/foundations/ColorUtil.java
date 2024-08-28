package ml.pluto7073.bartending.foundations;

import java.util.HashMap;

public class ColorUtil {

    public static final HashMap<String, Integer> COLORS_REGISTRY = new HashMap<>();

    public static int get(String s) {
        if (!COLORS_REGISTRY.containsKey(s)) return 0;
        return COLORS_REGISTRY.get(s);
    }

    static {
        COLORS_REGISTRY.put("minecraft:wheat", 9402184);
        COLORS_REGISTRY.put("minecraft:sweet_berries", 9321518);
        COLORS_REGISTRY.put("minecraft:glow_berries", 12223780);
        COLORS_REGISTRY.put("minecraft:potato", 0xf2e6ab);
        COLORS_REGISTRY.put("minecraft:apple", 0xbc8a49);
        COLORS_REGISTRY.put("minecraft:sugar_cane", 0x825424);

        COLORS_REGISTRY.put("bartending:oak_fermenting_barrel", 0xc29d62);
        COLORS_REGISTRY.put("bartending:cherry_fermenting_barrel", 0xefd0ef);
        COLORS_REGISTRY.put("bartending:bamboo_fermenting_barrel", 0xe3cc6a);
    }

}
