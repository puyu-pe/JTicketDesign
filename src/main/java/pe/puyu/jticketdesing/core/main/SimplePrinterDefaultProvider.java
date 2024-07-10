package pe.puyu.jticketdesing.core.main;

import pe.puyu.jticketdesing.core.bean.*;
import pe.puyu.jticketdesing.core.interfaces.PrinterDefaultValuesProvider;

import java.util.HashMap;
import java.util.LinkedList;

public class SimplePrinterDefaultProvider implements PrinterDefaultValuesProvider {

    @Override
    public PrinterDesignCell getDefaultCellValues() {
        return new PrinterDesignCell("", "");
    }

    @Override
    public PrinterDesignProperties getDefaultDesignProperties() {
        int charactersWidth = 42; // 42 characters equals 72 mm approx.
        return new PrinterDesignProperties(charactersWidth, false, "WPC1252");
    }

    @Override
    public PrinterDesignStyle getDefaultDesignStyle() {
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
    public PrinterDesignBlock getDefaultBlockValues() {
        return new PrinterDesignBlock(
            1,
            ' ',
            "",
            "",
            new HashMap<>(),
            12,
            new LinkedList<>()
        );
    }

    @Override
    public PrinterDesignObject getDefaultDesignObject() {
        return new PrinterDesignObject(
            getDefaultDesignProperties(),
            new LinkedList<>()
        );
    }
}
