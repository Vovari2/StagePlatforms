package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import me.vovari2.stageplatforms.objects.SPPlatform;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCommand  extends SPCommand {
    public TpCommand(CommandSender sender, String[] args, SP plugin){
        super(sender, args, plugin);
    }

    @Override
    public void execute() throws SPException {
        // Проверка является ли sender игроком
        if (!(sender instanceof Player))
            throw new SPException(SPText.getComponent("warning.you_not_player"));
        Player player = (Player) sender;

        // Проверка на количество параметров
        if (args.length > 2)
            throw new SPException(SPText.getComponent("warning.too_many_arguments"));
        if (args.length != 2)
            throw new SPException(SPText.getComponent("warning.command_incorrectly"));

        String platformName = args[1];
        if (!plugin.platforms.containsKey(platformName))
            throw new SPException(SPText.getComponent("warning.platform_name_incorrectly"));

        SPPlatform platform = plugin.platforms.get(platformName);
        player.teleport(platform.getCenter().clone().add(0.5D, 0, 0.5D));
        player.sendMessage(SPText.replacePlaceHolder("message.tp", "%name%", platformName));
    }
}
