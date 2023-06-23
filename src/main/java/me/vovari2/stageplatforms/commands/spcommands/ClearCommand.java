package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import me.vovari2.stageplatforms.objects.SPPlatform;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand extends SPCommand {
    public ClearCommand(CommandSender sender, String[] args, SP plugin){
        super(sender, args, plugin);
    }

    @Override
    public void execute() throws SPException {
        // Проверки на количество параметров
        if (args.length > 2)
            throw new SPException(SPText.getComponent("warning.too_many_arguments"));

        SPPlatform platform;
        if (args.length == 2){
            String platformsName = args[1];
            if (!plugin.platforms.containsKey(platformsName))
                throw new SPException(SPText.getComponent("warning.platform_name_incorrectly"));
            platform = plugin.platforms.get(platformsName);
        }
        else {
            if (!(sender instanceof Player))
                throw new SPException(SPText.getComponent("warning.you_not_player"));
            Player player = (Player) sender;

            platform = SPPlatform.getPlatform(player.getLocation());
        }

        platform.clear();
        sender.sendMessage(SPText.replacePlaceHolder("message.clear", "%name%", platform.getName()));

    }
}
