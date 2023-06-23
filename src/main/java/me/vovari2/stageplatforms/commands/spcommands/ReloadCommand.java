package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SPCommand {
    public ReloadCommand(CommandSender sender, String[] args, SP plugin){
        super(sender, args, plugin);
    }

    @Override
    public void execute(){
        SP.getInstance().onDisable();
        SP.getInstance().onEnable();
        sender.sendMessage(SPText.getComponent("message.reload"));
    }
}
