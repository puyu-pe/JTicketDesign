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
    private int blockWidth;
    private boolean normalize;
    private String charCodeTable;
}
