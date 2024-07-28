package pe.puyu.jticketdesing.domain.inputs.provider;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterDesignBlock;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterDesignProperties;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterDesignOpenDrawer;
import pe.puyu.jticketdesing.domain.inputs.text.PrinterDesignCell;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterDesignStyle;

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
