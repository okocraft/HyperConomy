package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializablePotionEffect extends SerializableObject {
	private static final long serialVersionUID = 1194773802989404854L;

	private String potionEffectType;
	private int amplifier;
	private int duration;
	private boolean isAmbient;

	public SerializablePotionEffect(PotionEffect pe) {
		this.potionEffectType = pe.getType().toString();
		this.amplifier = pe.getAmplifier();
		this.duration = pe.getDuration();
		this.isAmbient = pe.isAmbient();
	}

	public SerializablePotionEffect(String base64String) {
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializablePotionEffect)) {
				return;
			}
			SerializablePotionEffect spe = (SerializablePotionEffect) o;
			this.potionEffectType = spe.getType();
			this.amplifier = spe.getAmplifier();
			this.duration = spe.getDuration();
			this.isAmbient = spe.isAmbient();
		} catch (Exception e) {

		}
	}

	public PotionEffect getPotionEffect() {
		return new PotionEffect(PotionEffectType.getByName(potionEffectType), duration, amplifier, isAmbient);
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
		if (!(o instanceof SerializablePotionEffect)) {
			return false;
		}
		SerializablePotionEffect serializablePotionEffect = (SerializablePotionEffect) o;
		return Objects.equals(potionEffectType, serializablePotionEffect.potionEffectType)
				&& amplifier == serializablePotionEffect.amplifier && duration == serializablePotionEffect.duration
				&& isAmbient == serializablePotionEffect.isAmbient;
	}

	@Override
	public int hashCode() {
		return Objects.hash(potionEffectType, amplifier, duration, isAmbient);
	}

}
