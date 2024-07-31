package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public enum QrCorrectionLevel
{
    //7%
    L("L"),

    //15%
    M("M"),

    //25%
    Q("Q"),

    //30%
    H("H");

    private final String value;

    QrCorrectionLevel(@Nullable String value){
        this.value = value;
    }

    public static QrCorrectionLevel fromValue(@Nullable String value){
        for (QrCorrectionLevel type : QrCorrectionLevel.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return QrCorrectionLevel.Q;
    }
}
