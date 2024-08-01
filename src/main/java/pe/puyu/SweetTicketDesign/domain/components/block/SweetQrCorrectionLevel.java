package pe.puyu.SweetTicketDesign.domain.components.block;

import org.jetbrains.annotations.Nullable;

public enum SweetQrCorrectionLevel
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

    SweetQrCorrectionLevel(@Nullable String value){
        this.value = value;
    }

    public static SweetQrCorrectionLevel fromValue(@Nullable String value){
        for (SweetQrCorrectionLevel type : SweetQrCorrectionLevel.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return SweetQrCorrectionLevel.Q;
    }
}
