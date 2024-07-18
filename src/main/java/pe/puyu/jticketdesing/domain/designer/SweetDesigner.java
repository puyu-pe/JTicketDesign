package pe.puyu.jticketdesing.domain.designer;

import pe.puyu.jticketdesing.domain.inputpayload.*;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMaker;
import pe.puyu.jticketdesing.domain.painter.DesignPainter;

public class SweetDesigner {
    private final DesignObjectMaker maker;
    private final DesignPainter painter;
    private final DesignDefaultValuesProvider defaultProvider;

    public SweetDesigner(DesignObjectMaker maker, DesignPainter painter, DesignDefaultValuesProvider defaultProvider) {
        this.maker = maker;
        this.painter = painter;
        this.defaultProvider = defaultProvider;
    }

    public void paintDesign() {
        PrinterDesignObject designObject = maker.build();
    }

    public void printBlock(PrinterDesignBlock block, PrinterDesignProperties properties){

    }
}
