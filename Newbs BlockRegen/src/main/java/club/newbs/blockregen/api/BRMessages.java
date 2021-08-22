package club.newbs.blockregen.api;

import club.newbs.blockregen.NewbsBlockRegen;
import org.bukkit.Bukkit;

import static org.bukkit.ChatColor.*;

public enum BRMessages {

    PREFIX(NUtils.translate("&b&lN&3&lC &7")),
    INSUFFICIENT_PERMS(NUtils.translate("&7Insufficient permissions to access this command.")),
    INSUFFICIENT_ENTITY(NUtils.translate("&7Insufficient entity, player required.")),
    INSUFFICIENT_PLAYER(NUtils.translate("&7Insufficient player, please check the name.")),
    INSUFFICIENT_NUMBER(NUtils.translate("&Insufficient number, please use a valid number.")),
    UPDATE(NUtils.translate("&7You are currently running version &6% &7which is &c&lOUTDATED")),
    SOFT_DEPEND_FOUND(NUtils.translate("&7Soft-Dependency &6&l% &7found.")),
    SOFT_DEPEND_NOT_FOUND(NUtils.translate("&7Soft-Dependency &6&l% &7not found, skipping.")),
    RELOAD(NUtils.translate("&7Plugin has been &6&lRELOADED")),
    BLOCK_PLACEMENT(NUtils.translate("&7You can &c&lNOT &7build here!")),
    BLOCK_BREAK(NUtils.translate("&7You are interacting with a &6&lHARVESTED &7block!")),
    BLOCK_CREATE(NUtils.translate("&7Block &a&lCREATED&7, with &6&l%")),
    BLOCK_REMOVE(NUtils.translate("&7Block &c&lREMOVED&7, with &6&l%")),
    BLOCK_DUPLICATE(NUtils.translate("&7You tried &c&lDUPLICATING &7a &6&lHARVESTABLE &7block!")),
    BLOCK_INVALID(NUtils.translate("&7You tried interacting with a &6&lNORMAL &7block!")),
    EDIT_TIME(NUtils.translate("&7Set regen-time to &b&l%&7, for &6&l^"));

    private String message;

    BRMessages(String message){
        this.message=message;
    }

    public void setMessage(String msg){
        this.message = msg;
    }

    public static String getMessage(BRMessages message){
        return message.message;
    }

    public static void loadMessages(NewbsBlockRegen core){
        for(BRMessages msg : BRMessages.values()){
            String mesg = core.getMessageConfig().getString("messages." + msg.toString());
            if(mesg != null){
                msg.setMessage(NUtils.translate(mesg));
            }
        }
    }

    public static void list(){
        for(BRMessages msg : BRMessages.values()){
            Bukkit.getConsoleSender().sendMessage(msg.toString());
        }
    }

}
