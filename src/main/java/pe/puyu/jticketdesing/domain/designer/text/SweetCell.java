package pe.puyu.jticketdesing.domain.designer.text;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

public record SweetCell(
    @NotNull String text,
    @NotNull Integer width,
    @NotNull PainterStyle painterStyle,
    @NotNull SweetStringStyle stringStyle
) {

    public SweetCell(SweetCell otherCell){
        this(
            otherCell.text,
            otherCell.width,
            new PainterStyle(otherCell.painterStyle),
            new SweetStringStyle(otherCell.stringStyle)
        );
    }

    //immutable method
    public SweetCell setWidth(int width){
        return new SweetCell(
            this.text,
            Math.max(0, width),
            new PainterStyle(this.painterStyle),
            new SweetStringStyle(this.stringStyle)
        );
    }

}
