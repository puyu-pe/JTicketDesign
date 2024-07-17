package pe.puyu.jticketdesing.domain.inputpayload;

public interface DesignDefaultValuesProvider {

    PrinterDesignCell getDefaultCellValues();

    PrinterDesignProperties getDefaultDesignProperties();

    PrinterDesignStyle getDefaultDesignStyle();

    PrinterDesignBlock getDefaultBlockValues();

    PrinterDesignObject getDefaultDesignObject();

}
