package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import me.vovari2.stageplatforms.objects.SPPlatform;
import me.vovari2.stageplatforms.utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand extends SPCommand {
    public InfoCommand(CommandSender sender, String[] args, SP plugin){
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
        List<Component> infoMessage = SPText.replacePlaceHolderList(
                "message.info",
                new String[]{
                        "%name%",
                        "%center_point%",
                        "%schematic%"
                },
                new String[]{
                        platform.getName(),
                        TextUtils.locationToString(platform.getCenter()),
                        platform.getSchematic().getName()
                });

        for (Component message : infoMessage)
            sender.sendMessage(message);

        if (platform.getStages().size() > 0){
            Component text = MiniMessage.miniMessage().deserialize("  <#EF6400>Стадии:\n");
            for (String stageName : platform.getStages().keySet())text = text.append(SPText.replacePlaceHolder(
                        "message.info_string_stages",
                        new String[]{"%stage_name%", "%schematic_name%", "%name%"},
                        new String[]{stageName, platform.getStages().get(stageName).getSchematic().getName(), platform.getName()}
                )).append(Component.text("\n"));
            sender.sendMessage(text);
        }

    }
}
