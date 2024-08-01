package pe.puyu.SweetTicketDesign.domain.table;

import pe.puyu.SweetTicketDesign.domain.table.util.escpos.StyleText;

@Deprecated(since = "0.1.0", forRemoval = true)
public record TableCell(String text, int width, StyleText style) {
}
