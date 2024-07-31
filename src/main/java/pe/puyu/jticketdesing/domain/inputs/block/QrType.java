package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public enum QrType {

    IMG("IMG"),

    NATIVE("NATIVE");

    private final String value;

    QrType(String value){
        this.value = value;
    }

    public static QrType fromValue(@Nullable String value) {
        for (QrType type : QrType.values()) {
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
