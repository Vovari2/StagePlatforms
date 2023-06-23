package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import me.vovari2.stageplatforms.objects.SPDelayPaste;
import me.vovari2.stageplatforms.objects.SPPlatform;
import me.vovari2.stageplatforms.objects.SPSchematic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasteCommand extends SPCommand {
    public PasteCommand(CommandSender sender, String[] args, SP plugin){
        super(sender, args, plugin);
    }

    @Override
    public void execute() throws SPException {
        // Проверки на количество параметров
        if (args.length > 3)
            throw new SPException(SPText.getComponent("warning.too_many_arguments"));

        switch(args.length){
            // Команда: /stp paste
            case 1: {
                if (!(sender instanceof Player))
                    throw new SPException(SPText.getComponent("warning.you_not_player"));
                Player player = (Player) sender;

                SPPlatform platform = SPPlatform.getPlatform(player.getLocation());
                SP.getTaskSeconds().waitDelayPaste.put(platform.getName(), new SPDelayPaste(platform, platform.getSchematic(), "*"));
                platform.sendMessageNearPlayers(SPText.replacePlaceHolder("message.delay_paste", "%second%", String.valueOf(plugin.timer)));
            } break;
            // Команда: /stp paste [platformName]
            case 2: {
                String platformsName = args[1];
                if (!plugin.platforms.containsKey(platformsName))
                    throw new SPException(SPText.getComponent("warning.platform_name_incorrectly"));
                SPPlatform platform = plugin.platforms.get(platformsName);

                SP.getTaskSeconds().waitDelayPaste.put(platformsName, new SPDelayPaste(platform, platform.getSchematic(), "*"));
                platform.sendMessageNearPlayers(SPText.replacePlaceHolder("message.delay_paste", "%second%", String.valueOf(plugin.timer)));
            } break;
            // Команда: /stp paste [platformName] [stageName]
            case 3: {
                String platformsName = args[1];
                if (!plugin.platforms.containsKey(platformsName))
                    throw new SPException(SPText.getComponent("warning.platform_name_incorrectly"));
                SPPlatform platform = plugin.platforms.get(platformsName);

                String stageName = args[2];
                if (!platform.getStages().containsKey(stageName) && !stageName.equals("*"))
                    throw new SPException(SPText.getComponent("warning.stage_name_incorrectly"));

                SPSchematic schematic = stageName.equals("*") ? platform.getSchematic() : platform.getStages().get(stageName).getSchematic();

                SP.getTaskSeconds().waitDelayPaste.put(platformsName, new SPDelayPaste(platform, schematic, stageName));
                platform.sendMessageNearPlayers(
                        stageName.equals("*") ?
                        SPText.replacePlaceHolder("message.delay_paste", "%second%", String.valueOf(plugin.timer)) :
                        SPText.replacePlaceHolder("message.delay_paste_stage", new String[]{"%stage_name%", "%second%"}, new String[]{stageName, String.valueOf(plugin.timer)}));
            } break;
            default: throw new SPException(SPText.getComponent("warning.command_incorrectly"));
        }
    }
}
