package ml.pluto7073.bartending.client.config;

import ml.pluto7073.bartending.TheArtOfBartending;
import ml.pluto7073.bartending.foundations.alcohol.AlcDisplayType;
import ml.pluto7073.pdapi.config.BaseConfig;

public class ClientConfig extends BaseConfig {

    public ClientConfig() {
        super("bartending", "client", TheArtOfBartending.LOGGER);
    }

    @Override
    public void initConfig() {
        setEnum("alcoholDisplayType", AlcDisplayType.PROOF);
    }

    public void setAlcoholDisplayType(AlcDisplayType type) {
        setEnum("alcoholDisplayType", type);
    }

    public AlcDisplayType getAlcoholDisplayType() {
        return getEnum("alcoholDisplayType", AlcDisplayType.class);
    }

}
