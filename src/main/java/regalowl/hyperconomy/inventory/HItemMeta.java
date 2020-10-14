package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;
import regalowl.hyperconomy.account.HyperPlayer;

public class HItemMeta {

	protected String displayName;
	protected ArrayList<String> lore = new ArrayList<String>();
	protected ArrayList<HEnchantment> enchantments = new ArrayList<HEnchantment>();
	protected ArrayList<HItemFlag> itemFlags = new ArrayList<HItemFlag>();
	protected boolean unbreakable;
	protected int repairCost;

	public HItemMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost) {
		this.displayName = displayName;
		this.lore = lore;
		this.enchantments = enchantments;
		this.itemFlags = itemFlags;
		this.unbreakable = unbreakable;
		this.repairCost = repairCost;
	}

	/**
	 * Provides deep clone of given HItemMeta
	 * 
	 * @param meta
	 */
	public HItemMeta(HItemMeta meta) {
		this.displayName = meta.displayName;
		this.lore = new ArrayList<String>(meta.lore);
		for (HEnchantment he : meta.enchantments) {
			this.enchantments.add(new HEnchantment(he));
		}
		for (HItemFlag hif : meta.itemFlags) {
			this.itemFlags.add(new HItemFlag(hif));
		}
		this.unbreakable = meta.unbreakable;
		this.repairCost = meta.repairCost;
	}

	public HItemMeta(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.displayName = (data.get("displayName") != null) ? data.get("displayName") : "";
		this.lore = CommonFunctions.explode(data.get("lore"));
		ArrayList<String> sEnchants = CommonFunctions.explode(data.get("enchantments"));
		for (String e : sEnchants) {
			enchantments.add(new HEnchantment(e));
		}
		ArrayList<String> sItemFlags = CommonFunctions.explode(data.get("itemFlags"));
		for (String f : sItemFlags) {
			itemFlags.add(HItemFlag.deserialize(f));
		}
		this.unbreakable = (data.get("unbreakable") != null) ? Boolean.parseBoolean(data.get("unbreakable")) : false;
		this.repairCost = (data.get("repairCost") != null) ? Integer.parseInt(data.get("repairCost")) : 0;
	}

	public String serialize() {
		return CommonFunctions.implodeMap(getMap());
	}

	public HashMap<String, String> getMap() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("displayName", displayName);
		data.put("lore", CommonFunctions.implode(lore));
		ArrayList<String> sEnchants = new ArrayList<String>();
		for (HEnchantment e : enchantments) {
			sEnchants.add(e.serialize());
		}
		data.put("enchantments", CommonFunctions.implode(sEnchants));
		ArrayList<String> sItemFlags = new ArrayList<String>();
		for (HItemFlag f : itemFlags) {
			sItemFlags.add(f.serialize());
		}
		data.put("itemFlags", CommonFunctions.implode(sItemFlags));
		data.put("unbreakable", unbreakable + "");
		data.put("repairCost", repairCost + "");
		return data;
	}

	public ArrayList<String> displayInfo(HyperPlayer p, String color1, String color2) {
		ArrayList<String> info = new ArrayList<String>();
		info.add(color1 + "Meta Type: " + color2 + getType().toString());
		info.add(color1 + "Display Name: " + color2 + displayName);
		String loreString = "";
		if (lore != null && lore.size() > 0) {
			for (String l : lore) {
				loreString += l + ",";
			}
			loreString = loreString.substring(0, loreString.length() - 1);
		}
		info.add(color1 + "Lore: " + color2 + loreString);
		String enchantString = "";
		if (enchantments != null && enchantments.size() > 0) {
			for (HEnchantment se : enchantments) {
				enchantString += se.getEnchantmentName() + ",";
			}
			enchantString = enchantString.substring(0, enchantString.length() - 1);
		}
		info.add(color1 + "Enchantments: " + color2 + enchantString);
		String itemFlagString = "";
		if (itemFlags != null && itemFlags.size() > 0) {
			for (HItemFlag f : itemFlags) {
				itemFlagString += f.getItemFlag() + ",";
			}
			itemFlagString = itemFlagString.substring(0, itemFlagString.length() - 1);
		}
		info.add(color1 + "Item flags: " + color2 + itemFlagString);
		info.add(color1 + "Unbreakable: " + color2 + unbreakable);
		info.add(color1 + "Repair Cost: " + color2 + repairCost);
		return info;
	}

	public HItemMetaType getType() {
		return HItemMetaType.ITEM;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String name) {
		this.displayName = name;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}

	public void addLore(String string) {
		lore.add(string);
	}

	public List<HEnchantment> getEnchantments() {
		return enchantments;
	}

	public boolean hasEnchantments() {
		if (enchantments.size() > 0)
			return true;
		return false;
	}

	public void addEnchantment(HEnchantment e) {
		if (!containsEnchantment(e))
			enchantments.add(e);
	}

	public void removeEnchantment(HEnchantment e) {
		if (containsEnchantment(e))
			enchantments.remove(e);
	}

	public boolean containsEnchantment(HEnchantment e) {
		for (HEnchantment se : enchantments) {
			if (se.equals(e))
				return true;
		}
		return false;
	}

	public List<HItemFlag> getItemFlags() {
		return itemFlags;
	}

	public boolean hasItemFlags() {
		if (itemFlags.size() > 0)
			return true;
		return false;
	}

	public void addItemFlag(HItemFlag f) {
		if (!containsItemFlag(f))
			itemFlags.add(f);
	}

	public void removeItemFlag(HItemFlag f) {
		if (containsItemFlag(f))
			itemFlags.remove(f);
	}

	public boolean containsItemFlag(HItemFlag f) {
		for (HItemFlag fl : itemFlags) {
			if (fl.equals(f))
				return true;
		}
		return false;
	}

	public boolean unbreakable() {
		return unbreakable;
	}

	public void setUnbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
	}

	public int getRepairCost() {
		return repairCost;
	}

	public void setRepairCost(int repairCost) {
		this.repairCost = repairCost;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HItemMeta)) {
			return false;
		}
		HItemMeta hItemMeta = (HItemMeta) o;
		return Objects.equals(displayName, hItemMeta.displayName) && Objects.equals(lore, hItemMeta.lore)
				&& Objects.equals(enchantments, hItemMeta.enchantments)
				&& Objects.equals(itemFlags, hItemMeta.itemFlags) && unbreakable == hItemMeta.unbreakable
				&& repairCost == hItemMeta.repairCost;
	}

	@Override
	public int hashCode() {
		return Objects.hash(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
	}
}