module pe.puyu.jticketdesing {
    requires com.google.gson;
    requires escpos.coffee;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.jetbrains.annotations;
    exports pe.puyu.SweetTicketDesign.domain.designer;
    exports pe.puyu.SweetTicketDesign.application.builder.gson;
    exports pe.puyu.SweetTicketDesign.application.components;
    exports pe.puyu.SweetTicketDesign.application.printer.escpos;
    exports pe.puyu.SweetTicketDesign.domain.table.util.escpos;
    exports pe.puyu.SweetTicketDesign.domain.table;
    exports pe.puyu.SweetTicketDesign.domain.table.util;
}
