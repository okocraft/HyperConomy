package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializableSkullMeta other = (SerializableSkullMeta) obj;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

}