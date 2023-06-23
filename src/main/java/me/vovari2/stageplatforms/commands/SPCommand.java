package me.vovari2.stageplatforms.commands;

import me.vovari2.stageplatforms.SP;
import org.bukkit.command.CommandSender;

public abstract class SPCommand {

    protected CommandSender sender;
    protected String[] args;
    protected SP plugin;

    public SPCommand(CommandSender sender, String[] args, SP plugin){
        this.sender = sender;
        this.args = args;
        this.plugin = plugin;
    }

    public abstract void execute() throws Exception;
}
