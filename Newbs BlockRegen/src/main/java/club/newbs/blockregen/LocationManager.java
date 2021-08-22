package club.newbs.blockregen;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocationManager {

    private NewbsBlockRegen core;
    private static int count = 0;
    private Material refill = Material.BEDROCK;
    private Particle particle = Particle.EXPLOSION_LARGE;
    private int particleCount = 100;
    private Sound sound = Sound.ENTITY_GENERIC_EXPLODE;
    private SoundCategory soundCategory = SoundCategory.AMBIENT;
    private float volume = 1;
    private float pitch = 1;
    private List<LocationManaged> managed;

    public LocationManager(NewbsBlockRegen core){
        this.core = core;
        this.managed = new ArrayList<>();
    }

    public LocationManaged create(Block block, int time){
        if(block.getType() == Material.AIR){ return null; }

        LocationManaged object = new LocationManaged(
                block.getLocation(),
                block.getType(),
                time,
                next()
        );

        this.managed.add(object);
        object.save();

        count+=1;

        return object;
    }

    public boolean remove(LocationManaged loc){

        if(loc == null) {return false;}

        //delete file stuff
        FileConfiguration config = core.getBlockConfig();
        if(config == null){ return false; }

        config.set("blocks."+loc.getSlot()+".active", false);

        this.managed.remove(loc);
        NewbsBlockRegen.getCore().getBlockFile().reload(true);

        return true;
    }

    public boolean isManaged(Block block){
        if(block.getType() == Material.AIR) { return false; }

        for(LocationManaged loc : this.getManaged()){
            if(loc.getLocation().equals(block.getLocation())){
                return true;
            }
        }
        return false;
    }

    public boolean load(){
        FileConfiguration config = core.getBlockConfig();
        if(config == null){ return false; }

        loadConfigurable(config);

        ConfigurationSection configSection = config.getConfigurationSection("blocks");
        if(configSection == null){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Section is null!");
            return false;
        }

        Set<String> section = configSection.getKeys(false);
        if(section.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Section is empty!");
            return true;
        }

        for(String path : section){
            Material mat = Material.valueOf(config.getString("blocks." + path + ".material"));
            int regenTime = config.getInt("blocks."+path+".regenTime");
            boolean active = config.getBoolean("blocks."+path+".active");
            String w = config.getString("blocks."+path+".world");
            double x = config.getDouble("blocks."+path+".x");
            double y = config.getDouble("blocks."+path+".y");
            double z = config.getDouble("blocks."+path+".z");
            int slot = Integer.parseInt(path);

            World world = Bukkit.getServer().getWorld(w);
            Location loc = new Location(world, x, y, z);

            LocationManaged object = new LocationManaged(
                    loc,
                    mat,
                    regenTime,
                    slot
            );

            if(loc.isWorldLoaded()) {
                this.managed.add(object);
            }

            count+=1;
        }

        return true;
    }

    public void loadConfigurable(FileConfiguration config){
        String refill = config.getString("fill");
        if(refill == null) {
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Refill material is null!");
        }else{
            Material matr = Material.valueOf(refill.toUpperCase());
            setRefill(matr);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Refill loaded " + matr.toString());
        }

        String particle = config.getString("particles.type");
        if(particle == null){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Particle type is null!");
        }else{
            Particle part = Particle.valueOf(particle.toUpperCase());
            setParticle(part);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Particle loaded " + part.toString());
        }

        int count = config.getInt("particles.count");
        if(count == 0){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Particle count is null!");
        }else{
            setParticleCount(count);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Particle count loaded " + count);
        }

        String sound = config.getString("sounds.type");
        if(sound == null){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound type is null!");
        }else{
            Sound sou = Sound.valueOf(sound);
            setSound(sou);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound loaded " + sou.toString());
        }

        String soundCategory = config.getString("sounds.category");
        if(soundCategory == null){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound category is null!");
        }else{
            SoundCategory souc = SoundCategory.valueOf(soundCategory);
            setSoundCategory(souc);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound category loaded " + souc.toString());
        }

        float volume = config.getInt("sounds.volume");
        if(volume == 0){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound volume is null!");
        }else{
            setVolume(volume);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound volume loaded " + volume);
        }

        float pitch = config.getInt("sounds.pitch");
        if(pitch == 0){
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound pitch is null!");
        }else{
            setPitch(pitch);
            Bukkit.getConsoleSender().sendMessage(core.getPrefix() + "Sound pitch loaded " + pitch);
        }
    }

    public static int next(){
        return count + 1;
    }

    public List<LocationManaged> getManaged() {
        return managed;
    }

    public LocationManaged getManaged(Block block){

        for(LocationManaged loc : managed){
            if(loc.getLocation().equals(block.getLocation())){
                return loc;
            }
        }

        return null;
    }

    public Material getRefill() {
        return refill;
    }

    public void setRefill(Material refill) {
        this.refill = refill;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public float getPitch() {
        return pitch;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
