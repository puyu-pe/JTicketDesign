package pe.puyu.SweetTicketDesign.domain.components.drawer;

import org.jetbrains.annotations.Nullable;

public record SweetOpenDrawerComponent(
    @Nullable SweetPinConnector pin,
    @Nullable Integer t1,
    @Nullable Integer t2
) {
}
