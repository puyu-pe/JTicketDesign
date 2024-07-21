package pe.puyu.jticketdesing.domain.inputpayload;


import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PrinterDesignObject(
    @Nullable PrinterDesignProperties properties,
    @Nullable List< @Nullable PrinterDesignBlock> blocks
) {

}
