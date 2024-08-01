package pe.puyu.jticketdesing.domain.components.properties;

import org.jetbrains.annotations.Nullable;

public record SweetPropertiesComponent(
    @Nullable Integer blockWidth,
    @Nullable Boolean normalize,
    @Nullable String charCode,
    @Nullable SweetCutComponent cutMode
) {

}
