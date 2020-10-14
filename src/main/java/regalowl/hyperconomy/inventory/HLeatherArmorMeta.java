package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HLeatherArmorMeta extends HItemMeta {

	private HColor color;

	public HLeatherArmorMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, HColor color) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.color = color;
	}

	public HLeatherArmorMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		color = new HColor(data.get("color"));
	}

	public HLeatherArmorMeta(HLeatherArmorMeta meta) {
		super(meta);
		this.color = new HColor(meta.color);
	}

	public String serialize() {
		HashMap<String, String> data = super.getMap();
		data.put("color", color.serialize());
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.LEATHER_ARMOR;
	}

	public HColor getColor() {
		return color;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HLeatherArmorMeta)) {
			return false;
		}
		HLeatherArmorMeta hLeatherArmorMeta = (HLeatherArmorMeta) o;
		return super.equals(hLeatherArmorMeta) && Objects.equals(color, hLeatherArmorMeta.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), color);
	}
}