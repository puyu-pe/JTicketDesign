module pe.puyu.jticketdesing{
  requires com.google.gson;
  requires escpos.coffee;
  requires java.desktop;
  requires com.google.zxing;
  requires com.google.zxing.javase;
  exports pe.puyu.jticketdesing.core;
  exports pe.puyu.jticketdesing.util.escpos;
  exports pe.puyu.jticketdesing.util;
	exports pe.puyu.jticketdesing.metadata;
}
