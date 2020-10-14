package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableEnchantmentStorageMeta extends SerializableItemMeta {
	private static final long serialVersionUID = -6050487153906386305L;

	private List<SerializableEnchantment> storedEnchantments = new ArrayList<SerializableEnchantment>();

	public SerializableEnchantmentStorageMeta(ItemMeta im) {
		super(im);
		if (im instanceof EnchantmentStorageMeta) {
			EnchantmentStorageMeta esm = (EnchantmentStorageMeta) im;
			Map<Enchantment, Integer> enchants = esm.getStoredEnchants();
			Iterator<Enchantment> it = enchants.keySet().iterator();
			while (it.hasNext()) {
				Enchantment e = it.next();
				int lvl = enchants.get(e);
				this.storedEnchantments.add(new SerializableEnchantment(e, lvl));
			}
		}
	}

	public SerializableEnchantmentStorageMeta(String base64String) {
		super(base64String);
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableEnchantmentStorageMeta)) {
				return;
			}
			SerializableEnchantmentStorageMeta se = (SerializableEnchantmentStorageMeta) o;
			this.storedEnchantments = se.getStoredEnchantments();
		} catch (Exception e) {

		}
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemStack s = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta esm = (EnchantmentStorageMeta) s.getItemMeta();
		esm.setDisplayName(displayName);
		esm.setLore(lore);
		for (SerializableEnchantment se : enchantments) {
			esm.addEnchant(se.getEnchantment(), se.getLvl(), true);
		}
		for (SerializableEnchantment se : storedEnchantments) {
			esm.addStoredEnchant(Enchantment.getByName(se.getEnchantmentName()), se.getLvl(), true);
		}
		return esm;
	}

	public List<SerializableEnchantment> getStoredEnchantments() {
		return storedEnchantments;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableEnchantmentStorageMeta)) {
			return false;
		}
		SerializableEnchantmentStorageMeta serializableEnchantmentStorageMeta = (SerializableEnchantmentStorageMeta) o;
		return super.equals(serializableEnchantmentStorageMeta) &&
				Objects.equals(storedEnchantments, serializableEnchantmentStorageMeta.storedEnchantments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), storedEnchantments);
	}
}