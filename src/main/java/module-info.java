module pe.puyu.jticketdesing {
    requires com.google.gson;
    requires escpos.coffee;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires static lombok;
    requires org.jetbrains.annotations;
    exports pe.puyu.jticketdesing.domain;
    exports pe.puyu.jticketdesing.util.escpos;
    exports pe.puyu.jticketdesing.util;
    exports pe.puyu.jticketdesing.metadata;
    exports pe.puyu.jticketdesing.domain.table;
}
