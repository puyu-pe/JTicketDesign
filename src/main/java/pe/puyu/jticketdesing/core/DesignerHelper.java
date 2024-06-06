package pe.puyu.jticketdesing.core;

import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.escpos.StyleText;
import pe.puyu.jticketdesing.util.escpos.StyleTextBuilder;

public class DesignerHelper<T extends  PrinterPropertiesReader> {
	private final T propertiesReader;

	public DesignerHelper(T propertiesReader) {
		this.propertiesReader = propertiesReader;
	}

	// se considera el hecho de que el texto deba estar normalizado.
	public StyleTextBuilder styleBuilder(){
		return StyleText.builder().normalize(propertiesReader.textNormalize());
	}

	public T properties(){
		return propertiesReader;
	}
	// calcula el ancho del papel en pixeles
	public int calcWidthPaperInPx() {
		// 290 y 25 ,son valores referenciales ya que se vio que
		// 25 caracteres son 290px aprox.
		// Se aplica la regla de 3 simple 25 -> 290 => width = x;
		return (290 * (propertiesReader.width() + 2)) / 25;
	}

}
