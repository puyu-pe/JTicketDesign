package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

public record SweetCell(
    @NotNull String text,
    @NotNull PainterStyle painterStyle,
    @NotNull SweetStringStyle stringStyle
) {

}
