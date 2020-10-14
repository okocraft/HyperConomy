package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.simpledatalib.CommonFunctions;

public class HPotionMeta extends HItemMeta {

	private ArrayList<HPotionEffect> potionEffects = new ArrayList<HPotionEffect>();
	private HPotionData potionData;

	public HPotionMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, ArrayList<HPotionEffect> potionEffects,
			HPotionData potionData) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.potionEffects = potionEffects;
		this.potionData = potionData;
	}

	public HPotionMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		ArrayList<String> sEffects = CommonFunctions.explode(data.get("potionEffects"));
		for (String e : sEffects) {
			potionEffects.add(new HPotionEffect(e));
		}
		this.potionData = new HPotionData(data.get("potionData"));
	}

	public HPotionMeta(HPotionMeta meta) {
		super(meta);
		for (HPotionEffect pe : meta.potionEffects) {
			potionEffects.add(new HPotionEffect(pe));
		}
		this.potionData = new HPotionData(meta.potionData);
	}

	public String serialize() {
		HashMap<String, String> data = super.getMap();
		ArrayList<String> sEffects = new ArrayList<String>();
		for (HPotionEffect e : potionEffects) {
			sEffects.add(e.serialize());
		}
		data.put("potionEffects", CommonFunctions.implode(sEffects));
		data.put("potionData", potionData.serialize());
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public ArrayList<String> displayInfo(HyperPlayer p, String color1, String color2) {
		ArrayList<String> info = super.displayInfo(p, color1, color2);
		String potionEffectString = "";
		if (potionEffects != null && potionEffects.size() > 0) {
			for (HPotionEffect pe : potionEffects) {
				potionEffectString += "Type:" + pe.getType() + "," + "Amplifier:" + pe.getAmplifier() + ","
						+ "Duration:" + pe.getDuration() + "," + "Ambient:" + pe.isAmbient() + ";";
			}
			potionEffectString = potionEffectString.substring(0, potionEffectString.length() - 1);
		}
		info.add(color1 + "Potion Effects: " + color2 + potionEffectString);
		info.add(color1 + "Potion Data: " + color2 + "Type:" + potionData.getPotionType() + ",Extended:"
				+ potionData.isExtended() + ",Upgraded:" + potionData.isUpgraded());
		return info;
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.POTION;
	}

	public ArrayList<HPotionEffect> getPotionEffects() {
		return potionEffects;
	}

	public HPotionData getPotionData() {
		return potionData;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HPotionMeta)) {
			return false;
		}
		HPotionMeta hPotionMeta = (HPotionMeta) o;
		return super.equals(hPotionMeta) && Objects.equals(potionEffects, hPotionMeta.potionEffects)
				&& Objects.equals(potionData, hPotionMeta.potionData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), potionEffects, potionData);
	}

}