package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import org.bukkit.enchantments.Enchantment;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableEnchantment extends SerializableObject {
	private static final long serialVersionUID = 4510326523024526205L;

	private String enchantment;
	private int lvl;

	public SerializableEnchantment(Enchantment e, int lvl) {
		this.enchantment = e.getName();
		this.lvl = lvl;
	}

	public SerializableEnchantment(String base64String) {
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableEnchantment)) {
				return;
			}
			SerializableEnchantment se = (SerializableEnchantment) o;
			this.enchantment = se.getEnchantmentName();
			this.lvl = se.getLvl();
		} catch (Exception e) {

		}
	}

	public Enchantment getEnchantment() {
		return Enchantment.getByName(enchantment);
	}

	public String getEnchantmentName() {
		return enchantment;
	}

	public int getLvl() {
		return lvl;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableEnchantment)) {
			return false;
		}
		SerializableEnchantment serializableEnchantment = (SerializableEnchantment) o;
		return Objects.equals(enchantment, serializableEnchantment.enchantment) && lvl == serializableEnchantment.lvl;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enchantment, lvl);
	}

}