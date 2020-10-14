package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import regalowl.simpledatalib.CommonFunctions;

public class HFireworkEffect {

	private List<HColor> colors = new ArrayList<HColor>();
	private List<HColor> fadeColors = new ArrayList<HColor>();
	private String type;
	private boolean hasFlicker;
	private boolean hasTrail;

	public HFireworkEffect(ArrayList<HColor> colors, ArrayList<HColor> fadeColors, String type, boolean hasFlicker,
			boolean hasTrail) {
		this.colors = colors;
		this.fadeColors = fadeColors;
		this.type = type;
		this.hasFlicker = hasFlicker;
		this.hasTrail = hasTrail;
	}

	public HFireworkEffect(String serialized) {
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		ArrayList<String> c = CommonFunctions.explode(data.get("colors"));
		for (String cString : c) {
			colors.add(new HColor(cString));
		}
		ArrayList<String> fc = CommonFunctions.explode(data.get("fadeColors"));
		for (String cString : fc) {
			fadeColors.add(new HColor(cString));
		}
		type = data.get("type");
		hasFlicker = Boolean.parseBoolean(data.get("hasFlicker"));
		hasTrail = Boolean.parseBoolean(data.get("hasTrail"));
	}

	public HFireworkEffect(HFireworkEffect fe) {
		for (HColor c : fe.colors) {
			this.colors.add(new HColor(c));
		}
		for (HColor c : fe.colors) {
			this.fadeColors.add(new HColor(c));
		}
		type = fe.type;
		hasFlicker = fe.hasFlicker;
		hasTrail = fe.hasTrail;
	}

	public String serialize() {
		ArrayList<String> c = new ArrayList<String>();
		for (HColor hc : colors) {
			c.add(hc.serialize());
		}
		ArrayList<String> fc = new ArrayList<String>();
		for (HColor hc : fadeColors) {
			fc.add(hc.serialize());
		}
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("colors", CommonFunctions.implode(c));
		data.put("fadeColors", CommonFunctions.implode(fc));
		data.put("type", type);
		data.put("hasFlicker", hasFlicker + "");
		data.put("hasTrail", hasTrail + "");
		return CommonFunctions.implodeMap(data);
	}

	public List<HColor> getColors() {
		return colors;
	}

	public List<HColor> getFadeColors() {
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
		if (!(o instanceof HFireworkEffect)) {
			return false;
		}
		HFireworkEffect hFireworkEffect = (HFireworkEffect) o;
		return Objects.equals(colors, hFireworkEffect.colors) && Objects.equals(fadeColors, hFireworkEffect.fadeColors)
				&& Objects.equals(type, hFireworkEffect.type) && hasFlicker == hFireworkEffect.hasFlicker
				&& hasTrail == hFireworkEffect.hasTrail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(colors, fadeColors, type, hasFlicker, hasTrail);
	}
}
