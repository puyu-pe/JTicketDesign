package pe.puyu.jticketdesing.domain.components.block;



public enum SweetJustify {
    CENTER("CENTER"),

    LEFT("LEFT"),

    RIGHT("RIGHT");

    private final String value;

    SweetJustify(String value) {
        this.value = value;
    }

    public static SweetJustify fromValue(String value) {
        for (SweetJustify type : SweetJustify.values()) {
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
