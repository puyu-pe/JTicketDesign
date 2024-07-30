package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public enum TypeQr {

    IMG("IMG"),

    NATIVE("NATIVE");

    private final String value;

    TypeQr(String value){
        this.value = value;
    }

    public static TypeQr fromValue(@Nullable String value) {
        for (TypeQr type : TypeQr.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return IMG;
    }

    public String getValue(){
        return value;
    }
}
