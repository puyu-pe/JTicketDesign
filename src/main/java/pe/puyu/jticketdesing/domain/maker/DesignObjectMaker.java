package pe.puyu.jticketdesing.domain.maker;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignObject;

public interface DesignObjectMaker {
    @NotNull
    PrinterDesignObject build();
}
