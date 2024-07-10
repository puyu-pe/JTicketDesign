package pe.puyu.jticketdesing.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrinterDesignStyle {
    private int fontWidth;
    private int fontHeight;
    private boolean bold;
    private boolean normalize;
    private boolean bgInverted;
    private char pad;
    private PrinterJustifyAlign align;
    private int span;
}
