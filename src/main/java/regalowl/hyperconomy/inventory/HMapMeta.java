package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HMapMeta extends HItemMeta {

	private boolean isScaling;

	public HMapMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, boolean isScaling) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.isScaling = isScaling;
	}

	public HMapMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		isScaling = Boolean.parseBoolean(data.get("isScaling"));
	}

	public HMapMeta(HMapMeta meta) {
		super(meta);
		isScaling = meta.isScaling;
	}

	public String serialize() {
		HashMap<String, String> data = super.getMap();
		data.put("isScaling", isScaling + "");
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.MAP;
	}

	public boolean isScaling() {
		return isScaling;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HMapMeta)) {
			return false;
		}
		HMapMeta hMapMeta = (HMapMeta) o;
		return super.equals(hMapMeta) && isScaling == hMapMeta.isScaling;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), isScaling);
	}
}