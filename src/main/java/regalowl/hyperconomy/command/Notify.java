package regalowl.hyperconomy.command;

import java.util.ArrayList;

import regalowl.simpledatalib.CommonFunctions;
import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.HyperEconomy;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.event.HyperEvent;
import regalowl.hyperconomy.event.HyperEventListener;
import regalowl.hyperconomy.event.TransactionEvent;
import regalowl.hyperconomy.tradeobject.TradeObject;
import regalowl.hyperconomy.transaction.PlayerTransaction;
import regalowl.hyperconomy.transaction.TransactionType;

public class Notify extends BaseCommand implements HyperEventListener {

	public Notify(HyperConomy hc) {
		super(hc, false);
		enabled = hc.getConf().getBoolean("enable-feature.price-change-notifications");
		notifyNames = CommonFunctions.explode(hc.getConf().getString("shop.send-price-change-notifications-for"));
		hc.getHyperEventHandler().registerListener(this);
	}

	private ArrayList<String> notifyNames = new ArrayList<String>();
	private boolean enabled;

	public String getNotifyString() {
		return CommonFunctions.implode(notifyNames);
	}

	public void saveNotifyNames() {
		hc.getConf().set("shop.send-price-change-notifications-for", getNotifyString());
	}

	@Override
	public void handleHyperEvent(HyperEvent event) {
		if (event instanceof TransactionEvent) {
			TransactionEvent te = (TransactionEvent) event;
			if (te.getTransactionResponse().successful()) {
				PlayerTransaction pt = te.getTransaction();
				TransactionType tt = pt.getTransactionType();
				if (tt == TransactionType.BUY || tt == TransactionType.SELL) {
					if (pt.getHyperObject() != null) {
						TradeObject ho = pt.getHyperObject();
						if (notifyNames.contains(ho.getName())) {
							String message = L.f(L.get("SQL_NOTIFICATION"), ho.getStock(), ho.getBuyPriceWithTax(1),
									ho.getDisplayName(), ho.getEconomy());
							sendNotification(message);
						}
					}
				}
			}
		}

	}

	private void sendNotification(String message) {
		for (HyperPlayer p : hc.getHyperPlayerManager().getOnlinePlayers()) {
			if (p.hasPermission("hyperconomy.notify")) {
				p.sendMessage(message);
			}
		}
	}

	@Override
	public CommandData onCommand(CommandData data) {
		if (!validate(data))
			return data;
		try {
			HyperEconomy he = hc.getDataManager().getEconomy("default");
			TradeObject ho = he.getTradeObject(args[0]);
			if (!enabled) {
				data.addResponse(L.get("NOTIFICATIONS_DISABLED"));
				return data;
			}
			if (args[0].equalsIgnoreCase("all")) {
				notifyNames.clear();
				for (String cName : he.getNames()) {
					notifyNames.add(cName);
				}
				saveNotifyNames();
				data.addResponse(L.get("RECEIVE_NOTIFICATIONS"));
				return data;
			}
			if (args[0].equalsIgnoreCase("none")) {
				notifyNames.clear();
				saveNotifyNames();
				data.addResponse(L.get("NOT_RECEIVE_NOTIFICATIONS"));
				return data;
			}
			if (ho == null) {
				data.addResponse(L.get("OBJECT_NOT_IN_DATABASE"));
				return data;
			}
			if (notifyNames.contains(ho.getName())) {
				notifyNames.remove(ho.getName());
				saveNotifyNames();
				data.addResponse(L.f(L.get("NOT_RECEIVE_NOTIFICATIONS_S"), ho.getDisplayName()));
			} else {
				notifyNames.add(ho.getName());
				saveNotifyNames();
				data.addResponse(L.f(L.get("RECEIVE_NOTIFICATIONS_S"), ho.getDisplayName()));
			}
		} catch (Exception e) {
			data.addResponse(L.get("NOTIFY_INVALID"));
		}
		return data;
	}

}
