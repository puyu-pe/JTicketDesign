package pe.puyu.SweetTicketDesign.domain.components.properties;

public enum SweetCutMode {
    PART("PART"),

    FULL("FULL");

    private final String value;

    SweetCutMode(String value) {
        this.value = value;
    }

    public static SweetCutMode fromValue(String value) {
        for (SweetCutMode mode : SweetCutMode.values()) {
            if (mode.value.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return PART;
    }
}
