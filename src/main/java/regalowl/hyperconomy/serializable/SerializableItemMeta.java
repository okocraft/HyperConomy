package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableItemMeta extends SerializableObject {
	private static final long serialVersionUID = 4510326523024526205L;

	protected String displayName;
	protected List<String> lore;
	protected List<SerializableEnchantment> enchantments = new ArrayList<SerializableEnchantment>();

	public SerializableItemMeta(ItemMeta im) {
		this.displayName = im.getDisplayName();
		this.lore = im.getLore();
		Map<Enchantment, Integer> enchants = im.getEnchants();
		Iterator<Enchantment> it = enchants.keySet().iterator();
		while (it.hasNext()) {
			Enchantment e = it.next();
			int lvl = enchants.get(e);
			this.enchantments.add(new SerializableEnchantment(e, lvl));
		}
	}

	public SerializableItemMeta(String base64String) {
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableItemMeta)) {
				return;
			}
			SerializableItemMeta se = (SerializableItemMeta) o;
			this.displayName = se.getDisplayName();
			this.lore = se.getLore();
			this.enchantments = se.getEnchantments();
		} catch (Exception e) {

		}
	}

	public void displayInfo(Player p, ChatColor color1, ChatColor color2) {
		p.sendMessage(color1 + "Display Name: " + color2 + displayName);
		String loreString = "";
		if (lore != null && lore.size() > 0) {
			for (String l : lore) {
				loreString += l + ",";
			}
			loreString = loreString.substring(0, loreString.length() - 1);
		}
		p.sendMessage(color1 + "Lore: " + color2 + loreString);
		String enchantString = "";
		if (enchantments != null && enchantments.size() > 0) {
			for (SerializableEnchantment se : enchantments) {
				enchantString += se.getEnchantmentName() + ",";
			}
			enchantString = enchantString.substring(0, enchantString.length() - 1);
		}
		p.sendMessage(color1 + "Enchantments: " + color2 + enchantString);
	}

	public ItemMeta getItemMeta() {
		ItemStack s = new ItemStack(Material.STONE);
		ItemMeta im = s.getItemMeta();
		im.setDisplayName(displayName);
		im.setLore(lore);
		for (SerializableEnchantment se : enchantments) {
			im.addEnchant(se.getEnchantment(), se.getLvl(), true);
		}
		return im;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<SerializableEnchantment> getEnchantments() {
		return enchantments;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableItemMeta)) {
			return false;
		}
		SerializableItemMeta serializableItemMeta = (SerializableItemMeta) o;
		return Objects.equals(displayName, serializableItemMeta.displayName)
				&& Objects.equals(lore, serializableItemMeta.lore)
				&& Objects.equals(enchantments, serializableItemMeta.enchantments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(displayName, lore, enchantments);
	}

}