package regalowl.hyperconomy.minecraft;

import java.io.Serializable;
import java.util.Objects;

import regalowl.hyperconomy.HyperConomy;

public class HBlock implements Serializable {

	private static final long serialVersionUID = 6789704788932270058L;
	private transient HyperConomy hc;
	private HLocation location;

	public HBlock(HyperConomy hc, HLocation location) {
		this.hc = hc;
		this.location = new HLocation(location);
		this.location.convertToBlockLocation();
	}

	public HLocation getLocation() {
		return new HLocation(location);
	}

	public boolean isChest() {
		return hc.getMC().isChest(location);
	}

	public boolean isInfoSign() {
		return hc.getMC().isInfoSign(location);
	}

	public boolean isTransactionSign() {
		return hc.getMC().isTransactionSign(location);
	}

	public HBlock getFirstNonAirBlockBelow() {
		return hc.getMC().getFirstNonAirBlockInColumn(location);
	}

	public boolean canFall() {
		return hc.getMC().canFall(this);
	}

	public boolean isLoaded() {
		return hc.getMC().isLoaded(location);
	}

	public void load() {
		hc.getMC().load(location);
	}

	public HBlock[] getSurroundingBlocks() {
		HBlock[] blocks = new HBlock[6];
		HLocation l1 = new HLocation(location);
		l1.setY(l1.getY() + 1);
		blocks[0] = new HBlock(hc, l1);
		HLocation l2 = new HLocation(location);
		l2.setY(l2.getY() - 1);
		blocks[1] = new HBlock(hc, l2);
		HBlock[] nsew = getNorthSouthEastWestBlocks();
		blocks[2] = nsew[0];
		blocks[3] = nsew[1];
		blocks[4] = nsew[2];
		blocks[5] = nsew[3];
		return blocks;
	}

	public HBlock[] getNorthSouthEastWestBlocks() {
		HBlock[] blocks = new HBlock[4];
		HLocation l1 = new HLocation(location);
		l1.setX(l1.getX() + 1);
		blocks[0] = new HBlock(hc, l1);
		HLocation l2 = new HLocation(location);
		l2.setX(l2.getX() - 1);
		blocks[1] = new HBlock(hc, l2);
		HLocation l3 = new HLocation(location);
		l3.setZ(l3.getZ() + 1);
		blocks[2] = new HBlock(hc, l3);
		HLocation l4 = new HLocation(location);
		l4.setZ(l4.getZ() - 1);
		blocks[3] = new HBlock(hc, l4);
		return blocks;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HBlock)) {
			return false;
		}
		HBlock hBlock = (HBlock) o;
		return Objects.equals(location, hBlock.location);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(location);
	}
}
