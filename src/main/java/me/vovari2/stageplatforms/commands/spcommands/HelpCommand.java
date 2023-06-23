package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SPCommand {
    public HelpCommand(CommandSender sender, String[] args, SP plugin){
        super(sender, args, plugin);
    }

    @Override
    public void execute() throws SPException {
        // Проверки на количество параметров
        if (args.length > 1)
            throw new SPException(SPText.getComponent("warning.too_many_arguments"));

        sender.sendMessage(SPText.getComponent("message.help_message"));
    }
}
