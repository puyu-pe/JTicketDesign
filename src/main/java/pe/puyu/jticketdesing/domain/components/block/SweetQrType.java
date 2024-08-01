package pe.puyu.jticketdesing.domain.components.block;

import org.jetbrains.annotations.Nullable;

public enum SweetQrType {

    IMG("IMG"),

    NATIVE("NATIVE");

    private final String value;

    SweetQrType(String value){
        this.value = value;
    }

    public static SweetQrType fromValue(@Nullable String value) {
        for (SweetQrType type : SweetQrType.values()) {
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
