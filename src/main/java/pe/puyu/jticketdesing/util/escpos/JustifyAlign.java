package pe.puyu.jticketdesing.util.escpos;

public enum JustifyAlign {
	CENTER("CENTER"),
	LEFT("LEFT"),
	RIGHT("RIGHT");

	private final String value;

	JustifyAlign(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static JustifyAlign fromValue(String value) {
		for (JustifyAlign type : JustifyAlign.values()) {
			if (type.value.equalsIgnoreCase(value)) {
				return type;
			}
		}
		return LEFT;
	}
}
