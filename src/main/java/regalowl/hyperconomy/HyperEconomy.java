package regalowl.hyperconomy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import regalowl.simpledatalib.CommonFunctions;
import regalowl.simpledatalib.sql.QueryResult;
import regalowl.simpledatalib.sql.SQLRead;
import regalowl.hyperconomy.account.HyperAccount;
import regalowl.hyperconomy.account.HyperBank;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.inventory.HEnchantment;
import regalowl.hyperconomy.inventory.HItemStack;
import regalowl.hyperconomy.shop.PlayerShop;
import regalowl.hyperconomy.shop.Shop;
import regalowl.hyperconomy.tradeobject.ComponentTradeItem;
import regalowl.hyperconomy.tradeobject.CompositeTradeItem;
import regalowl.hyperconomy.tradeobject.StackComparisonData;
import regalowl.hyperconomy.tradeobject.TradeEnchant;
import regalowl.hyperconomy.tradeobject.TradeObject;
import regalowl.hyperconomy.tradeobject.TradeObjectType;
import regalowl.hyperconomy.tradeobject.TradeXp;
import regalowl.hyperconomy.util.Backup;

public class HyperEconomy implements Serializable {

	private static final long serialVersionUID = 4820082604724045149L;

	private transient HyperConomy hc;
	private transient String lastFailedToLoadComposite = "";
	private transient boolean successfulLoad = true;

	private String accountData;
	private String defaultAccount;
	private boolean defaultAccountIsBank;
	private HashMap<String, TradeObject> tradeObjectsNameMap = new HashMap<String, TradeObject>();
	private HashMap<StackComparisonData, TradeObject> tradeObjectsStackMap = new HashMap<StackComparisonData, TradeObject>();
	private ArrayList<TradeObject> tradeObjects = new ArrayList<TradeObject>();
	private HashMap<String, String> composites = new HashMap<String, String>();
	private boolean useComposites;
	private String economyName;
	private String xpName = null;

	public HyperEconomy(HyperConomy hc, String economy, String accountData) {
		this.hc = hc;
		this.accountData = accountData;
		SQLRead sr = hc.getSQLRead();
		this.economyName = economy;
		if (accountData.contains(":")) {
			String[] accountDataArray = accountData.split(Pattern.quote(":"));
			this.defaultAccount = accountDataArray[1];
			if (accountDataArray[0].equalsIgnoreCase("BANK"))
				defaultAccountIsBank = true;
		} else {
			this.defaultAccount = accountData;
		}
		useComposites = hc.getConf().getBoolean("enable-feature.composite-items");
		successfulLoad = loadData(sr);
	}

	private synchronized boolean loadData(SQLRead sr) {
		composites.clear();
		QueryResult result = sr
				.select("SELECT NAME, COMPONENTS FROM hyperconomy_objects WHERE COMPONENTS != '' AND ECONOMY = '"
						+ economyName + "'");
		while (result.next()) {
			composites.put(result.getString("NAME").toLowerCase(), result.getString("COMPONENTS"));
		}
		tradeObjectsNameMap.clear();
		tradeObjectsStackMap.clear();
		result = sr.select(
				"SELECT * FROM hyperconomy_objects ho LEFT JOIN hyperconomy_object_data hod ON ho.DATA_ID = hod.ID WHERE ECONOMY = '"
						+ economyName + "'");
		while (result.next()) {
			if (useComposites && composites.containsKey(result.getString("NAME").toLowerCase())) {
				continue;
			}
			TradeObjectType type = TradeObjectType.fromString(result.getString("TYPE"));
			TradeObject to = null;
			if (type == TradeObjectType.ITEM) {
				to = new ComponentTradeItem(hc, this, result.getString("NAME"), result.getString("ECONOMY"),
						result.getString("DISPLAY_NAME"), result.getString("ALIASES"), result.getString("CATEGORIES"),
						result.getString("TYPE"), result.getDouble("VALUE"), result.getString("STATIC"),
						result.getDouble("STATICPRICE"), result.getDouble("STOCK"), result.getDouble("MEDIAN"),
						result.getString("INITIATION"), result.getDouble("STARTPRICE"), result.getDouble("CEILING"),
						result.getDouble("FLOOR"), result.getDouble("MAXSTOCK"), result.getString("COMPONENTS"),
						result.getInt("DATA_ID"), result.getString("DATA"), result.getDouble("VERSION"));
			} else if (type == TradeObjectType.ENCHANTMENT) {
				to = new TradeEnchant(hc, this, result.getString("NAME"), result.getString("ECONOMY"),
						result.getString("DISPLAY_NAME"), result.getString("ALIASES"), result.getString("CATEGORIES"),
						result.getString("TYPE"), result.getDouble("VALUE"), result.getString("STATIC"),
						result.getDouble("STATICPRICE"), result.getDouble("STOCK"), result.getDouble("MEDIAN"),
						result.getString("INITIATION"), result.getDouble("STARTPRICE"), result.getDouble("CEILING"),
						result.getDouble("FLOOR"), result.getDouble("MAXSTOCK"), result.getString("COMPONENTS"),
						result.getInt("DATA_ID"), result.getString("DATA"), result.getDouble("VERSION"));
			} else if (type == TradeObjectType.EXPERIENCE) {
				to = new TradeXp(hc, this, result.getString("NAME"), result.getString("ECONOMY"),
						result.getString("DISPLAY_NAME"), result.getString("ALIASES"), result.getString("CATEGORIES"),
						result.getString("TYPE"), result.getDouble("VALUE"), result.getString("STATIC"),
						result.getDouble("STATICPRICE"), result.getDouble("STOCK"), result.getDouble("MEDIAN"),
						result.getString("INITIATION"), result.getDouble("STARTPRICE"), result.getDouble("CEILING"),
						result.getDouble("FLOOR"), result.getDouble("MAXSTOCK"), result.getString("COMPONENTS"),
						result.getInt("DATA_ID"), result.getString("DATA"), result.getDouble("VERSION"));
				xpName = result.getString("NAME");
			}
			if (to != null)
				addObject(to);
		}
		result.close();
		if (xpName == null)
			xpName = "xp";
		if (useComposites) {
			return loadComposites(sr);
		}
		return true;
	}

	public synchronized void addObject(TradeObject to) {
		if (to == null)
			return;
		tradeObjectsNameMap.put(to.getName().toLowerCase(), to);
		tradeObjectsNameMap.put(to.getDisplayName().toLowerCase(), to);
		for (String alias : to.getAliases()) {
			tradeObjectsNameMap.put(alias.toLowerCase(), to);
		}
		if (to.getType() == TradeObjectType.ITEM)
			tradeObjectsStackMap.put(to.getItem().getStackComparisonData(), to);
		tradeObjects.add(to);
	}

	public synchronized void removeObject(TradeObject to) {
		if (to == null)
			return;
		tradeObjectsNameMap.remove(to.getName().toLowerCase());
		tradeObjectsNameMap.remove(to.getDisplayName().toLowerCase());
		for (String alias : to.getAliases()) {
			tradeObjectsNameMap.remove(alias.toLowerCase());
		}
		if (to.getType() == TradeObjectType.ITEM)
			tradeObjectsStackMap.remove(to.getItem().getStackComparisonData());
		tradeObjects.remove(to);
	}

	public synchronized void removeObject(String name) {
		TradeObject to = getTradeObject(name);
		if (to == null)
			return;
		removeObject(to);
	}

	private synchronized boolean loadComposites(SQLRead sr) {
		boolean loaded = false;
		int counter = 0;
		QueryResult result = sr.select(
				"SELECT * FROM hyperconomy_objects ho LEFT JOIN hyperconomy_object_data hod ON ho.DATA_ID = hod.ID WHERE ECONOMY = '"
						+ economyName + "' AND COMPONENTS != ''");
		while (!loaded) {
			counter++;
			if (counter > 100) {
				if (hc != null) {
					hc.getMC().logSevere("[HyperConomy]Composite item dependency missing: [" + lastFailedToLoadComposite
							+ "]  Please use /hcdelete or the GUI to delete objects; do not directly edit the database.");
					hc.getMC().logSevere("[HyperConomy]Attempting to repair the database...");
					repairComposites(sr);
				}
				return false;
			}
			loaded = true;
			result.reset();
			while (result.next()) {
				String name = result.getString("NAME");
				if (getTradeObject(name) != null)
					continue;
				if (!componentsLoaded(name)) {
					loaded = false;
					continue;
				}
				TradeObject to = new CompositeTradeItem(hc, this, name, economyName, result.getString("DISPLAY_NAME"),
						result.getString("ALIASES"), result.getString("CATEGORIES"), result.getString("TYPE"),
						result.getString("COMPONENTS"), result.getInt("DATA_ID"), result.getString("DATA"),
						result.getDouble("VERSION"));
				addObject(to);
			}
		}
		return true;
	}

	private synchronized void repairComposites(SQLRead sr) {
		if (hc.getConf().getBoolean("enable-feature.automatic-backups")) {
			new Backup(hc);
		}
		QueryResult result = sr.select(
				"SELECT NAME, DISPLAY_NAME, ALIASES, COMPONENTS FROM hyperconomy_objects WHERE COMPONENTS != '' AND ECONOMY = '"
						+ economyName + "'");
		while (result.next()) {// iterate through all composite objects
			String name = result.getString("NAME");// name of current composite
			String components = result.getString("COMPONENTS");
			HashMap<String, String> tempComponents = CommonFunctions.explodeMap(components);// get current composite's
																							// components
			for (Map.Entry<String, String> entry : tempComponents.entrySet()) {// iterate through current composite's
																				// components
				String oname = entry.getKey();// name of current component
				QueryResult result2 = sr
						.select("SELECT NAME, DISPLAY_NAME, ALIASES FROM hyperconomy_objects WHERE ECONOMY = '"
								+ economyName + "'");
				boolean removeCompositeNature = true;
				while (result2.next()) {// iterate through all objects in economy
					if (result2.getString("NAME").equals(oname) || result2.getString("DISPLAY_NAME").equals(oname)) {
						removeCompositeNature = false; // if name matches the current component continue since the
														// component exists
					} else {
						ArrayList<String> aliases = CommonFunctions.explode(result2.getString("ALIASES"));
						for (String alias : aliases) {
							if (alias.equals(oname)) {
								removeCompositeNature = false; // if an alias matches the current component continue
																// since the component exists
							}
						}
					}
				}
				if (removeCompositeNature) {// if composite's component doesn't exist convert the composite to component
					hc.getMC().logSevere(
							"[HyperConomy]Composite removed: " + name + " (" + result.getString("DISPLAY_NAME") + ")");
					String statement = "UPDATE hyperconomy_objects SET COMPONENTS = '' WHERE NAME = '" + name
							+ "' AND ECONOMY = '" + economyName + "'";
					hc.getSQLWrite().addToQueue(statement);
				}
			}
		}
		hc.restart();
	}

	private synchronized boolean componentsLoaded(String name) {
		HashMap<String, String> tempComponents = CommonFunctions.explodeMap(composites.get(name.toLowerCase()));
		for (Map.Entry<String, String> entry : tempComponents.entrySet()) {
			String oname = entry.getKey();
			TradeObject ho = getTradeObject(oname);
			if (ho == null) {
				lastFailedToLoadComposite = oname;
				// System.out.println("Not loaded: " + oname);
				return false;
			}
		}
		return true;
	}

	public synchronized void delete() {
		for (Shop shop : hc.getDataManager().getHyperShopManager().getShops()) {
			if (shop.getEconomy().equalsIgnoreCase(economyName)) {
				shop.setEconomy("default");
			}
		}
		for (HyperPlayer hp : hc.getDataManager().getHyperPlayerManager().getHyperPlayers()) {
			if (hp.getEconomy().equalsIgnoreCase(economyName)) {
				hp.setEconomy("default");
			}
		}
		hc.getDataManager().deleteEconomy(economyName);
	}

	public synchronized HyperAccount getDefaultAccount() {
		if (defaultAccountIsBank) {
			return hc.getHyperBankManager().getHyperBank(defaultAccount);
		} else {
			return hc.getHyperPlayerManager().getHyperPlayer(defaultAccount);
		}
	}

	public synchronized void setDefaultAccount(HyperAccount account) {
		if (account == null) {
			return;
		}
		HashMap<String, String> conditions = new HashMap<String, String>();
		HashMap<String, String> values = new HashMap<String, String>();
		conditions.put("NAME", economyName);
		defaultAccountIsBank = false;
		if (account instanceof HyperBank)
			defaultAccountIsBank = true;
		if (defaultAccountIsBank) {
			values.put("HYPERACCOUNT", "BANK:" + account.getName());
		} else {
			values.put("HYPERACCOUNT", "PLAYER:" + account.getName());
		}
		hc.getSQLWrite().performUpdate("hyperconomy_economies", values, conditions);
		this.defaultAccount = account.getName();
	}

	public synchronized String getName() {
		return economyName;
	}

	public synchronized TradeObject getTradeObject(HEnchantment enchant) {
		return getTradeObject(enchant, null);
	}

	public synchronized TradeObject getTradeObject(HEnchantment enchant, Shop s) {
		if (enchant == null) {
			return null;
		}
		if (s != null && s instanceof PlayerShop) {
			for (TradeObject ho : tradeObjects) {
				if (ho.getType() != TradeObjectType.ENCHANTMENT) {
					continue;
				}
				if (!ho.matchesEnchantment(enchant)) {
					continue;
				}
				return (TradeObject) ((PlayerShop) s).getPlayerShopObject(ho);
			}
		} else {
			for (TradeObject ho : tradeObjects) {
				if (ho.getType() != TradeObjectType.ENCHANTMENT) {
					continue;
				}
				if (ho.matchesEnchantment(enchant)) {
					return ho;
				}
			}
		}
		return null;
	}

	public synchronized TradeObject getTradeObject(HItemStack stack) {
		return getTradeObject(stack, null);
	}

	public synchronized TradeObject getTradeObject(HItemStack stack, Shop s) {
		if (stack == null) {
			return null;
		}
		TradeObject ho = tradeObjectsStackMap.get(stack.getStackComparisonData());
		if (ho == null)
			return null;
		if (s != null && s instanceof PlayerShop) {
			return (TradeObject) ((PlayerShop) s).getPlayerShopObject(ho);
		}
		return ho;
	}

	public synchronized TradeObject getTradeObject(String name, Shop s) {
		if (name == null) {
			return null;
		}
		String sname = name.toLowerCase();
		if (s != null && s instanceof PlayerShop) {
			if (tradeObjectsNameMap.containsKey(sname)) {
				return (TradeObject) ((PlayerShop) s).getPlayerShopObject(tradeObjectsNameMap.get(sname));
			} else {
				hc.getDebugMode().debugWriteMessage(
						"getTradeObject() returning null for given name: [" + name + "], shop: [" + s.getName() + "]");
				return null;
			}
		} else {
			if (tradeObjectsNameMap.containsKey(sname)) {
				return tradeObjectsNameMap.get(sname);
			} else {
				hc.getDebugMode().debugWriteMessage("getTradeObject() returning null for given name: [" + name + "]");
				return null;
			}
		}
	}

	public synchronized TradeObject getTradeObject(String name) {
		return getTradeObject(name, null);
	}

	public synchronized ArrayList<TradeObject> getCategory(String category) {
		ArrayList<TradeObject> objs = new ArrayList<TradeObject>();
		for (TradeObject t : tradeObjects) {
			if (t.inCategory(category))
				objs.add(t);
		}
		return objs;
	}

	public synchronized ArrayList<TradeObject> getCategory(String category, Shop s) {
		ArrayList<TradeObject> tos = new ArrayList<TradeObject>();
		for (TradeObject to : getCategory(category)) {
			tos.add(getTradeObject(to.getName(), s));
		}
		return tos;
	}

	public synchronized ArrayList<TradeObject> getTradeObjects(Shop s) {
		ArrayList<TradeObject> hos = new ArrayList<TradeObject>();
		for (TradeObject ho : tradeObjects) {
			hos.add(getTradeObject(ho.getName(), s));
		}
		return hos;
	}

	public synchronized ArrayList<TradeObject> getTradeObjects() {
		return new ArrayList<TradeObject>(tradeObjects);
	}

	public synchronized void clearData() {
		tradeObjectsNameMap.clear();
		tradeObjects.clear();
	}

	public synchronized ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (TradeObject ho : tradeObjects) {
			names.add(ho.getName());
		}
		return names;
	}

	public synchronized String getEnchantNameWithoutLevel(String bukkitName) {
		for (TradeObject ho : tradeObjectsNameMap.values()) {
			if (ho.getType() == TradeObjectType.ENCHANTMENT) {
				if (ho.getEnchantmentName().equalsIgnoreCase(bukkitName)) {
					String name = ho.getName();
					return name.substring(0, name.length() - 1);
				}
			}
		}
		return null;
	}

	public synchronized boolean objectTest(String name) {
		if (!tradeObjectsNameMap.containsKey(name.toLowerCase()))
			return false;
		return true;
	}

	public synchronized boolean itemTest(String name) {
		if (!objectTest(name))
			return false;
		if (tradeObjectsNameMap.get(name.toLowerCase()).getType() != TradeObjectType.ITEM)
			return false;
		return true;
	}

	public synchronized boolean enchantTest(String name) {
		if (!objectTest(name))
			return false;
		if (tradeObjectsNameMap.get(name.toLowerCase()).getType() != TradeObjectType.ENCHANTMENT)
			return false;
		return true;
	}

	public synchronized String getXpName() {
		return xpName;
	}

	public synchronized boolean useComposites() {
		return useComposites;
	}

	public synchronized boolean successfulLoad() {
		return successfulLoad;
	}

	public synchronized void setHyperConomy(HyperConomy hc) {
		this.hc = hc;
		for (TradeObject to : tradeObjects) {
			to.setHyperConomy(hc);
		}
		this.successfulLoad = true;
	}

	public synchronized String getAccountData() {
		return accountData;
	}

	public synchronized void save() {
		// remove from economies table
		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put("NAME", economyName);
		hc.getSQLWrite().performDelete("hyperconomy_economies", conditions);

		// remove all tradeobjects associated with economy
		conditions = new HashMap<String, String>();
		conditions.put("ECONOMY", economyName);
		hc.getSQLWrite().performDelete("hyperconomy_objects", conditions);

		// add to economies table
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("NAME", economyName);
		values.put("HYPERACCOUNT", accountData);
		hc.getSQLWrite().performInsert("hyperconomy_economies", values);

		// add all tradeobjects
		for (TradeObject to : tradeObjects) {
			to.save();
		}
	}

	@Override
	public synchronized int hashCode() {
		return Objects.hash(composites, defaultAccount, defaultAccountIsBank, economyName, tradeObjects,
				tradeObjectsNameMap, useComposites, xpName);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HyperEconomy)) {
			return false;
		}
		HyperEconomy hyperEconomy = (HyperEconomy) o;
		return Objects.equals(defaultAccount, hyperEconomy.defaultAccount)
				&& defaultAccountIsBank == hyperEconomy.defaultAccountIsBank
				&& Objects.equals(tradeObjectsNameMap, hyperEconomy.tradeObjectsNameMap)
				&& Objects.equals(tradeObjects, hyperEconomy.tradeObjects)
				&& Objects.equals(composites, hyperEconomy.composites) && useComposites == hyperEconomy.useComposites
				&& Objects.equals(economyName, hyperEconomy.economyName) && Objects.equals(xpName, hyperEconomy.xpName);
	}
}
