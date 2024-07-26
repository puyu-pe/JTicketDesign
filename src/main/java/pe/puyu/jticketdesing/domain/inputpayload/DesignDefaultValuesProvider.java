package pe.puyu.jticketdesing.domain.inputpayload;

import org.jetbrains.annotations.NotNull;

public interface DesignDefaultValuesProvider {

    @NotNull
    PrinterDesignCell getDefaultCellValues();

    @NotNull
    PrinterDesignProperties getDefaultDesignProperties();

    @NotNull
    PrinterDesignStyle getDefaultDesignStyle();

    @NotNull
    PrinterDesignBlock getDefaultBlockValues();

    @NotNull
    PrinterDesignObject getDefaultDesignObject();

    @NotNull
    PrinterDesignOpenDrawer getDefaultOpenDrawer();

}
