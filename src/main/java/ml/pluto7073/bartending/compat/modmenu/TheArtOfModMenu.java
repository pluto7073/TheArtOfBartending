package ml.pluto7073.bartending.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pluto7073.bartending.client.TheArtOfClient;
import ml.pluto7073.bartending.client.config.ClientConfig;
import ml.pluto7073.bartending.foundations.alcohol.AlcDisplayType;
import net.minecraft.network.chat.Component;

public class TheArtOfModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ClientConfig config = TheArtOfClient.config();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("title.bartending.config"));

            ConfigCategory general = builder.getOrCreateCategory(Component.translatable("title.bartending.config"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startEnumSelector(Component.translatable("option.bartending.alcohol_display"),
                    AlcDisplayType.class, config.getAlcoholDisplayType())
                    .setDefaultValue(AlcDisplayType.PROOF)
                    .setTooltip(Component.translatable("option.bartending.alcohol_display.tooltip"))
                    .setSaveConsumer(config::setAlcoholDisplayType)
                    .build());

			general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.bartending.blackout_enabled"), config.getBlackoutEnabled())
					.setDefaultValue(true)
					.setTooltip(Component.translatable("option.bartending.blackout_enabled.tooltip"))
					.setSaveConsumer(config::setBlackoutEnabled)
					.build());

            return builder.setSavingRunnable(config::save).build();
        };
    }

}
