package pe.puyu.SweetTicketDesign.domain.components.drawer;

import org.jetbrains.annotations.Nullable;

public enum SweetPinConnector {
    Pin_2(2),

    Pin_5(5);

    private final Integer value;

    SweetPinConnector(int value) {
        this.value = value;
    }

    public static SweetPinConnector fromValue(@Nullable Integer value) {
        if (value != null) {
            for (SweetPinConnector pin : SweetPinConnector.values()) {
                if (pin.value.equals(value)) {
                    return pin;
                }
            }
        }
        return Pin_2;
    }
}
