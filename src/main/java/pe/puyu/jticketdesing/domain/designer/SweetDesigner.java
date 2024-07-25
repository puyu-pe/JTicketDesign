package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputpayload.*;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignProperties;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMaker;
import pe.puyu.jticketdesing.domain.painter.DesignPainter;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

import java.util.*;

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
        String charCode = Optional.ofNullable(propertiesDto.charCode()).or(() -> Optional.ofNullable(defaultProperties.charCode())).orElse("");
        boolean normalize = Optional
            .ofNullable(propertiesDto.normalize())
            .or(() -> Optional.ofNullable(defaultProperties.normalize()))
            .or(() -> Optional.ofNullable(defaultProvider.getDefaultDesignStyle().normalize()))
            .orElse(false);
        SweetProperties properties = new SweetProperties(Math.max(blockWidth, 0), normalize, charCode);
        return new SweetDesignHelper(properties, defaultProvider.getDefaultDesignStyle());
    }

    private void printBlock(@Nullable PrinterDesignBlock block, @NotNull SweetDesignHelper helper) {
        if (block == null) return;
        if (block.imgPath() != null && !block.imgPath().isBlank()) {

        } else if (block.stringQR() != null && !block.stringQR().isBlank()) {

        } else {
            SweetTextBlock textBlock = makeTextBlock(block);
            SweetTable table = makeSweetTable(textBlock, helper);
            table = phase1CalcWidthAndNormalizeSpan(table, helper);
            table = phase2WrapRows(table, helper);
            phase3PrintRow(table, helper);
        }
    }

    private @NotNull SweetTextBlock makeTextBlock(@NotNull PrinterDesignBlock block) {
        PrinterDesignBlock defaultBlock = defaultProvider.getDefaultBlockValues();
        int gap = Math.max(Optional.ofNullable(block.gap()).or(() -> Optional.ofNullable(defaultBlock.gap())).orElse(1), 1);
        char separator = Optional.ofNullable(block.separator()).or(() -> Optional.ofNullable(defaultBlock.separator())).orElse(' ');
        int nColumns = Math.max(Optional.ofNullable(block.nColumns()).or(() -> Optional.ofNullable(defaultBlock.nColumns())).orElse(0), 0);
        var rows = Optional.ofNullable(block.rows()).orElse(new LinkedList<>());
        var styles = Optional.ofNullable(block.styles()).orElse(new HashMap<>());
        return new SweetTextBlock(gap, separator, nColumns, styles, rows);
    }

    private @NotNull SweetTable makeSweetTable(@NotNull SweetTextBlock block, @NotNull SweetDesignHelper helper) {
        SweetTableInfo tableInfo = new SweetTableInfo(block.gap(), block.separator(), Math.max(block.nColumns(), 0));
        SweetTable table = new SweetTable(tableInfo);
        List<SweetRow> printRows = block.rows().stream()
            .map(rowDto -> {
                List<PrinterDesignCell> cellRow = Optional.ofNullable(rowDto).orElse(new LinkedList<>());
                List<SweetCell> row = new LinkedList<>();
                for (int i = 0; i < cellRow.size(); ++i) {
                    PrinterDesignCell defaultCell = defaultProvider.getDefaultCellValues();
                    PrinterDesignCell cellDto = Optional.ofNullable(cellRow.get(i)).orElse(defaultCell);
                    String text = Optional.ofNullable(cellDto.text()).or(() -> Optional.ofNullable(defaultCell.text())).orElse("");
                    String className = Optional.ofNullable(cellDto.className()).or(() -> Optional.ofNullable(defaultCell.className())).orElse("");
                    PainterStyle painterStyle = helper.makePainterStyleFor(className, i, block.styles());
                    SweetStringStyle stringStyle = helper.makeSweetStringStyleFor(className, i, block.styles());
                    row.add(new SweetCell(text, 0, painterStyle, stringStyle));
                }
                SweetRow printRow = new SweetRow();
                printRow.addAll(row);
                return printRow;
            })
            .toList();
        table.addAll(printRows);
        return table;
    }

    private @NotNull SweetTable phase1CalcWidthAndNormalizeSpan(@NotNull SweetTable table, @NotNull SweetDesignHelper helper) {
        SweetTable newTable = new SweetTable(table.getInfo());
        for (SweetRow row : table) {
            SweetRow newRow = new SweetRow();
            int remainingWidth = helper.getProperties().blockWidth();
            int blockWidth = remainingWidth;
            int nColumns = table.getInfo().maxNumberOfColumns();
            int coveredColumns = 0;
            for (int i = 0; i < row.size(); ++i) {
                SweetCell cell = row.get(i);
                int span = Math.min(Math.max(cell.stringStyle().span(), 0), nColumns); // normalize span in range (0, max)
                int cellWidth = nColumns == 0 ? 0 : Math.min(span * blockWidth / nColumns, remainingWidth);
                int coverWidthByCell = cellWidth;
                boolean isLastItem = i + 1 >= row.size();
                coveredColumns += span;
                if (!isLastItem && (remainingWidth - cellWidth) > 0) { // is not the last item
                    int gap = table.getInfo().gap();
                    cellWidth = Math.max(cellWidth - gap, 0); // consider intermediate space
                }
                if (isLastItem && coveredColumns >= nColumns) {
                    cellWidth = Math.max(remainingWidth, 0); // cover all remaining width
                }
                remainingWidth -= coverWidthByCell;
                SweetStringStyle newStringStyle = new SweetStringStyle(
                    span,
                    cell.stringStyle().pad(),
                    cell.stringStyle().align(),
                    cell.stringStyle().normalize()
                );
                PainterStyle painterStyle = new PainterStyle(cell.painterStyle());
                newRow.add(new SweetCell(cell.text(), cellWidth, painterStyle, newStringStyle));
            }
            newTable.add(newRow);
        }
        return newTable;
    }

    private @NotNull SweetTable phase2WrapRows(@NotNull SweetTable table, @NotNull SweetDesignHelper helper) {
        SweetTable newTable = new SweetTable(table.getInfo());
        for (SweetRow row : table) {
            newTable.addAll(wrapRow(row, helper));
        }
        return newTable;
    }

    private void phase3PrintRow(@NotNull SweetTable table, @NotNull SweetDesignHelper helper) {
        SweetTableInfo tableInfo = table.getInfo();
        String separator = tableInfo.separator().toString();
        int gap = Math.max(tableInfo.gap(), 0);
        for (SweetRow row : table) {
            int remainingWidth = helper.getProperties().blockWidth();
            for (int i = 0; i < row.size(); ++i) {
                SweetCell cell = row.get(i);
                boolean isLastElement = i + 1 >= row.size();
                cell = helper.justify(cell);
                cell = helper.normalize(cell);
                if (!isLastElement) {
                    PainterStyle gapStyle = new PainterStyle(
                        1,
                        1,
                        cell.painterStyle().bold(),
                        cell.painterStyle().bgInverted(),
                        cell.painterStyle().charCode()
                    );
                    painter.print(cell.text(), cell.painterStyle());
                    remainingWidth -= cell.width();
                    if (remainingWidth > 0) {
                        painter.print(separator.repeat(gap), gapStyle);
                        remainingWidth -= gap;
                    }
                } else {
                    painter.println(cell.text(), cell.painterStyle());
                }
            }
        }
    }

    private @NotNull List<SweetRow> wrapRow(@NotNull SweetRow row, @NotNull SweetDesignHelper helper) {
        List<SweetRow> matrix = new LinkedList<>();
        int nColumns = 0;
        for (SweetCell cell : row) {
            SweetRow newRow = new SweetRow();
            List<String> wrappedText = helper.wrapText(cell.text(), cell.width(), cell.painterStyle().fontWidth());
            for (String text : wrappedText) {
                newRow.add(new SweetCell(
                    text,
                    cell.width(),
                    new PainterStyle(cell.painterStyle()),
                    new SweetStringStyle(cell.stringStyle())
                ));
            }
            if (newRow.size() > nColumns)
                nColumns = newRow.size();
            matrix.add(newRow);
        }
        List<SweetRow> wrappedRow = new LinkedList<>();
        for (int j = 0; j < nColumns; ++j) {
            SweetRow newRow = new SweetRow();
            for (SweetRow currentRow : matrix) {
                if (!currentRow.existsIndex(j)) {
                    if (currentRow.existsIndex(0)) {
                        SweetCell firstCell = currentRow.get(0);
                        newRow.add(new SweetCell(
                            "",
                            firstCell.width(),
                            new PainterStyle(firstCell.painterStyle()),
                            new SweetStringStyle(firstCell.stringStyle()))
                        );
                    }
                } else {
                    newRow.add(new SweetCell(currentRow.get(j)));

                }
            }
            wrappedRow.add(newRow);
        }
        return wrappedRow;
    }

}
