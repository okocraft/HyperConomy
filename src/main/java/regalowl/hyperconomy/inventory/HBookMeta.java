package regalowl.hyperconomy.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.simpledatalib.CommonFunctions;

public class HBookMeta extends HItemMeta {

	private String author;
	private ArrayList<String> pages;
	private String title;

	public HBookMeta(String displayName, ArrayList<String> lore, ArrayList<HEnchantment> enchantments,
			ArrayList<HItemFlag> itemFlags, boolean unbreakable, int repairCost, String author, ArrayList<String> pages,
			String title) {
		super(displayName, lore, enchantments, itemFlags, unbreakable, repairCost);
		this.author = author;
		this.pages = pages;
		this.title = title;
	}

	public HBookMeta(String serialized) {
		super(serialized);
		HashMap<String, String> data = CommonFunctions.explodeMap(serialized);
		this.author = data.get("author");
		this.pages = CommonFunctions.explode(data.get("pages"));
		this.title = data.get("title");
	}

	public HBookMeta(HBookMeta meta) {
		super(meta);
		this.author = meta.author;
		this.pages = new ArrayList<String>(meta.pages);
		this.title = meta.title;
	}

	@Override
	public String serialize() {
		HashMap<String, String> data = super.getMap();
		data.put("author", author);
		data.put("pages", CommonFunctions.implode(pages));
		data.put("title", title);
		return CommonFunctions.implodeMap(data);
	}

	@Override
	public ArrayList<String> displayInfo(HyperPlayer p, String color1, String color2) {
		ArrayList<String> info = super.displayInfo(p, color1, color2);
		info.add(color1 + "Author: " + color2 + author);
		info.add(color1 + "Title: " + color2 + title);
		info.add(color1 + "Page Count: " + color2 + pages.size());
		return info;
	}

	@Override
	public HItemMetaType getType() {
		return HItemMetaType.BOOK;
	}

	public ArrayList<String> getPages() {
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
		if (!(o instanceof HBookMeta)) {
			return false;
		}
		HBookMeta hBookMeta = (HBookMeta) o;
		return super.equals(hBookMeta) && Objects.equals(author, hBookMeta.author) && Objects.equals(pages, hBookMeta.pages) && Objects.equals(title, hBookMeta.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), author, pages, title);
	}
}