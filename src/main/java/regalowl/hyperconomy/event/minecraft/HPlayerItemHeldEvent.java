package regalowl.hyperconomy.event.minecraft;

import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.event.HyperEvent;

public class HPlayerItemHeldEvent extends HyperEvent {

	private HyperPlayer hp;
	private int previousSlot;
	private int newSlot;

	public HPlayerItemHeldEvent(HyperPlayer hp, int previousSlot, int newSlot) {
		this.hp = hp;
		this.previousSlot = previousSlot;
		this.newSlot = newSlot;
	}

	public HyperPlayer getHyperPlayer() {
		return hp;
	}

	public int getPreviousSlot() {
		return previousSlot;
	}

	public int getNewSlot() {
		return newSlot;
	}
}