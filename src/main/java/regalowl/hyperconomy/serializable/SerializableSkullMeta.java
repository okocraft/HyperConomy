package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableSkullMeta extends SerializableItemMeta {
	private static final long serialVersionUID = -1095975801937823837L;

	private OfflinePlayer owner;

	public SerializableSkullMeta(ItemMeta im) {
		super(im);
		if (im instanceof SkullMeta) {
			SkullMeta sm = (SkullMeta) im;
			this.owner = sm.getOwningPlayer();
		}
	}

	public SerializableSkullMeta(String base64String) {
		super(base64String);
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableSkullMeta)) {
				return;
			}
			SerializableSkullMeta ssm = (SerializableSkullMeta) o;
			this.owner = ssm.getOwner();
		} catch (Exception ignored) {
		}
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemStack s = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm = (SkullMeta) s.getItemMeta();
		sm.setDisplayName(displayName);
		sm.setLore(lore);
		for (SerializableEnchantment se : enchantments) {
			sm.addEnchant(se.getEnchantment(), se.getLvl(), true);
		}
		sm.setOwningPlayer(owner);
		return sm;
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableSkullMeta)) {
			return false;
		}
		SerializableSkullMeta serializableSkullMeta = (SerializableSkullMeta) o;
		return super.equals(serializableSkullMeta) && Objects.equals(owner, serializableSkullMeta.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), owner);
	}
}