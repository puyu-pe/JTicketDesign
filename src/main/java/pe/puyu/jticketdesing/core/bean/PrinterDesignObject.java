package pe.puyu.jticketdesing.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrinterDesignObject {
    private PrinterDesignProperties properties;
    private List<PrinterDesignBlock> data;
}
