package pe.puyu.SweetTicketDesign.domain.components;


import org.jetbrains.annotations.Nullable;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetBlockComponent;
import pe.puyu.SweetTicketDesign.domain.components.drawer.SweetOpenDrawerComponent;
import pe.puyu.SweetTicketDesign.domain.components.properties.SweetPropertiesComponent;

import java.util.List;

public record SweetPrinterObjectComponent(
    @Nullable SweetPropertiesComponent properties,
    @Nullable List< @Nullable SweetBlockComponent> blocks,
    @Nullable SweetOpenDrawerComponent openDrawer
) {

}
