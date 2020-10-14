package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HSpawnEggMeta extends HItemMeta {

	private String entityType;

	public HSpawnEggMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, String entityType) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.entityType = entityType;
	}

	public HSpawnEggMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.entityType = data.get("entityType");
	}

	public HSpawnEggMeta(HSpawnEggMeta meta) {
		super(meta);
		this.entityType = meta.entityType;
	}

	@Override
	public String serialize() {
		HashMap<String, String> data = super.getMap();
		data.put("entityType", entityType);
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.SPAWN_EGG;
	}

	public String getEntityType() {
		return entityType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HSpawnEggMeta)) {
			return false;
		}
		HSpawnEggMeta hSpawnEggMeta = (HSpawnEggMeta) o;
		return super.equals(hSpawnEggMeta) && Objects.equals(entityType, hSpawnEggMeta.entityType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), entityType);
	}

}