package regalowl.hyperconomy.command;

import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.HyperEconomy;
import regalowl.hyperconomy.display.ItemDisplayHandler;
import regalowl.hyperconomy.minecraft.HLocation;

public class Makedisplay extends BaseCommand {

	public Makedisplay(HyperConomy hc) {
		super(hc, true);
	}

	@Override
	public CommandData onCommand(CommandData data) {
		if (!validate(data))
			return data;
		if (!hc.getConf().getBoolean("enable-feature.item-displays")) {
			data.addResponse(L.get("ENABLE_ITEM_DISPLAYS"));
			return data;
		}
		ItemDisplayHandler itdi = hc.getItemDisplay();
		HyperEconomy he = hp.getHyperEconomy();

		if (args.length == 1) {
			HLocation sl = hp.getTargetLocation();
			String name = args[0];
			if (he.itemTest(name)) {
				itdi.addDisplay(sl.getX(), sl.getY() + 1, sl.getZ(), sl.getWorld(), name);
			} else {
				data.addResponse(L.get("INVALID_ITEM_NAME"));
			}
		} else if (args.length == 2 && args[1].equalsIgnoreCase("u")) {
			String name = args[0];
			if (he.itemTest(name)) {
				double x = hp.getLocation().getX();
				double y = hp.getLocation().getY();
				double z = hp.getLocation().getZ();
				String w = hp.getLocation().getWorld();
				itdi.addDisplay(x, y, z, w, name);
			} else {
				data.addResponse(L.get("INVALID_ITEM_NAME"));
			}
		} else {
			data.addResponse(L.get("MAKEDISPLAY_INVALID"));
		}
		return data;
	}

}
