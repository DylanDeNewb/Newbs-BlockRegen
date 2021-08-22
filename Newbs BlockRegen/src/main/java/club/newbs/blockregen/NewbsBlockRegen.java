package club.newbs.blockregen;

import club.newbs.blockregen.api.BRMessages;
import club.newbs.blockregen.api.NFile;
import club.newbs.blockregen.api.commands.NCommandLoad;
import club.newbs.blockregen.event.BlockInteractEvents;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class NewbsBlockRegen extends JavaPlugin {

    private static NewbsBlockRegen core;
    private String prefix;

    private static NFile blockFile;
    private static NFile messageFile;

    private LocationManager manager;
    private NCommandLoad cmdmap;

    @Override
    public void onEnable() {
        // Plugin startup logic
        long start = System.currentTimeMillis();

        manager = new LocationManager(this);
        manager.load();

        cmdmap = new NCommandLoad(this);
        cmdmap.load();

        events();

        Bukkit.getConsoleSender().sendMessage("Plugin started in " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDisable() {
        for(LocationManaged loc : getManager().getManaged()){
            loc.getLocation().getBlock().setType(loc.getMaterial());
        }

        getMessageFile().save();
    }

    @Override
    public void onLoad() {
        core = this;

        blockFile = new NFile(this,"blocks.yml");
        messageFile = new NFile(this, "messages.yml");

        BRMessages.loadMessages(this);
        prefix = BRMessages.getMessage(BRMessages.PREFIX);
    }

    private void events(){
        Stream.of(
                new BlockInteractEvents(this)
        ).forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
    }

    public String getPrefix() {
        return prefix;
    }

    public static NewbsBlockRegen getCore() {
        return core;
    }

    public FileConfiguration getBlockConfig(){
        return blockFile.getAsYaml();
    }

    public NFile getBlockFile() {
        return blockFile;
    }

    public FileConfiguration getMessageConfig(){
        return messageFile.getAsYaml();
    }

    public NFile getMessageFile() {
        return messageFile;
    }

    public LocationManager getManager() {
        return manager;
    }

}
