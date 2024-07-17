package pe.puyu.jticketdesing.domain.inputpayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrinterDesignObject {

    private PrinterDesignProperties properties = new PrinterDesignProperties();
    private List<PrinterDesignBlock> data = new LinkedList<>();

    public PrinterDesignObject(PrinterDesignObject printerDesignObject) {
        this.properties = new PrinterDesignProperties(printerDesignObject.properties);
        this.data = new LinkedList<>(data);
    }

    public void addData(List<PrinterDesignBlock> data) {
        this.data.addAll(data);
    }

}
