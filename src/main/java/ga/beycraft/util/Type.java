package ga.beycraft.util;

public class Type {

	private BeyTypes type;
	private float[] values;

	public Type(String input) {
		type = BeyTypes.getByName(input.substring(0, input.indexOf('(')));
		String[] valuesStr = input.substring(input.indexOf('(')+1, input.indexOf(')')).split(",");
		values = new float[valuesStr.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = Float.valueOf(valuesStr[i]);
		}
	}

	public BeyTypes getType() {
		return type;
	}

	public float[] getValues() {
		return values;
	}
}
