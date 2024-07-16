package pe.puyu.jticketdesing.core.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrinterDesignProperties {
    private int blockWidth = 0;
    private boolean normalize = false;
    private String charCodeTable = "";

    public PrinterDesignProperties(PrinterDesignProperties printerDesignProperties){
        this.blockWidth = printerDesignProperties.blockWidth;
        this.normalize = printerDesignProperties.normalize;
        this.charCodeTable = printerDesignProperties.charCodeTable;
    }
}
