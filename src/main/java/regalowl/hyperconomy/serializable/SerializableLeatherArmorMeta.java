package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableLeatherArmorMeta extends SerializableItemMeta {
	private static final long serialVersionUID = -7716626610545205516L;

	private SerializableColor color;

	public SerializableLeatherArmorMeta(ItemMeta im) {
		super(im);
		if (im instanceof LeatherArmorMeta) {
			LeatherArmorMeta lam = (LeatherArmorMeta) im;
			this.color = new SerializableColor(lam.getColor());
		}
	}

	public SerializableLeatherArmorMeta(String base64String) {
		super(base64String);
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableLeatherArmorMeta)) {
				return;
			}
			SerializableLeatherArmorMeta slam = (SerializableLeatherArmorMeta) o;
			this.color = slam.getColor();
		} catch (Exception e) {

		}
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemStack s = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta lam = (LeatherArmorMeta) s.getItemMeta();
		lam.setDisplayName(displayName);
		lam.setLore(lore);
		for (SerializableEnchantment se : enchantments) {
			lam.addEnchant(se.getEnchantment(), se.getLvl(), true);
		}
		lam.setColor(color.getColor());
		return lam;
	}

	public SerializableColor getColor() {
		return color;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableLeatherArmorMeta)) {
			return false;
		}
		SerializableLeatherArmorMeta serializableLeatherArmorMeta = (SerializableLeatherArmorMeta) o;
		return super.equals(serializableLeatherArmorMeta) && Objects.equals(color, serializableLeatherArmorMeta.color);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), color);
	}
}