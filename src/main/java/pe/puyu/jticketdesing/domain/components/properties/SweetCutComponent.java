package pe.puyu.jticketdesing.domain.components.properties;

import org.jetbrains.annotations.Nullable;

public record SweetCutComponent(
    @Nullable Integer feed,
    @Nullable SweetCutMode mode
) {
}
