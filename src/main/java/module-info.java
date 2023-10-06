module pe.puyu.jticketdesing{
  requires transitive org.json;
  requires escpos.coffee;
  requires java.desktop;
  requires com.google.zxing;
  requires com.google.zxing.javase;
  exports pe.puyu.jticketdesing.core;
}
