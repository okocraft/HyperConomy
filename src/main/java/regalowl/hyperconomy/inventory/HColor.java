package regalowl.hyperconomy.inventory;

import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HColor {

	private int red;
	private int green;
	private int blue;

	public HColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public String serialize() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("red", red + "");
		data.put("green", green + "");
		data.put("blue", blue + "");
		return CommonFunctions.implodeMap(data);
	}

	public HColor(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.red = Integer.parseInt(data.get("red"));
		this.green = Integer.parseInt(data.get("green"));
		this.blue = Integer.parseInt(data.get("blue"));
	}

	public HColor(HColor c) {
		this.red = c.red;
		this.green = c.green;
		this.blue = c.blue;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HColor)) {
			return false;
		}
		HColor hColor = (HColor) o;
		return red == hColor.red && green == hColor.green && blue == hColor.blue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(red, green, blue);
	}
}
