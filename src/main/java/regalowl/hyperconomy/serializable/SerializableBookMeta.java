package regalowl.hyperconomy.serializable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializableBookMeta extends SerializableItemMeta {
	private static final long serialVersionUID = -1095975801937823837L;

	private String author;
	private List<String> pages;
	private String title;

	public SerializableBookMeta(ItemMeta im) {
		super(im);
		if (im instanceof BookMeta) {
			BookMeta bm = (BookMeta) im;
			this.author = bm.getAuthor();
			this.pages = bm.getPages();
			this.title = bm.getTitle();
		}
	}

	public SerializableBookMeta(String base64String) {
		super(base64String);
		try {
			byte[] data = Base64Coder.decode(base64String);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			if (!(o instanceof SerializableBookMeta)) {
				return;
			}
			SerializableBookMeta bm = (SerializableBookMeta) o;
			this.author = bm.getAuthor();
			this.pages = bm.getPages();
			this.title = bm.getTitle();
		} catch (Exception e) {

		}
	}

	@Override
	public ItemMeta getItemMeta() {
		ItemStack s = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta) s.getItemMeta();
		bm.setDisplayName(displayName);
		bm.setLore(lore);
		for (SerializableEnchantment se : enchantments) {
			bm.addEnchant(se.getEnchantment(), se.getLvl(), true);
		}
		bm.setPages(pages);
		bm.setAuthor(author);
		bm.setTitle(title);
		return bm;
	}

	public List<String> getPages() {
		return pages;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SerializableBookMeta)) {
			return false;
		}
		SerializableBookMeta serializableBookMeta = (SerializableBookMeta) o;
		return super.equals(serializableBookMeta) && Objects.equals(author, serializableBookMeta.author) && Objects.equals(pages, serializableBookMeta.pages) && Objects.equals(title, serializableBookMeta.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), author, pages, title);
	}

}