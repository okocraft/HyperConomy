/* This file is part of Vault.

    Vault is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Vault is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Vault.  If not, see <http://www.gnu.org/licenses/>.
 */
package regalowl.hyperconomy.bukkit;

import java.util.List;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import regalowl.hyperconomy.DataManager;
import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.account.HyperBank;
import regalowl.hyperconomy.account.HyperBankManager;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.account.HyperPlayerManager;
import regalowl.hyperconomy.util.LanguageFile;

public class Economy_HyperConomy implements Economy {
	private final String name = "HyperConomy";
	private HyperConomy hc;

	public Economy_HyperConomy(HyperConomy hc) {
		this.hc = hc;
	}

	@Override
	public boolean isEnabled() {
		if (hc == null) {
			return false;
		} else {
			return hc.getMC().isEnabled();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getBalance(String playerName) {
		return hc.getEconomyAPI().getAccountBalance(playerName);
	}

	@Override
	public double getBalance(String playerName, String world) {
		return getBalance(playerName);
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		return getBalance(player.getName());
	}

	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return getBalance(player.getName(), world);
	}

	@Override
	public boolean createPlayerAccount(String playerName) {
		hc.getEconomyAPI().createAccount(playerName);
		return true;
	}

	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		return createPlayerAccount(playerName);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		return createPlayerAccount(player.getName());
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
		return createPlayerAccount(player.getName());
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative funds");
		}
		if (hc.getEconomyAPI().hasAccount(playerName)) {
			if (hc.getEconomyAPI().accountHasBalance(playerName, amount)) {
				hc.getEconomyAPI().withdrawAccount(playerName, amount);
				return new EconomyResponse(0, 0, ResponseType.SUCCESS, null);
			} else {
				return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds");
			}
		} else {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Account does not exist");
		}
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		return withdrawPlayer(playerName, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		return withdrawPlayer(player.getName(), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		return withdrawPlayer(player.getName(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		if (amount < 0) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative funds");
		}
		if (hc.getEconomyAPI().hasAccount(playerName)) {
			hc.getEconomyAPI().depositAccount(playerName, amount);
			return new EconomyResponse(0, 0, ResponseType.SUCCESS, null);
		} else {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Account does not exist");
		}
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		return depositPlayer(playerName, amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		return depositPlayer(player.getName(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
		return depositPlayer(player.getName(), amount);
	}

	@Override
	public String format(double amount) {
		return hc.getEconomyAPI().getAmountAsString(amount);
	}

	@Override
	public String currencyNameSingular() {
		return hc.getEconomyAPI().currencyNamePlural();
	}

	@Override
	public String currencyNamePlural() {
		return hc.getEconomyAPI().currencyNamePlural();
	}

	@Override
	public boolean has(String playerName, double amount) {
		return hc.getEconomyAPI().accountHasBalance(playerName, amount);
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		return has(playerName, amount);
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return has(player.getName(), amount);
	}

	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount) {
		return has(player.getName(), amount);
	}

	@Override
	public boolean hasAccount(String playerName) {
		return hc.getEconomyAPI().hasAccount(playerName);
	}

	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return hasAccount(playerName);
	}

	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return hasAccount(player.getName());
	}

	@Override
	public boolean hasAccount(OfflinePlayer player, String world) {
		return hasAccount(player.getName(), world);
	}

	@Override
	public int fractionalDigits() {
		return hc.getEconomyAPI().fractionalDigits();
	}

	@Override
	public EconomyResponse createBank(String name, String player) {
		DataManager dm = hc.getDataManager();
		HyperPlayerManager hpm = hc.getHyperPlayerManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		LanguageFile L = hc.getLanguageFile();
		if (hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_ALREADY_EXISTS"));
		}
		if (!hpm.hyperPlayerExists(player)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("PLAYER_NOT_FOUND"));
		}
		HyperPlayer hp = hpm.getHyperPlayer(player);
		HyperBank hb = new HyperBank(hc, name, hp);
		hbm.addHyperBank(hb);
		return new EconomyResponse(0, hb.getBalance(), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		return createBank(name, player.getName());
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		LanguageFile L = hc.getLanguageFile();
		if (!hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_EXIST"));
		}
		HyperBank hb = hbm.getHyperBank(name);
		hb.delete();
		return new EconomyResponse(0, hb.getBalance(), ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		LanguageFile L = hc.getLanguageFile();
		if (!hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_EXIST"));
		}
		HyperBank hb = hbm.getHyperBank(name);
		double balance = hb.getBalance();
		if (balance < amount) {
			return new EconomyResponse(0, balance, ResponseType.FAILURE, L.get("INSUFFICIENT_FUNDS"));
		} else {
			return new EconomyResponse(0, balance, ResponseType.SUCCESS, "");
		}
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		EconomyResponse er = bankHas(name, amount);
		if (!er.transactionSuccess()) {
			return er;
		} else {
			HyperBank hb = hbm.getHyperBank(name);
			hb.setBalance(hb.getBalance() - amount);
			return new EconomyResponse(amount, hb.getBalance(), ResponseType.SUCCESS, "");
		}
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		LanguageFile L = hc.getLanguageFile();
		if (!hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_EXIST"));
		} else {
			HyperBank hb = hbm.getHyperBank(name);
			hb.setBalance(hb.getBalance() + amount);
			return new EconomyResponse(amount, hb.getBalance(), ResponseType.SUCCESS, "");
		}
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		DataManager dm = hc.getDataManager();
		HyperPlayerManager hpm = hc.getHyperPlayerManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		LanguageFile L = hc.getLanguageFile();
		if (!hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_EXIST"));
		}
		if (!hpm.hyperPlayerExists(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("PLAYER_NOT_FOUND"));
		}
		HyperBank hb = hbm.getHyperBank(name);
		HyperPlayer hp = hpm.getHyperPlayer(playerName);
		if (hb.isOwner(hp)) {
			return new EconomyResponse(0, hb.getBalance(), ResponseType.SUCCESS, "");
		} else {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_OWNER"));
		}
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		return isBankOwner(name, player.getName());
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		HyperPlayerManager hpm = hc.getHyperPlayerManager();
		LanguageFile L = hc.getLanguageFile();
		if (!hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_EXIST"));
		}
		if (!hpm.hyperPlayerExists(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("PLAYER_NOT_FOUND"));
		}
		HyperBank hb = hbm.getHyperBank(name);
		HyperPlayer hp = hpm.getHyperPlayer(playerName);
		if (hb.isMember(hp)) {
			return new EconomyResponse(0, hb.getBalance(), ResponseType.SUCCESS, "");
		} else {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_MEMBER"));
		}
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player) {
		return isBankMember(name, player.getName());
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		LanguageFile L = hc.getLanguageFile();
		if (!hbm.hasBank(name)) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, L.get("BANK_NOT_EXIST"));
		}
		HyperBank hb = hbm.getHyperBank(name);
		return new EconomyResponse(0, hb.getBalance(), ResponseType.SUCCESS, null);
	}

	@Override
	public List<String> getBanks() {
		DataManager dm = hc.getDataManager();
		HyperBankManager hbm = dm.getHyperBankManager();
		return hbm.getHyperBankNames();
	}

	@Override
	public boolean hasBankSupport() {
		return true;
	}

}