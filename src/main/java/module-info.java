module pe.puyu.jticketdesing{
  requires org.json;
  requires escpos.coffee;
  requires java.desktop;
  requires com.google.zxing;
  requires com.google.zxing.javase;
  exports pe.puyu.jticketdesing.core;
  exports pe.puyu.jticketdesing.util.escpos;
  exports pe.puyu.jticketdesing.util;
}
