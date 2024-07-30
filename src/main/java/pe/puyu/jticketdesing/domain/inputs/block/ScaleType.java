package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public enum ScaleType {
    DEFAULT("DEFAULT"),

    FAST("FAST"),

    SMOOTH("SMOOTH"),

    REPLICATE("REPLICATE"),

    AREA_AVERAGING("AREA_AVERAGING");

    private final String value;

    ScaleType(@Nullable String value){
        this.value = value;
    }

    public static ScaleType fromValue(String value){
        for (ScaleType type : ScaleType.values()) {
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
