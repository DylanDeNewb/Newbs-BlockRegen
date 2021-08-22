package club.newbs.blockregen.command;

import club.newbs.blockregen.LocationManaged;
import club.newbs.blockregen.NewbsBlockRegen;
import club.newbs.blockregen.api.BRMessages;
import club.newbs.blockregen.api.commands.NCommand;
import club.newbs.blockregen.api.inventory.menu.MenuManagerException;
import club.newbs.blockregen.api.inventory.menu.MenuManagerNotSetupException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockRegenCommand extends NCommand {

    public NewbsBlockRegen core;

    public BlockRegenCommand(NewbsBlockRegen core){
        this.core = core;
    }

    @Override
    public void command(Player player, String[] args) throws MenuManagerNotSetupException, MenuManagerException {
        if(args.length==0){ help(player);}
        else {
            if(args[0].equalsIgnoreCase("create")) { create(player,args); }
            else if(args[0].equalsIgnoreCase("remove")) { remove(player); }
            else if(args[0].equalsIgnoreCase("edit")) { edit(player,args); }
            else if(args[0].equalsIgnoreCase("inspect")) { inspect(player); }
            else if(args[0].equalsIgnoreCase("reload")) { reload(player); }
            else{
                help(player);
            }
        }
    }

    private void create(Player p, String[] args){
        p.getLineOfSight(null, 10).stream()
                .filter(block -> block.getType() != Material.AIR)
                .forEach(block ->{
                    if(core.getManager().isManaged(block)){
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_DUPLICATE));
                        return;
                    }

                    int time = 10;
                    try{
                         time = Integer.parseInt(args[1]);
                    }catch(NumberFormatException e){
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.INSUFFICIENT_NUMBER));
                        return;
                    }catch(Exception e){

                    }

                    //Create regen block
                    LocationManaged managed = core.getManager().create(block, time);
                    p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_CREATE).replace("%", managed.getMaterial().toString()));
                });
    }

    private void remove(Player p){
        p.getLineOfSight(null, 10).stream()
                .filter(block -> block.getType() != Material.AIR)
                .forEach(block ->{
                    if(!core.getManager().isManaged(block)){
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_INVALID));
                        return;
                    }

                    //Remove regen block
                    LocationManaged managed = core.getManager().getManaged(block);
                    Location loc = managed.getLocation();
                    Material mat = managed.getMaterial();

                    if(core.getManager().remove(managed)){
                        loc.getBlock().setType(mat);
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_REMOVE).replace("%", managed.getMaterial().toString()));
                    }
                });
    }

    private void edit(Player p, String[] args){
        p.getLineOfSight(null, 10).stream()
                .filter(block -> block.getType() != Material.AIR)
                .forEach(block ->{
                    if(!core.getManager().isManaged(block)){
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_INVALID));
                        return;
                    }

                    //Edit block regen time
                    LocationManaged managed = core.getManager().getManaged(block);

                    int time = managed.getRegenTime();
                    try{
                        time = Integer.parseInt(args[1]);
                    }catch(NumberFormatException e){
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.INSUFFICIENT_NUMBER));
                        return;
                    }catch(Exception e){

                    }

                    managed.setRegenTime(time);
                    p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.EDIT_TIME).replace("%", String.valueOf(time)).replace("^", managed.getMaterial().toString()));
                });
    }

    private void inspect(Player p){
        p.getLineOfSight(null ,10).stream()
                .filter(block -> block.getType() != Material.AIR)
                .forEach(block ->{
                    if(!core.getManager().isManaged(block)){
                        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_INVALID));
                        return;
                    }

                    LocationManaged managed = core.getManager().getManaged(block);

                    int slot = managed.getSlot();
                    Material material = managed.getMaterial();
                    int time = managed.getRegenTime();
                    boolean broken = managed.isBroken();

                    p.sendMessage(core.getPrefix() + "Slot: " + slot);
                    p.sendMessage(core.getPrefix() + "Material: " + material.toString());
                    p.sendMessage(core.getPrefix() + "Regen-Time: " + time);
                    p.sendMessage(core.getPrefix() + "Broken? " + broken);
                });
    }

    private void help(Player p){
        p.sendMessage(core.getPrefix() + "/" + getCommand() + " <create|remove|edit|inspect|reload> <integer - create|edit>");
    }

    private void reload(Player p){
        core.getBlockFile().reload(false);
        core.getMessageFile().reload(false);

        core.getManager().loadConfigurable(core.getBlockConfig());
        BRMessages.loadMessages(core);

        p.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.RELOAD));
    }

    @Override
    public void command(ConsoleCommandSender console, String[] args) {
        console.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.INSUFFICIENT_ENTITY));
    }

    @Override
    public List<String> tabcomplete(CommandSender sender, Command cmd, String alias, String[] args) {

        List<String> options = new ArrayList<>();

        if(!cmd.getName().equalsIgnoreCase(getCommand())) { return options; }

        Player p = (Player) sender;
        if(!p.hasPermission("newbs.blockregen")) { return options; }

        if(args.length == 1){
            options.add("create");
            options.add("remove");
            options.add("edit");
            options.add("inspect");
            options.add("reload");

            return options;
        }

        return new ArrayList<>(0);
    }

    @Override
    public NewbsBlockRegen getCore() {
        return core;
    }

    @Override
    public String getCommand() {
        return "blockregen";
    }

    @Override
    public String getPermission() {
        return "newbs.blockregen";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("br","block","regen");
    }
}
