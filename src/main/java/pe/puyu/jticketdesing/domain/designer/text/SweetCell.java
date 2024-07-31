package pe.puyu.jticketdesing.domain.designer.text;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.printer.SweetPrinterStyle;

public record SweetCell(
    @NotNull String text,
    @NotNull Integer width,
    @NotNull SweetPrinterStyle printerStyle,
    @NotNull SweetStringStyle stringStyle
) {

    public SweetCell(SweetCell otherCell){
        this(
            otherCell.text,
            otherCell.width,
            new SweetPrinterStyle(otherCell.printerStyle),
            new SweetStringStyle(otherCell.stringStyle)
        );
    }

    //immutable method
    public SweetCell setWidth(int width){
        return new SweetCell(
            this.text,
            Math.max(0, width),
            new SweetPrinterStyle(this.printerStyle),
            new SweetStringStyle(this.stringStyle)
        );
    }

}
