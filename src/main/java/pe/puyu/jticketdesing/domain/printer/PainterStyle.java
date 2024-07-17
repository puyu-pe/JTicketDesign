package pe.puyu.jticketdesing.domain.printer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PainterStyle {
    private int fontWidth;
    private int fontHeight;
    private boolean bold;
    private boolean bgInverted;
}
