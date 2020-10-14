package regalowl.hyperconomy.tradeobject;

import java.io.Serializable;
import java.util.Objects;

import regalowl.hyperconomy.inventory.HItemMeta;

public class StackComparisonData implements Serializable {
	private static final long serialVersionUID = -755014510365039145L;

	public String material;
	public short durability;
	public byte data;
	public HItemMeta itemMeta;
	public int maxStackSize;
	public int maxDurability;
	public boolean isBlank;

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof StackComparisonData)) {
			return false;
		}
		StackComparisonData stackComparisonData = (StackComparisonData) o;
		return Objects.equals(material, stackComparisonData.material) && durability == stackComparisonData.durability
				&& data == stackComparisonData.data && Objects.equals(itemMeta, stackComparisonData.itemMeta)
				&& maxStackSize == stackComparisonData.maxStackSize
				&& maxDurability == stackComparisonData.maxDurability && isBlank == stackComparisonData.isBlank;
	}

	@Override
	public int hashCode() {
		return Objects.hash(material, durability, data, itemMeta, maxStackSize, maxDurability, isBlank);
	}
}
