package me.vovari2.stageplatforms.commands;

import com.google.common.collect.ImmutableList;
import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.objects.SPPlatform;
import me.vovari2.stageplatforms.objects.SPStage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SPTabCompleter implements TabCompleter {
    private final SP plugin;
    private final List<String> commands;
    public SPTabCompleter(SP plugin){
        this.plugin = plugin;
        commands = ImmutableList.of("help", "info", "clear", "list", "paste", "tp", "reload");
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1)
            return commands;
        if (args.length == 2)
            switch (args[0]){
                case "info": case "clear": case "paste": case "tp": return getPlatforms();
                default: new ArrayList<>();
            }
        if (args.length == 3 && args[0].equals("paste") && plugin.platforms.containsKey(args[1]))
            return getStages(plugin.platforms.get(args[1]));
        return new ArrayList<>();
    }

    private List<String> getPlatforms(){
        List<String> list = new ArrayList<>();
        for (SPPlatform platform : plugin.platforms.values())
            list.add(platform.getName());
        return list;
    }
    public static List<String> getStages(SPPlatform platform){
        List<String> list = new ArrayList<>();
        list.add("*");
        for (SPStage stage : platform.getStages().values())
            list.add(stage.getName());
        return list;
    }
}
