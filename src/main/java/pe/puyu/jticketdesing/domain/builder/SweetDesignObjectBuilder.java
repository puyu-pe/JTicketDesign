package pe.puyu.jticketdesing.domain.builder;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.SweetPrinterObjectComponent;

public interface SweetDesignObjectBuilder {
    @NotNull
    SweetPrinterObjectComponent build();
}
