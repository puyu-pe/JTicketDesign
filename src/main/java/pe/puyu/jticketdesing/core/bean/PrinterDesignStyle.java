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
    private int fontWidth = 1;
    private int fontHeight = 1;
    private boolean bold = false;
    private boolean normalize = false;
    private boolean bgInverted = true;
    private char pad = ' ';
    private PrinterJustifyAlign align = PrinterJustifyAlign.LEFT;
    private int span = 1;

    public PrinterDesignStyle(PrinterDesignStyle printerDesignStyle){
        this.fontWidth = printerDesignStyle.fontWidth;
        this.fontHeight = printerDesignStyle.fontHeight;
        this.bold = printerDesignStyle.bold;
        this.normalize = printerDesignStyle.normalize;
        this.bgInverted = printerDesignStyle.bgInverted;
        this.pad = printerDesignStyle.pad;
        this.align = printerDesignStyle.align;
        this.span = printerDesignStyle.span;
    }
}
