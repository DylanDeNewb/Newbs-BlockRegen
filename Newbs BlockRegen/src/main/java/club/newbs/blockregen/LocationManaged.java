package club.newbs.blockregen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class LocationManaged {

    private Location loc;
    private Material mat;
    private int regenTime;
    private int slot;
    private boolean broken;

    public LocationManaged(Location loc, Material mat, int regenTime, int slot){
        this.loc = loc;
        this.mat = mat;
        this.regenTime = regenTime;
        this.slot = slot;
        this.broken = false;
    }

    public boolean save(){
        //Save loc
        FileConfiguration config = NewbsBlockRegen.getCore().getBlockConfig();
        if(config == null) { return false; }

        config.set("blocks." + slot + ".material",this.mat.toString());
        config.set("blocks." + slot + ".regenTime", this.regenTime);
        config.set("blocks." + slot + ".active", true);
        config.set("blocks." + slot + ".world", this.loc.getWorld().getName());
        config.set("blocks." + slot + ".x", this.loc.getX());
        config.set("blocks." + slot + ".y", this.loc.getY());
        config.set("blocks." + slot + ".z", this.loc.getZ());

        NewbsBlockRegen.getCore().getBlockFile().reload(true);

        return true;
    }

    public Material getMaterial() {
        return mat;
    }

    public int getRegenTime() {
        return regenTime;
    }

    public Location getLocation() {
        return loc;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public boolean setRegenTime(int regenTime) {
        this.regenTime = regenTime;

        FileConfiguration config = NewbsBlockRegen.getCore().getBlockConfig();
        if(config == null) { return false; }

        config.set("blocks." + slot + ".regenTime", this.regenTime);

        NewbsBlockRegen.getCore().getBlockFile().reload(true);

        return true;
    }
}
