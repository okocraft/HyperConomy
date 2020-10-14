package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import org.bukkit.Color;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableColor extends SerializableObject {
	private static final long serialVersionUID = 1194773802989404854L;

	private int red;
	private int green;
	private int blue;

	public SerializableColor(Color c) {
		this.red = c.getRed();
		this.green = c.getGreen();
		this.blue = c.getBlue();
	}

	public SerializableColor(String base64String) {
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableColor)) {
				return;
			}
			SerializableColor sc = (SerializableColor) o;
			this.red = sc.getRed();
			this.green = sc.getGreen();
			this.blue = sc.getBlue();
		} catch (Exception e) {

		}
	}

	public Color getColor() {
		return Color.fromRGB(red, green, blue);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return red;
	}

	public int getBlue() {
		return red;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableColor)) {
			return false;
		}
		SerializableColor serializableColor = (SerializableColor) o;
		return red == serializableColor.red && green == serializableColor.green && blue == serializableColor.blue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(red, green, blue);
	}

}
