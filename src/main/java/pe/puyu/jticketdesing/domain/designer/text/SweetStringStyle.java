package pe.puyu.jticketdesing.domain.designer.text;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.block.SweetJustify;

public record SweetStringStyle(
    @NotNull Integer span,
    @NotNull Character pad,
    @NotNull SweetJustify align,
    @NotNull Boolean normalize
) {
    public SweetStringStyle(SweetStringStyle stringStyle) {
        this(
            stringStyle.span,
            stringStyle.pad,
            stringStyle.align,
            stringStyle.normalize
        );
    }
}
