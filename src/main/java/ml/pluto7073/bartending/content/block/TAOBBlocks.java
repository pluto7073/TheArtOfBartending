package ml.pluto7073.bartending.content.block;

import ml.pluto7073.bartending.TheArtOfBartending;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;

public class TAOBBlocks {

    public static final Block BOILER = new BoilerBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)
            .pushReaction(PushReaction.BLOCK));

    // Barrels
    public static final FermentingBarrelBlock OAK_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.OAK, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock SPRUCE_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.SPRUCE, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock BIRCH_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.BIRCH, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock JUNGLE_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.JUNGLE, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock ACACIA_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.ACACIA, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock DARK_OAK_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.DARK_OAK, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock MANGROVE_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.MANGROVE, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock CHERRY_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.CHERRY, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock BAMBOO_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.BAMBOO, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock CRIMSON_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.CRIMSON, FabricBlockSettings.copy(Blocks.BARREL));
    public static final FermentingBarrelBlock WARPED_FERMENTING_BARREL = new FermentingBarrelBlock(WoodType.WARPED, FabricBlockSettings.copy(Blocks.BARREL));

    private static void register(String id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, TheArtOfBartending.asId(id), block);
    }

    public static void init() {
        register("boiler", BOILER);

        register("oak_fermenting_barrel", OAK_FERMENTING_BARREL);
        register("spruce_fermenting_barrel", SPRUCE_FERMENTING_BARREL);
        register("birch_fermenting_barrel", BIRCH_FERMENTING_BARREL);
        register("jungle_fermenting_barrel", JUNGLE_FERMENTING_BARREL);
        register("acacia_fermenting_barrel", ACACIA_FERMENTING_BARREL);
        register("dark_oak_fermenting_barrel", DARK_OAK_FERMENTING_BARREL);
        register("mangrove_fermenting_barrel", MANGROVE_FERMENTING_BARREL);
        register("cherry_fermenting_barrel", CHERRY_FERMENTING_BARREL);
        register("bamboo_fermenting_barrel", BAMBOO_FERMENTING_BARREL);
        register("crimson_fermenting_barrel", CRIMSON_FERMENTING_BARREL);
        register("warped_fermenting_barrel", WARPED_FERMENTING_BARREL);
    }

}
