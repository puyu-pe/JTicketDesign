package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.*;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMaker;
import pe.puyu.jticketdesing.domain.painter.DesignPainter;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

import javax.swing.text.html.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SweetDesigner {
    private final @NotNull DesignObjectMaker maker;
    private final @NotNull DesignPainter painter;
    private final @NotNull DesignDefaultValuesProvider defaultProvider;

    public SweetDesigner(@NotNull DesignObjectMaker maker, @NotNull DesignPainter painter, @NotNull DesignDefaultValuesProvider defaultProvider) {
        this.maker = maker;
        this.painter = painter;
        this.defaultProvider = defaultProvider;
    }

    public void paintDesign() {
        PrinterDesignObject designObject = maker.build();
        PrinterDesignObject defaultDesignObject = defaultProvider.getDefaultDesignObject();
        var blocks = Optional
            .ofNullable(designObject.blocks())
            .or(() ->  Optional.ofNullable(defaultDesignObject.blocks()))
            .orElse(new LinkedList<>());
        var properties = Optional.ofNullable(designObject.properties()).orElse(defaultProvider.getDefaultDesignProperties());
        blocks.forEach(block -> printBlock(block, properties));
    }

    private void printBlock(@NotNull PrinterDesignBlock block, @NotNull PrinterDesignProperties properties) {
        //print qr, img, text
    }

    private @NotNull SweetTable makeSweetTable(@NotNull PrinterDesignBlock block, @NotNull SweetSearchEngineStyle searchEngineStyle) {
        SweetTable table = new SweetTable(); // FIXME: agregar el TableInfo
        var rows = Optional.ofNullable(block.rows()).orElse(new LinkedList<>());
        List<SweetRow> printRows = rows.stream().map( rowDto -> {
            List<SweetCell> row = Optional
                .ofNullable(rowDto)
                .orElse(new LinkedList<>())
                .stream()
                .map(cellDto -> {
                    cellDto = Optional.ofNullable(cellDto).orElse(defaultProvider.getDefaultCellValues());
                    String text = Optional.ofNullable(cellDto.text()).orElse("");
                    String className = Optional.ofNullable(cellDto.className()).orElse("");
                    PainterStyle painterStyle = searchEngineStyle.makePainterStyleFor(className, 0);
                    SweetStringStyle stringStyle = searchEngineStyle.makeSweetStringStyleFor(className, 0);
                    return new SweetCell(text, painterStyle, stringStyle);
                }).toList();
            SweetRow printRow = new SweetRow();
            printRow.addAll(row);
            return printRow;
        }).toList();
        table.addAll(printRows);
        return table;
    }



}
