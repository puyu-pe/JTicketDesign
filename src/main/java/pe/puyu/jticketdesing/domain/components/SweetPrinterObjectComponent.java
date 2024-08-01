package pe.puyu.jticketdesing.domain.components;


import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.components.block.SweetBlockComponent;
import pe.puyu.jticketdesing.domain.components.drawer.SweetOpenDrawerComponent;
import pe.puyu.jticketdesing.domain.components.properties.SweetPropertiesComponent;

import java.util.List;

public record SweetPrinterObjectComponent(
    @Nullable SweetPropertiesComponent properties,
    @Nullable List< @Nullable SweetBlockComponent> blocks,
    @Nullable SweetOpenDrawerComponent openDrawer
) {

}
