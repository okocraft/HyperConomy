package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HSkullMeta extends HItemMeta {

	private String owner;

	public HSkullMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, String owner) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.owner = owner;
	}

	public HSkullMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.owner = data.get("owner");
	}

	public HSkullMeta(HSkullMeta meta) {
		super(meta);
		this.owner = meta.owner;
	}

	@Override
	public String serialize() {
		HashMap<String, String> data = super.getMap();
		data.put("owner", owner);
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.SKULL;
	}

	public String getOwner() {
		return owner;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HSkullMeta)) {
			return false;
		}
		HSkullMeta hSkullMeta = (HSkullMeta) o;
		return super.equals(hSkullMeta) && Objects.equals(owner, hSkullMeta.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), owner);
	}
}