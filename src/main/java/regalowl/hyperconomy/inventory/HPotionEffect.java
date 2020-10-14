package regalowl.hyperconomy.inventory;

import java.util.HashMap;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HPotionEffect {

	private String potionEffectType;
	private int amplifier;
	private int duration;
	private boolean isAmbient;

	public HPotionEffect(String potionEffectType, int amplifier, int duration, boolean isAmbient) {
		this.potionEffectType = potionEffectType;
		this.amplifier = amplifier;
		this.duration = duration;
		this.isAmbient = isAmbient;
	}

	public HPotionEffect(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.potionEffectType = data.get("potionEffectType");
		this.amplifier = Integer.parseInt(data.get("amplifier"));
		this.duration = Integer.parseInt(data.get("duration"));
		this.isAmbient = Boolean.parseBoolean(data.get("isAmbient"));
	}

	public HPotionEffect(HPotionEffect pe) {
		this.potionEffectType = pe.potionEffectType;
		this.amplifier = pe.amplifier;
		this.duration = pe.duration;
		this.isAmbient = pe.isAmbient;
	}

	public String serialize() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("potionEffectType", potionEffectType);
		data.put("amplifier", amplifier + "");
		data.put("duration", duration + "");
		data.put("isAmbient", isAmbient + "");
		return CommonFunctions.implodeMap(data);
	}

	public String getType() {
		return potionEffectType;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public int getDuration() {
		return duration;
	}

	public boolean isAmbient() {
		return isAmbient;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HPotionEffect)) {
			return false;
		}
		HPotionEffect hPotionEffect = (HPotionEffect) o;
		return Objects.equals(potionEffectType, hPotionEffect.potionEffectType) && amplifier == hPotionEffect.amplifier
				&& duration == hPotionEffect.duration && isAmbient == hPotionEffect.isAmbient;
	}

	@Override
	public int hashCode() {
		return Objects.hash(potionEffectType, amplifier, duration, isAmbient);
	}

}