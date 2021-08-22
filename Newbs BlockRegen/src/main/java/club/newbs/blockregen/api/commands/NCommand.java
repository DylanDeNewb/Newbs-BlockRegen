package club.newbs.blockregen.api.commands;

import club.newbs.blockregen.NewbsBlockRegen;
import club.newbs.blockregen.api.BRMessages;
import club.newbs.blockregen.api.inventory.menu.MenuManagerException;
import club.newbs.blockregen.api.inventory.menu.MenuManagerNotSetupException;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class NCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args){

        if(sender instanceof Player){

            Player p = (Player) sender;

            if(p.hasPermission(getPermission())){
                try {
                    command(p, args);
                } catch (MenuManagerNotSetupException e) {
                    e.printStackTrace();
                } catch (MenuManagerException e) {
                    e.printStackTrace();
                }
                return true;
            } else{
                p.sendMessage(BRMessages.getMessage(BRMessages.INSUFFICIENT_PERMS));
                return false;
            }
        }else if(sender instanceof ConsoleCommandSender){

            ConsoleCommandSender cp = (ConsoleCommandSender) sender;

            command(cp, args);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
        return tabcomplete(sender, cmd, alias,args);
    }

    public abstract void command(Player player, String[] args) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void command(ConsoleCommandSender console, String[] args);

    public abstract List<String> tabcomplete(CommandSender sender, Command cmd, String alias, String[] args);

    public abstract NewbsBlockRegen getCore();

    public abstract String getCommand();

    public abstract String getPermission();

    public abstract List<String> getAliases();
}
