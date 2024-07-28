package pe.puyu.jticketdesing.domain.inputs;


import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterDesignBlock;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterDesignOpenDrawer;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterDesignProperties;

import java.util.List;

public record PrinterDesignObject(
    @Nullable PrinterDesignProperties properties,
    @Nullable List< @Nullable PrinterDesignBlock> blocks,
    @Nullable PrinterDesignOpenDrawer openDrawer
) {

}
