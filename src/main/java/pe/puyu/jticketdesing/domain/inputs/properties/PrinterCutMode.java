package pe.puyu.jticketdesing.domain.inputs.properties;

public enum PrinterCutMode {
    PART("PART"),

    FULL("FULL");

    private final String value;

    PrinterCutMode(String value) {
        this.value = value;
    }

    public static PrinterCutMode fromValue(String value) {
        for (PrinterCutMode mode : PrinterCutMode.values()) {
            if (mode.value.equalsIgnoreCase(value)) {
                return mode;
            }
        }
        return PART;
    }
}