package pe.puyu.jticketdesing.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrinterDesignBlock {
    private int gap;
    private char separator;
    private String stringQR;
    private String imgPath;
    private Map<String, PrinterDesignStyle> styles;
    private int nColumns;
    private List<List<PrinterDesignCell>> rows;
}
