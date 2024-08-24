package ml.pluto7073.bartending.content.gui;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.client.TheArtOfClient;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class TAOBMenuTypes {

    public static final MenuType<BoilerMenu> BOILER_MENU_TYPE = register("boiler", BoilerMenu::new);
    public static final MenuType<BottlerMenu> BOTTLER_MENU_TYPE = register("bottler", BottlerMenu::new);

    public static void init() {}

    private static <T extends AbstractContainerMenu> MenuType<T> register(String id, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, TheArtOfBartending.asId(id), new MenuType<>(factory, FeatureFlags.VANILLA_SET));
    }

}
