package pe.puyu.jticketdesing.domain.mapper;

import pe.puyu.jticketdesing.domain.inputpayload.DesignDefaultValuesProvider;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignObject;

public interface DesignObjectMapper {
    PrinterDesignObject build(DesignDefaultValuesProvider printerDefaultValuesProvider);
}
