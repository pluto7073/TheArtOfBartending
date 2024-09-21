package ml.pluto7073.bartending.foundations.alcohol;

public enum AlcDisplayType {

    GRAMS(1, "g"),
    ML(1.26670466781f, "mL"),
    OUNCES(0.0445817721198f, "oz"),
    PROOF(-1, " Proof");

    public final float multiplier;
    private final String ending;

    AlcDisplayType(float multiplierFromGrams, String ending) {
        multiplier = multiplierFromGrams;
        this.ending = ending;
    }

    public String format(float amount) {
        String strAmount = String.valueOf(amount).transform(s -> {
            if (amount > 9999) return s;
            return s.substring(0, 3);
        });
        return strAmount + ending;
    }

}
