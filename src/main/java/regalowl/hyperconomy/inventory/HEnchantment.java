package regalowl.hyperconomy.inventory;

import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HEnchantment {
	private String enchantment;
	private int lvl;

	public HEnchantment(String enchantment, int lvl) {
		this.enchantment = enchantment;
		this.lvl = lvl;
	}

	public HEnchantment(HEnchantment he) {
		this.enchantment = he.enchantment;
		this.lvl = he.lvl;
	}

	public String serialize() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("enchantment", enchantment);
		data.put("lvl", lvl + "");
		return CommonFunctions.implodeMap(data);
	}

	public HEnchantment(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.enchantment = data.get("enchantment");
		this.lvl = Integer.parseInt(data.get("lvl"));
	}

	public String getEnchantmentName() {
		return enchantment;
	}

	public int getLvl() {
		return lvl;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HEnchantment)) {
			return false;
		}
		HEnchantment hEnchantment = (HEnchantment) o;
		return Objects.equals(enchantment, hEnchantment.enchantment) && lvl == hEnchantment.lvl;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enchantment, lvl);
	}

}