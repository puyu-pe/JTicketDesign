package pe.puyu.SweetTicketDesign.domain.builder;

import org.jetbrains.annotations.NotNull;
import pe.puyu.SweetTicketDesign.domain.components.SweetPrinterObjectComponent;

public interface SweetPrinterObjectBuilder {
    @NotNull
    SweetPrinterObjectComponent build();
}
