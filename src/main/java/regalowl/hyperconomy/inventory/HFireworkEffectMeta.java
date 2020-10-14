package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HFireworkEffectMeta extends HItemMeta {

	private HFireworkEffect effect;

	public HFireworkEffectMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, HFireworkEffect effect) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.effect = effect;
	}

	public HFireworkEffectMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		String serializedFireworkEffect = data.get("effect");
		if (serializedFireworkEffect != null)
			effect = new HFireworkEffect(serializedFireworkEffect);
	}

	public HFireworkEffectMeta(HFireworkEffectMeta meta) {
		super(meta);
		this.effect = new HFireworkEffect(meta.effect);
	}

	@Override
	public String serialize() {
		HashMap<String, String> data = super.getMap();
		if (effect != null)
			data.put("effect", effect.serialize());
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.FIREWORK_EFFECT;
	}

	public HFireworkEffect getEffect() {
		return effect;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HFireworkEffectMeta)) {
			return false;
		}
		HFireworkEffectMeta hFireworkEffectMeta = (HFireworkEffectMeta) o;
		return super.equals(hFireworkEffectMeta) && Objects.equals(effect, hFireworkEffectMeta.effect);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), effect);
	}
}