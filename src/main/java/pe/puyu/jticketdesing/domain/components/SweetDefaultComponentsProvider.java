package pe.puyu.jticketdesing.domain.components;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.block.SweetBlockComponent;
import pe.puyu.jticketdesing.domain.components.block.SweetQrComponent;
import pe.puyu.jticketdesing.domain.components.properties.SweetPropertiesComponent;
import pe.puyu.jticketdesing.domain.components.drawer.SweetOpenDrawerComponent;
import pe.puyu.jticketdesing.domain.components.block.SweetCellComponent;
import pe.puyu.jticketdesing.domain.components.block.SweetStyleComponent;

import java.util.List;

public interface SweetDefaultComponentsProvider {

    @NotNull
    SweetCellComponent getCellComponent();

    @NotNull
    SweetPropertiesComponent getPropertiesComponent();

    @NotNull
    SweetStyleComponent getStyleComponent();

    @NotNull
    SweetBlockComponent getBlockComponent();

    @NotNull
    SweetOpenDrawerComponent getOpenDrawerComponent();

    @NotNull
    List<SweetBlockComponent> getDataComponent();

    @NotNull
    SweetQrComponent getQrComponent();
}
