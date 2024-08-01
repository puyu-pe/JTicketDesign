package pe.puyu.SweetTicketDesign.application.components;

import org.jetbrains.annotations.NotNull;
import pe.puyu.SweetTicketDesign.domain.components.block.*;
import pe.puyu.SweetTicketDesign.domain.components.drawer.SweetOpenDrawerComponent;
import pe.puyu.SweetTicketDesign.domain.components.drawer.SweetPinConnector;
import pe.puyu.SweetTicketDesign.domain.components.properties.SweetCutMode;
import pe.puyu.SweetTicketDesign.domain.components.properties.SweetCutComponent;
import pe.puyu.SweetTicketDesign.domain.components.properties.SweetPropertiesComponent;
import pe.puyu.SweetTicketDesign.domain.components.SweetDefaultComponentsProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DefaultComponentsProvider implements SweetDefaultComponentsProvider {

    @Override
    public @NotNull SweetCellComponent getCellComponent() {
        return new SweetCellComponent("", "");
    }

    @Override
    public @NotNull SweetPropertiesComponent getPropertiesComponent() {
        int charactersWidth = 42; // 42 characters equals 72 mm approx.
        return new SweetPropertiesComponent(
            charactersWidth,
            false,
            "WPC1252",
            new SweetCutComponent(4, SweetCutMode.PART)
        );
    }

    @Override
    public @NotNull SweetStyleComponent getStyleComponent() {
        return new SweetStyleComponent(
            1,
            1,
            false,
            false,
            false,
            ' ',
            SweetJustify.LEFT,
            1,
            SweetScale.SMOOTH,
            290,
            290
        );
    }

    @Override
    public @NotNull SweetBlockComponent getBlockComponent() {
        return new SweetBlockComponent(
            1,
            ' ',
            null,
            null,
            6,
            new HashMap<>(),
            new LinkedList<>()
        );
    }

    @Override
    public @NotNull SweetOpenDrawerComponent getOpenDrawerComponent() {
        return new SweetOpenDrawerComponent(SweetPinConnector.Pin_2, 120, 240);
    }

    @Override
    public @NotNull List<SweetBlockComponent> getDataComponent() {
        return new LinkedList<>();
    }

    @Override
    public @NotNull SweetQrComponent getQrComponent() {
        return new SweetQrComponent(
            null,
            SweetQrType.IMG,
            SweetQrCorrectionLevel.Q
        );
    }
}
