package regalowl.hyperconomy.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import regalowl.simpledatalib.file.FileTools;
import regalowl.hyperconomy.HyperConomy;

public class LanguageFile {

	private HyperConomy hc;
	private FileTools ft;
	private HashMap<String, String> language = new HashMap<String, String>();
	private HashMap<String, String> languageBackup = new HashMap<String, String>();
	private ArrayList<String> supportedLanguages = new ArrayList<String>();
	private HashMap<String, String> languageConversions = new HashMap<String, String>();

	public LanguageFile(HyperConomy hc) {
		this.hc = hc;
		languageConversions.put("french", "frFR");
		languageConversions.put("français", "frFR");
		languageConversions.put("le français", "frFR");
		languageConversions.put("english", "enUS");
		languageConversions.put("russian", "ruRU");
		languageConversions.put("ру́сский язы́к", "ruRU");
		languageConversions.put("russkij jazyk", "ruRU");
		languageConversions.put("日本語", "jaJP");

		supportedLanguages.add("enUS");
		supportedLanguages.add("frFR");
		supportedLanguages.add("ruRU");
		supportedLanguages.add("jaJP");
		buildLanguageFile(false);
	}

	public String buildLanguageFile(boolean overwrite) {
		ft = hc.getFileTools();
		String lang = hc.getConf().getString("language");
		if (lang == null)
			lang = "enUS";
		lang = lang.replace(" ", "").replace("\"", "").replace("'", "");
		boolean validLanguage = false;
		for (int i = 0; i < supportedLanguages.size(); i++) {
			if (supportedLanguages.get(i).contains(lang)) {
				lang = supportedLanguages.get(i);
				validLanguage = true;
				break;
			}
		}
		String folderpath = hc.getFolderPath() + File.separator + "Languages";
		ft.makeFolder(folderpath);
		updateBackupLanguageFile();
		String filepath = folderpath + File.separator + lang + ".hl";
		if (!ft.fileExists(filepath) || overwrite) {
			if (!validLanguage)
				lang = "enUS";
			filepath = folderpath + File.separator + lang + ".hl";
			ft.deleteFile(filepath);
			ft.copyFileFromJar("Languages/" + lang + ".hl.zip", filepath + ".zip");
			ft.unZipFile(filepath + ".zip", folderpath);
			ft.deleteFile(filepath + ".zip");
		}
		buildHashMap(filepath);
		return lang;
	}

	public void updateBackupLanguageFile() {
		String languageFolder = hc.getFolderPath() + File.separator + "Languages";
		String tempFolder = languageFolder + File.separator + "lang_temp";
		ft.deleteFile(languageFolder + File.separator + "enUS_b.hl");
		ft.makeFolder(languageFolder);
		ft.makeFolder(tempFolder);
		ft.copyFileFromJar("Languages/enUS.hl.zip", tempFolder + File.separator + "enUS.hl.zip");
		ft.unZipFile(tempFolder + File.separator + "enUS.hl.zip", tempFolder);
		ft.moveFile(tempFolder + File.separator + "enUS.hl", languageFolder + File.separator + "enUS_b.hl");
		ft.deleteDirectory(tempFolder);
		ft.deleteFile(tempFolder);
		languageBackup.clear();
		buildBackupHashMap(languageFolder + File.separator + "enUS_b.hl");
	}

	private void buildHashMap(String filepath) {
		try {
			ArrayList<String> lines = ft.getStringArrayFromFile(filepath);
			for (int i = 0; i < lines.size(); i++) {
				String name = lines.get(i).substring(0, lines.get(i).indexOf(":"));
				String text = lines.get(i).substring(lines.get(i).indexOf(":") + 1, lines.get(i).length());
				if (text.startsWith(" ")) {
					text = text.substring(1, text.length());
				}
				language.put(name, text);
			}
		} catch (Exception e) {
			Logger log = Logger.getLogger("Minecraft");
			log.severe("[HyperConomy]You likely have an error in your language file...using a backup.");
		}
	}

	private void buildBackupHashMap(String filepath) {
		ArrayList<String> lines = ft.getStringArrayFromFile(filepath);
		for (int i = 0; i < lines.size(); i++) {
			String name = lines.get(i).substring(0, lines.get(i).indexOf(":"));
			String text = lines.get(i).substring(lines.get(i).indexOf(":") + 1, lines.get(i).length());
			if (text.startsWith(" ")) {
				text = text.substring(1, text.length());
			}
			languageBackup.put(name, text);
		}
	}

	public String get(String key) {
		String message = "";
		if (language.containsKey(key)) {
			message = language.get(key);
		} else {
			if (languageBackup.containsKey(key)) {
				message = languageBackup.get(key);
			} else {
				message = "[" + key + "] NOT FOUND";
			}
		}
		return message;
	}

	public boolean languageSupported(String language) {
		if (languageConversions.containsKey(language.toLowerCase())) {
			return true;
		}
		if (supportedLanguages.contains(language.toLowerCase())) {
			return true;
		}
		return false;
	}

	public String fixLanguage(String language) {
		language = language.toLowerCase();
		if (languageConversions.containsKey(language)) {
			return languageConversions.get(language);
		}
		for (String lang : supportedLanguages) {
			if (lang.equalsIgnoreCase(language)) {
				return lang;
			}
		}
		return language;
	}

	public String formatMoney(double money) {
		BigDecimal bd = new BigDecimal(money);
		BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_DOWN);
		return fC(rounded.toPlainString());
	}

	public String gC(boolean fullName) {
		String currency = get("CURRENCY");
		if (currency == null) {
			currency = "$";
		}
		if (!fullName && currency.length() > 1) {
			currency = currency.trim();
			currency = currency.substring(0, 1);
		}
		return currency;
	}

	public String fC(String amount) {
		String formatted = gC(true) + amount;
		if (hc.getConf().getBoolean("shop.show-currency-symbol-after-price")) {
			formatted = amount + gC(true);
		}
		return formatted;
	}

	public String fC(double amount) {
		String formatted = gC(true) + amount;
		if (hc.getConf().getBoolean("shop.show-currency-symbol-after-price")) {
			formatted = amount + gC(true);
		}
		return formatted;
	}

	public String fCS(double amount) {
		String formatted = gC(false) + amount;
		if (hc.getConf().getBoolean("shop.show-currency-symbol-after-price")) {
			formatted = amount + gC(false);
		}
		return formatted;
	}

	public String fCS(String amount) {
		String formatted = gC(false) + amount;
		if (hc.getConf().getBoolean("shop.show-currency-symbol-after-price")) {
			formatted = amount + gC(false);
		}
		return formatted;
	}

	public String f(String inputstring, int value, int value2) {
		inputstring = inputstring.replace("%v", value + "");
		inputstring = inputstring.replace("%w", value2 + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, String name, String extra) {
		inputstring = inputstring.replace("%e", extra);
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, String name, String extra, int i) {
		inputstring = inputstring.replace("%e", extra);
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%i", i + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, double amount, double price, String name, String extra) {
		inputstring = inputstring.replace("%a", amount + "");
		inputstring = inputstring.replace("%e", extra + "");
		inputstring = inputstring.replace("%zc", extra);
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%p", price + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, double amount, double price, String name, double tax) {
		inputstring = inputstring.replace("%a", amount + "");
		inputstring = inputstring.replace("%t", tax + "");
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%p", price + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, double amount, double price, String name) {
		inputstring = inputstring.replace("%a", amount + "");
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%p", price + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, String name) {
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, double value) {
		inputstring = inputstring.replace("%v", value + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, int value) {
		inputstring = inputstring.replace("%v", value + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, int amount, String name) {
		inputstring = inputstring.replace("%a", amount + "");
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, double amount, String name) {
		inputstring = inputstring.replace("%a", amount + "");
		inputstring = inputstring.replace("%n", name);
		inputstring = inputstring.replace("%zc", name);
		inputstring = inputstring.replace("%p", amount + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	public String f(String inputstring, double value, boolean status) {
		inputstring = inputstring.replace("%s", status + "");
		inputstring = inputstring.replace("%v", value + "");
		inputstring = inputstring.replace("%c", get("CURRENCY"));
		return inputstring;
	}

	/*
	 * public String f(String inputstring, int amount, double price, String name,
	 * HyperPlayer player) { inputstring = inputstring.replace("%a",amount+"");
	 * inputstring = inputstring.replace("%y",player.getName()); inputstring =
	 * inputstring.replace("%n",name); inputstring =
	 * inputstring.replace("%p",price+""); inputstring =
	 * inputstring.replace("%c",get("CURRENCY")); return inputstring; }
	 */
	/*
	 * public String f(String inputstring, int amount, double price, String name,
	 * String isstatic, String isinitial, HyperPlayer player) { inputstring =
	 * inputstring.replace("%a",amount+""); inputstring =
	 * inputstring.replace("%y",player.getName()); inputstring =
	 * inputstring.replace("%n",name); inputstring =
	 * inputstring.replace("%p",price+""); inputstring =
	 * inputstring.replace("%c",get("CURRENCY")); inputstring =
	 * inputstring.replace("%za",isstatic); inputstring =
	 * inputstring.replace("%zb",isinitial); return inputstring; }
	 */
	/*
	 * public String f(String inputstring, int amount, double price, String name,
	 * String isstatic, String isinitial, HyperPlayer player, String owner) {
	 * inputstring = inputstring.replace("%a",amount+""); inputstring =
	 * inputstring.replace("%y",player.getName()); inputstring =
	 * inputstring.replace("%n",name); inputstring =
	 * inputstring.replace("%p",price+""); inputstring =
	 * inputstring.replace("%c",get("CURRENCY")); inputstring =
	 * inputstring.replace("%za",isstatic); inputstring =
	 * inputstring.replace("%zb",isinitial); inputstring =
	 * inputstring.replace("%zc",owner); return inputstring; }
	 */

}
