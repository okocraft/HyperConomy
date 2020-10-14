package regalowl.hyperconomy.minecraft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import regalowl.hyperconomy.HyperConomy;
import regalowl.simpledatalib.CommonFunctions;

public class HLocation implements Serializable {

	private static final long serialVersionUID = -1750045947840867723L;
	private double x;
	private double y;
	private double z;
	private String world;

	public HLocation(HLocation l) {
		if (l == null)
			return;
		this.world = l.getWorld();
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
	}

	public HLocation(String world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public String getWorld() {
		return world;
	}

	public int getBlockX() {
		return (int) Math.floor(x);
	}

	public int getBlockY() {
		return (int) Math.floor(y);
	}

	public int getBlockZ() {
		return (int) Math.floor(z);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public void convertToBlockLocation() {
		this.x = getBlockX();
		this.y = getBlockY();
		this.z = getBlockZ();
	}

	public boolean isLoaded(HyperConomy hc) {
		return hc.getMC().isLoaded(this);
	}

	public void load(HyperConomy hc) {
		hc.getMC().load(this);
	}

	public HBlock getBlock(HyperConomy hc) {
		return new HBlock(hc, this);
	}

	@Override
	public String toString() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("x", x + "");
		data.put("y", y + "");
		data.put("z", z + "");
		data.put("world", world);
		return CommonFunctions.implodeMap(data);
	}

	public String toReadableString() {
		return getBlockX() + "," + getBlockY() + "," + getBlockZ() + "(" + getWorld() + ")";
	}

	public String toBlockString() {
		return getBlockX() + "|" + getBlockY() + "|" + getBlockZ() + "|" + getWorld();
	}

	public static HLocation fromBlockString(String blockString) {
		ArrayList<String> parts = CommonFunctions.explode(blockString, "|");
		return new HLocation(parts.get(3), Integer.parseInt(parts.get(0)), Integer.parseInt(parts.get(1)),
				Integer.parseInt(parts.get(2)));
	}

	public HLocation down() {
		return new HLocation(this.world, this.x, this.y - 1, this.z);
	}

	public HLocation up() {
		return new HLocation(this.world, this.x, this.y + 1, this.z);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof HLocation)) {
			return false;
		}
		HLocation hLocation = (HLocation) o;
		return x == hLocation.x && y == hLocation.y && z == hLocation.z && Objects.equals(world, hLocation.world);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, world);
	}

}
