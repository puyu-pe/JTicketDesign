package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignProperties;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignStyle;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

public class SweetSearchEngineStyle {

    private final @NotNull PrinterDesignProperties _properties;
    private final @NotNull PrinterDesignStyle _defaultStyle;

    public SweetSearchEngineStyle(@NotNull PrinterDesignProperties properties, @NotNull PrinterDesignStyle defaultStyle) {
        this._properties = properties;
        this._defaultStyle = defaultStyle;
    }

    public @NotNull PainterStyle makePainterStyleFor(@NotNull String className, @NotNull Integer index) {
        return new PainterStyle();
    }

    public @NotNull SweetStringStyle makeSweetStringStyleFor(@NotNull String className, @NotNull Integer index) {

    }

}
