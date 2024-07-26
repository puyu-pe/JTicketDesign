package pe.puyu.jticketdesing.application.defaultprovider;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.*;

import java.util.HashMap;
import java.util.LinkedList;

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
            1
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
    public @NotNull PrinterDesignObject getDefaultDesignObject() {
        return new PrinterDesignObject(
            getDefaultDesignProperties(),
            new LinkedList<>(),
            getDefaultOpenDrawer()
        );
    }

    @Override
    public @NotNull PrinterDesignOpenDrawer getDefaultOpenDrawer() {
        return new PrinterDesignOpenDrawer(PrinterPinConnector.Pin_2, 120, 240);
    }
}
