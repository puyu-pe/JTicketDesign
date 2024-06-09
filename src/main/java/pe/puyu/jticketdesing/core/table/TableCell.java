package pe.puyu.jticketdesing.core.table;

import pe.puyu.jticketdesing.util.escpos.StyleText;

public record TableCell(String text, int width, StyleText style) {
}
