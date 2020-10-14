package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.simpledatalib.CommonFunctions;

public class HBannerMeta extends HItemMeta {

	private String baseColor;
	private ArrayList<HPattern> patterns = new ArrayList<HPattern>();

	public HBannerMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, String baseColor,
			ArrayList<HPattern> patterns) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.patterns = patterns;
		this.baseColor = baseColor;
	}

	public HBannerMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		ArrayList<String> stringPatterns = CommonFunctions.explode(data.get("patterns"));
		for (String hp : stringPatterns) {
			patterns.add(new HPattern(hp));
		}
		this.baseColor = data.get("baseColor");
	}

	public HBannerMeta(HBannerMeta meta) {
		super(meta);
		for (HPattern hp : meta.patterns) {
			patterns.add(new HPattern(hp));
		}
		this.baseColor = meta.baseColor;
	}

	@Override
	public String serialize() {
		HashMap<String, String> data = super.getMap();
		data.put("baseColor", baseColor);
		ArrayList<String> stringPatterns = new ArrayList<String>();
		for (HPattern hp : patterns) {
			stringPatterns.add(hp.serialize());
		}
		data.put("patterns", CommonFunctions.implode(stringPatterns));
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public ArrayList<String> displayInfo(HyperPlayer p, String color1, String color2) {
		ArrayList<String> info = super.displayInfo(p, color1, color2);
		info.add(color1 + "Base Color: " + color2 + displayName);
		String patternString = "";
		if (patterns != null && patterns.size() > 0) {
			for (HPattern pat : patterns) {
				patternString += pat.getDyeColor() + "," + pat.getPatternType() + ";";
			}
			patternString = patternString.substring(0, patternString.length() - 1);
		}
		info.add(color1 + "Patterns: " + color2 + patternString);
		return info;
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.BANNER;
	}

	public ArrayList<HPattern> getPatterns() {
		return patterns;
	}

	public String getBaseColor() {
		return baseColor;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HBannerMeta)) {
			return false;
		}
		HBannerMeta hBannerMeta = (HBannerMeta) o;
		return super.equals(hBannerMeta) && Objects.equals(baseColor, hBannerMeta.baseColor) && Objects.equals(patterns, hBannerMeta.patterns);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), baseColor, patterns);
	}

}
