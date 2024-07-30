package pe.puyu.jticketdesing.application.defaultprovider;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.*;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterDesignOpenDrawer;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterPinConnector;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterCutMode;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterDesignCut;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterDesignProperties;
import pe.puyu.jticketdesing.domain.inputs.DesignDefaultValuesProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SimpleDesignDefaultProvider implements DesignDefaultValuesProvider {

    @Override
    public @NotNull PrinterDesignCell getDefaultCellValues() {
        return new PrinterDesignCell("", "");
    }

    @Override
    public @NotNull PrinterDesignProperties getDefaultDesignProperties() {
        int charactersWidth = 42; // 42 characters equals 72 mm approx.
        return new PrinterDesignProperties(
            charactersWidth,
            false,
            "WPC1252",
            new PrinterDesignCut(4, PrinterCutMode.PART)
        );
    }

    @Override
    public @NotNull PrinterDesignStyle getDefaultDesignStyle() {
        return new PrinterDesignStyle(
            1,
            1,
            false,
            false,
            false,
            ' ',
            PrinterJustifyAlign.LEFT,
            1,
            ScaleType.SMOOTH,
            290,
            290,
            TypeQr.IMG
        );
    }

    @Override
    public @NotNull PrinterDesignBlock getDefaultBlockValues() {
        return new PrinterDesignBlock(
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
    public @NotNull PrinterDesignOpenDrawer getDefaultOpenDrawer() {
        return new PrinterDesignOpenDrawer(PrinterPinConnector.Pin_2, 120, 240);
    }

    @Override
    public @NotNull List<PrinterDesignBlock> getDefaultData() {
        return new LinkedList<>();
    }
}
