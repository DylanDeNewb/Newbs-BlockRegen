package club.newbs.blockregen.api.commands;

import club.newbs.blockregen.NewbsBlockRegen;
import club.newbs.blockregen.command.BlockRegenCommand;
import org.bukkit.Bukkit;

import java.util.stream.Stream;

public class NCommandLoad {

    private final NewbsBlockRegen core;
    private NCmdWrapper map;

    public NCommandLoad(NewbsBlockRegen core){
        this.core = core;
    }

    public void load(){
        try {
            map = new NCmdWrapper();
        } catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }

        Stream.of(
                new BlockRegenCommand(core)
        ).forEach(command -> map.load(command));

    }

}
