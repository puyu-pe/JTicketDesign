module pe.puyu.jticketdesing {
    requires com.google.gson;
    requires escpos.coffee;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires org.jetbrains.annotations;
    exports pe.puyu.jticketdesing.domain.designer;
    exports pe.puyu.jticketdesing.application.builder.gson;
    exports pe.puyu.jticketdesing.application.components;
    exports pe.puyu.jticketdesing.application.printer.escpos;
    exports pe.puyu.jticketdesing.domain.table.util.escpos;
    exports pe.puyu.jticketdesing.domain.table;
    exports pe.puyu.jticketdesing.domain.table.util;
}
