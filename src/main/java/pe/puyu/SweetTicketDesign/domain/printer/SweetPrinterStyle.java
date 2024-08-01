package pe.puyu.SweetTicketDesign.domain.printer;

import org.jetbrains.annotations.NotNull;

public record SweetPrinterStyle(
    @NotNull Integer fontWidth,
    @NotNull Integer fontHeight,
    @NotNull Boolean bold,
    @NotNull Boolean bgInverted,
    @NotNull String charCode
) {

    public SweetPrinterStyle(SweetPrinterStyle otherSweetPrinterStyle){
        this(
            otherSweetPrinterStyle.fontWidth,
            otherSweetPrinterStyle.fontHeight,
            otherSweetPrinterStyle.bold,
            otherSweetPrinterStyle.bgInverted,
            otherSweetPrinterStyle.charCode
        );
    }
}
