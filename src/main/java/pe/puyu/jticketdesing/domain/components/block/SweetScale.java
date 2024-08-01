package pe.puyu.jticketdesing.domain.components.block;

import org.jetbrains.annotations.Nullable;

public enum SweetScale {
    DEFAULT("DEFAULT"),

    FAST("FAST"),

    SMOOTH("SMOOTH"),

    REPLICATE("REPLICATE"),

    AREA_AVERAGING("AREA_AVERAGING");

    private final String value;

    SweetScale(@Nullable String value){
        this.value = value;
    }

    public static SweetScale fromValue(String value){
        for (SweetScale type : SweetScale.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return SMOOTH;
    }

    public String getValue(){
        return value;
    }
}
