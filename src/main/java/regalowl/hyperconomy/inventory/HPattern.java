package regalowl.hyperconomy.inventory;

import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HPattern {

	private String dyeColor;
	private String patternType;

	public HPattern(String dyeColor, String patternType) {
		this.dyeColor = dyeColor;
		this.patternType = patternType;
	}

	public String serialize() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("dyeColor", dyeColor);
		data.put("patternType", patternType);
		return CommonFunctions.implodeMap(data);
	}

	public HPattern(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.dyeColor = data.get("dyeColor");
		this.patternType = data.get("patternType");
	}

	public HPattern(HPattern hp) {
		this.dyeColor = hp.dyeColor;
		this.patternType = hp.patternType;
	}

	public String getDyeColor() {
		return dyeColor;
	}

	public String getPatternType() {
		return patternType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HPattern)) {
			return false;
		}
		HPattern hPattern = (HPattern) o;
		return Objects.equals(dyeColor, hPattern.dyeColor) && Objects.equals(patternType, hPattern.patternType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dyeColor, patternType);
	}

}
