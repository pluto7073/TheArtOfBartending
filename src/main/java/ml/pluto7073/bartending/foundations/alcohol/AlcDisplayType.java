package ml.pluto7073.bartending.foundations.alcohol;

public enum AlcDisplayType {

    GRAMS(1),
    ML(1.26670466781f),
    OUNCES(0.0445817721198f),
    PROOF(-1);

    public final float multiplier;

    AlcDisplayType(float multiplierFromGrams) {
        multiplier = multiplierFromGrams;
    }

}
