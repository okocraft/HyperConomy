package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableFireworkEffect extends SerializableObject {
	private static final long serialVersionUID = 2644823685312321272L;

	private List<SerializableColor> colors = new ArrayList<SerializableColor>();
	private List<SerializableColor> fadeColors = new ArrayList<SerializableColor>();
	private String type;
	private boolean hasFlicker;
	private boolean hasTrail;

	public SerializableFireworkEffect(FireworkEffect fe) {
		for (Color c : fe.getColors()) {
			colors.add(new SerializableColor(c));
		}
		for (Color c : fe.getFadeColors()) {
			fadeColors.add(new SerializableColor(c));
		}
		this.type = fe.getType().toString();
		this.hasFlicker = fe.hasFlicker();
		this.hasTrail = fe.hasTrail();
	}

	public SerializableFireworkEffect(String base64String) {
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableFireworkEffect)) {
				return;
			}
			SerializableFireworkEffect sfe = (SerializableFireworkEffect) o;
			this.colors = sfe.getColors();
			this.fadeColors = sfe.getFadeColors();
			this.type = sfe.getType();
			this.hasFlicker = sfe.hasFlicker();
			this.hasTrail = sfe.hasTrail();
		} catch (Exception e) {

		}
	}

	public FireworkEffect getFireworkEffect() {
		Builder fb = FireworkEffect.builder();
		for (SerializableColor c : colors) {
			fb.withColor(c.getColor());
		}
		for (SerializableColor c : fadeColors) {
			fb.withFade(c.getColor());
		}
		fb.with(FireworkEffect.Type.valueOf(type));
		fb.flicker(hasFlicker);
		fb.trail(hasTrail);
		return fb.build();
	}

	public List<SerializableColor> getColors() {
		return colors;
	}

	public List<SerializableColor> getFadeColors() {
		return fadeColors;
	}

	public String getType() {
		return type;
	}

	public boolean hasFlicker() {
		return hasFlicker;
	}

	public boolean hasTrail() {
		return hasTrail;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableFireworkEffect)) {
			return false;
		}
		SerializableFireworkEffect serializableFireworkEffect = (SerializableFireworkEffect) o;
		return Objects.equals(colors, serializableFireworkEffect.colors) && Objects.equals(fadeColors, serializableFireworkEffect.fadeColors) && Objects.equals(type, serializableFireworkEffect.type) && hasFlicker == serializableFireworkEffect.hasFlicker && hasTrail == serializableFireworkEffect.hasTrail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(colors, fadeColors, type, hasFlicker, hasTrail);
	}

}
