package pe.puyu.jticketdesing.application;

import pe.puyu.jticketdesing.domain.inputpayload.*;

import java.util.HashMap;
import java.util.LinkedList;

public class SimpleDesignDefaultProvider implements DesignDefaultValuesProvider {

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
            6,
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
