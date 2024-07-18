package pe.puyu.jticketdesing.domain.inputpayload;



public enum PrinterJustifyAlign {
    CENTER("CENTER"),

    LEFT("LEFT"),

    RIGHT("RIGHT");

    private final String value;

    PrinterJustifyAlign(String value) {
        this.value = value;
    }

    public static PrinterJustifyAlign fromValue(String value) {
        for (PrinterJustifyAlign type : PrinterJustifyAlign.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return LEFT;
    }

    public String getValue(){
        return value;
    }
}
