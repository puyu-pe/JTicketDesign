package pe.puyu.jticketdesing.domain.inputpayload;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    PrinterDesignOpenDrawer getDefaultOpenDrawer();

    @NotNull
    List<PrinterDesignBlock> getDefaultData();

}
