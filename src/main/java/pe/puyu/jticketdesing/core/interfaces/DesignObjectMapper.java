package pe.puyu.jticketdesing.core.interfaces;

import pe.puyu.jticketdesing.core.bean.PrinterDesignObject;

public interface DesignObjectMapper {
    PrinterDesignObject build(DesignDefaultValuesProvider printerDefaultValuesProvider);
}
