package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;

public record SweetTableInfo(
    @NotNull Integer gap,
    @NotNull Character separator,
    @NotNull Integer maxNumberOfColumns
) {

    public SweetTableInfo (SweetTableInfo tableInfo){
        this(tableInfo.gap, tableInfo.separator, tableInfo.maxNumberOfColumns);
    }
}
