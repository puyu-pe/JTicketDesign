package pe.puyu.jticketdesing.domain.maker;

import pe.puyu.jticketdesing.domain.inputpayload.DesignDefaultValuesProvider;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignObject;

public interface DesignObjectMaker {
    PrinterDesignObject build();
}
