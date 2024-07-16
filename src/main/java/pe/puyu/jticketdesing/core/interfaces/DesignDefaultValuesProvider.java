package pe.puyu.jticketdesing.core.interfaces;

import pe.puyu.jticketdesing.core.bean.*;

public interface DesignDefaultValuesProvider {

    PrinterDesignCell getDefaultCellValues();

    PrinterDesignProperties getDefaultDesignProperties();

    PrinterDesignStyle getDefaultDesignStyle();

    PrinterDesignBlock getDefaultBlockValues();

    PrinterDesignObject getDefaultDesignObject();

}
