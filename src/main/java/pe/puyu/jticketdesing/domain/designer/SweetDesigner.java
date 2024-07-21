package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputpayload.*;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignProperties;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMaker;
import pe.puyu.jticketdesing.domain.painter.DesignPainter;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

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
        List<PrinterDesignBlock> blocks = Optional
            .ofNullable(designObject.blocks())
            .or(() -> Optional.ofNullable(defaultDesignObject.blocks()))
            .orElse(new LinkedList<>());
        SweetDesignHelper helper = makeSweetHelper(designObject.properties());
        blocks.forEach(block -> printBlock(block, helper));
    }

    private @NotNull SweetDesignHelper makeSweetHelper(@Nullable PrinterDesignProperties propertiesDto) {
        PrinterDesignProperties defaultProperties = defaultProvider.getDefaultDesignProperties();
        propertiesDto = Optional.ofNullable(propertiesDto).orElse(defaultProperties);
        int blockWidth = Optional.ofNullable(propertiesDto.blockWidth()).or(() -> Optional.ofNullable(defaultProperties.blockWidth())).orElse(0);
        boolean normalize = Optional.ofNullable(propertiesDto.normalize()).or(() -> Optional.ofNullable(defaultProperties.normalize())).orElse(false);
        String charCode = Optional.ofNullable(propertiesDto.charCode()).or(() -> Optional.ofNullable(defaultProperties.charCode())).orElse("");
        SweetProperties properties = new SweetProperties(Math.max(blockWidth, 0), normalize, charCode);
        return new SweetDesignHelper(properties, defaultProvider.getDefaultDesignStyle());
    }

    private void printBlock(@Nullable PrinterDesignBlock block, @NotNull SweetDesignHelper helper) {
        if (block == null) return;

        //print qr, img, text
    }

    private @NotNull SweetTable makeSweetTable(@NotNull PrinterDesignBlock block, @NotNull SweetDesignHelper helper) {
        SweetTableInfo tableInfo = makeSweetTableInfo(block);
        SweetTable table = new SweetTable(tableInfo);
        var rows = Optional.ofNullable(block.rows()).orElse(new LinkedList<>());
        List<SweetRow> printRows = rows.stream()
            .map(rowDto -> {
                List<PrinterDesignCell> cellRow = Optional.ofNullable(rowDto).orElse(new LinkedList<>());
                List<SweetCell> row = new LinkedList<>();
                for (int i = 0; i < cellRow.size(); ++i) {
                    PrinterDesignCell defaultCell = defaultProvider.getDefaultCellValues();
                    PrinterDesignCell cellDto = Optional.ofNullable(cellRow.get(i)).orElse(defaultCell);
                    String text = Optional.ofNullable(cellDto.text()).or(() -> Optional.ofNullable(defaultCell.text())).orElse("");
                    String className = Optional.ofNullable(cellDto.className()).or(() -> Optional.ofNullable(defaultCell.className())).orElse("");
//                    PainterStyle painterStyle = helper.makePainterStyleFor(className, i, block.styles());
//                    SweetStringStyle stringStyle = helper.makeSweetStringStyleFor(className, i, block.styles());
//                    row.add(new SweetCell(text, 0, painterStyle, stringStyle));
                }
                SweetRow printRow = new SweetRow();
                printRow.addAll(row);
                return printRow;
            })
            .toList();
        table.addAll(printRows);
        return table;
    }

    private @NotNull SweetTableInfo makeSweetTableInfo(@NotNull PrinterDesignBlock block) {
        PrinterDesignBlock defaultBlock = defaultProvider.getDefaultBlockValues();
        int gap = Math.max(Optional.ofNullable(block.gap()).or(() -> Optional.ofNullable(defaultBlock.gap())).orElse(1), 0);
        char separator = Optional.ofNullable(block.separator()).or(() -> Optional.ofNullable(defaultBlock.separator())).orElse(' ');
        int nColumns = Math.max(Optional.ofNullable(block.nColumns()).or(() -> Optional.ofNullable(defaultBlock.nColumns())).orElse(0), 0);
        return new SweetTableInfo(gap, separator, nColumns);
    }

    private @NotNull SweetTable phase1CalcWidthAndNormalizeSpan(@NotNull SweetTable table, @NotNull SweetDesignHelper helper) {
        SweetTable newTable = new SweetTable(table.getInfo());
        for (SweetRow row : table) {
            SweetRow newRow = new SweetRow();
            for (SweetCell cell : row) {
                int maxNumberOfColumns = table.getInfo().maxNumberOfColumns();
                int blockWidth = helper.getProperties().blockWidth();
                int span = Math.min(Math.max(cell.stringStyle().span(), 0), maxNumberOfColumns); // normalize span in range (0, max)
                int widthPerCell = maxNumberOfColumns <= 0 ? 0 : blockWidth / maxNumberOfColumns;
                SweetStringStyle newStringStyle = new SweetStringStyle(
                    span,
                    cell.stringStyle().pad(),
                    cell.stringStyle().align(),
                    cell.stringStyle().normalize()
                );
                PainterStyle painterStyle = new PainterStyle(cell.painterStyle());
                newRow.add(new SweetCell(cell.text(), widthPerCell * span, painterStyle, newStringStyle));
            }
            newTable.add(newRow);
        }
        return newTable;
    }

    private @NotNull SweetTable phase2WrapCell(@NotNull SweetTable table) {
        SweetTable newTable = new SweetTable(table.getInfo());
        for (SweetRow row : table) {

        }
        return newTable;
    }


}
