package pe.puyu.jticketdesing.domain.inputpayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrinterDesignCell {
    private String className = "";
    private String text = "";

    public PrinterDesignCell(PrinterDesignCell printerDesignCell) {
        this.className = printerDesignCell.className;
        this.text = printerDesignCell.text;
    }

}