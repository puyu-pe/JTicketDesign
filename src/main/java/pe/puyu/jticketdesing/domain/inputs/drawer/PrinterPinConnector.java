package pe.puyu.jticketdesing.domain.inputs.drawer;

import org.jetbrains.annotations.Nullable;

public enum PrinterPinConnector {
    Pin_2(2),

    Pin_5(5);

    private final Integer value;

    PrinterPinConnector(int value) {
        this.value = value;
    }

    public static PrinterPinConnector fromValue(@Nullable Integer value) {
        if (value != null) {
            for (PrinterPinConnector pin : PrinterPinConnector.values()) {
                if (pin.value.equals(value)) {
                    return pin;
                }
            }
        }
        return Pin_2;
    }
}
