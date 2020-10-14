package regalowl.hyperconomy.inventory;

import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HPotionData {
	private String potionType;
	private boolean isExtended;
	private boolean isUpgraded;

	public HPotionData(String potionType, boolean isExtended, boolean isUpgraded) {
		this.potionType = potionType;
		this.isExtended = isExtended;
		this.isUpgraded = isUpgraded;
	}

	public String serialize() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("potionType", potionType);
		data.put("isExtended", isExtended + "");
		data.put("isUpgraded", isUpgraded + "");
		return CommonFunctions.implodeMap(data);
	}

	public HPotionData(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.potionType = data.get("potionType");
		this.isExtended = Boolean.parseBoolean(data.get("isExtended"));
		this.isUpgraded = Boolean.parseBoolean(data.get("isUpgraded"));
	}

	public HPotionData(HPotionData hpd) {
		this.potionType = hpd.potionType;
		this.isExtended = hpd.isExtended;
		this.isUpgraded = hpd.isUpgraded;
	}

	public String getPotionType() {
		return potionType;
	}

	public boolean isExtended() {
		return isExtended;
	}

	public boolean isUpgraded() {
		return isUpgraded;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HPotionData)) {
			return false;
		}
		HPotionData hPotionData = (HPotionData) o;
		return Objects.equals(potionType, hPotionData.potionType) && isExtended == hPotionData.isExtended
				&& isUpgraded == hPotionData.isUpgraded;
	}

	@Override
	public int hashCode() {
		return Objects.hash(potionType, isExtended, isUpgraded);
	}

}