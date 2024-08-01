package pe.puyu.jticketdesing.domain.builder;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.PrinterDesignObject;

public interface SweetDesignObjectBuilder {
    @NotNull
    PrinterDesignObject build();
}
