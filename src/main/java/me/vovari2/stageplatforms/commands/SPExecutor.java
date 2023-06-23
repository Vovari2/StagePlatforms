package me.vovari2.stageplatforms.commands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.spcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SPExecutor implements CommandExecutor {
    private final SP plugin;
    public SPExecutor(SP plugin){
        this.plugin = plugin;
        HashMap<String, Class<? extends SPCommand>> commands = plugin.commands;
        commands.put("help", HelpCommand.class);
        commands.put("list", ListCommand.class);
        commands.put("info", InfoCommand.class);
        commands.put("paste", PasteCommand.class);
        commands.put("clear", ClearCommand.class);
        commands.put("tp", TpCommand.class);
        commands.put("reload", ReloadCommand.class);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try{
            // Проверка, имеются ли права доступа у игрока
            if (!sender.hasPermission("stage_platforms.*"))
                throw new SPException(SPText.getComponent("warning.dont_have_permission"));

            // Проверка, равно ли количество параметров нулю
            if (args.length == 0)
                throw new SPException(SPText.getComponent("message.help_message"));

            String str = args[0].toLowerCase();
            if (!plugin.commands.containsKey(str))
                throw new SPException(SPText.getComponent("warning.command_incorrectly"));

            if (plugin.isDisable && !(args.length == 1 && args[0].equals("reload")))
                throw new SPException(SPText.getComponent("warning.command_incorrectly"));

            SPCommand tdCommand = plugin.commands.get(str).getDeclaredConstructor(CommandSender.class, String[].class, SP.class).newInstance(sender, args, plugin);
            tdCommand.execute();
        }
        catch (SPException error){
            sender.sendMessage(error.getCompMessage());
        }
        catch (Exception error){
            error.printStackTrace();
            sender.sendMessage(SPText.getComponent("warning.command_incorrectly"));
        }
        return true;
    }
}
