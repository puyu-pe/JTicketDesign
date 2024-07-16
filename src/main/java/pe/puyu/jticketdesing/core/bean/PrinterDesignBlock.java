package pe.puyu.jticketdesing.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrinterDesignBlock {
    private int gap = 0;
    private char separator = ' ';
    private String stringQR = "";
    private String imgPath = "";
    private Map<String, PrinterDesignStyle> styles = new HashMap<>();
    private int nColumns = 0;
    private List<List<PrinterDesignCell>> rows = new LinkedList<>();

    public PrinterDesignBlock(PrinterDesignBlock printerDesignBlock) {
        this.gap = printerDesignBlock.gap;
        this.separator = printerDesignBlock.separator;
        this.stringQR = printerDesignBlock.stringQR;
        this.imgPath = printerDesignBlock.imgPath;
        this.styles = new HashMap<>(printerDesignBlock.styles);
        this.nColumns = printerDesignBlock.nColumns;
        this.rows = new LinkedList<>(printerDesignBlock.rows);
    }

    public void addRow(List<PrinterDesignCell> row){
        this.rows.add(row);
    }

    public void addStyle(String className, PrinterDesignStyle style){
        this.styles.put(className, style);
    }

    public void addStyles(Map<String, PrinterDesignStyle> styles){
        this.styles.putAll(styles);
    }

    public void addRows(List<List<PrinterDesignCell>> rows){
        this.rows.addAll(rows);
    }

}
