package club.newbs.blockregen.event;

import club.newbs.blockregen.LocationManaged;
import club.newbs.blockregen.NewbsBlockRegen;
import club.newbs.blockregen.api.BRMessages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockInteractEvents implements Listener {

    public NewbsBlockRegen core;
    private ConfigurationSection config;

    public BlockInteractEvents(NewbsBlockRegen core){
        this.core = core;
        this.config = core.getBlockConfig();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if(core.getManager().isManaged(block)){
            LocationManaged managed = core.getManager().getManaged(block);
            player.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_PLACEMENT));

            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if(core.getManager().isManaged(block)){
            e.setCancelled(true);
            LocationManaged managed = core.getManager().getManaged(block);

            if(managed.isBroken()){
                //Send node already harvested
                player.sendMessage(core.getPrefix() + BRMessages.getMessage(BRMessages.BLOCK_BREAK));
                return;
            }else{
                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                    block.getDrops().forEach(itemStack ->
                            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), itemStack));
                }
            }

            managed.setBroken(true);
            block.getLocation().getBlock().setType(core.getManager().getRefill());

            new BukkitRunnable() {
                @Override
                public void run() {
                    managed.getLocation().getBlock().setType(managed.getMaterial());
                    managed.getLocation().getWorld().spawnParticle(core.getManager().getParticle(), managed.getLocation(), core.getManager().getParticleCount());
                    managed.getLocation().getWorld().playSound(managed.getLocation(), core.getManager().getSound(), core.getManager().getSoundCategory(),
                            core.getManager().getVolume(), core.getManager().getPitch());
                    managed.setBroken(false);
                }
            }.runTaskLater(core, managed.getRegenTime()* 20L);
        }
    }
}
