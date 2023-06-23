package me.vovari2.stageplatforms;

import me.vovari2.stageplatforms.commands.SPCommand;
import me.vovari2.stageplatforms.commands.SPExecutor;
import me.vovari2.stageplatforms.commands.SPTabCompleter;
import me.vovari2.stageplatforms.objects.SPPlatform;
import me.vovari2.stageplatforms.utils.ConfigUtils;
import me.vovari2.stageplatforms.utils.TextUtils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public final class SP extends JavaPlugin {

    private static SP plugin;
    public boolean isDisable;
    public SPText locale; // Переменная, хранящая все надписи от локализации

    public HashMap<String, Class<? extends SPCommand>> commands;

    public SPTaskSeconds taskSeconds;

    public int timer;
    public double radius_notice;
    public int amountOnOnePage;
    public HashMap<String, SPPlatform> platforms;

    @Override
    public void onEnable() {
        long loadingTime = System.currentTimeMillis();

        plugin = this;
        commands = new HashMap<>();

        taskSeconds = new SPTaskSeconds(this);
        taskSeconds.runTaskTimer(this, 20, 20);

        platforms = new HashMap<>();

        try{
            ConfigUtils.Initialization();
        } catch(Exception error){
            disablePlugin(error.getMessage());
            return;
        }

        PluginCommand command = getCommand("stage_platforms");
        if (command != null){
            command.setExecutor(new SPExecutor(this));
            command.setTabCompleter(new SPTabCompleter(this));
        }
        else TextUtils.sendWarningMessage("Command \"stage_platforms\" is null!");


        TextUtils.sendInfoMessage("Plugin enabled for " + (System.currentTimeMillis() - loadingTime) + " ms");
    }

    @Override
    public void onDisable() {
        if (taskSeconds != null)
            taskSeconds.cancel();
        TextUtils.sendInfoMessage("Plugin disabled!");
    }
    public static void disablePlugin(String message) {
        TextUtils.sendWarningMessage(message);
        plugin.isDisable = true;
    }

    public static SP getInstance(){
        return plugin;
    }
    public static ConsoleCommandSender getConsoleSender(){
        return plugin.getServer().getConsoleSender();
    }
    public static Logger getPluginLogger(){
        return plugin.getServer().getLogger();
    }

    public static SPTaskSeconds getTaskSeconds(){
        return plugin.taskSeconds;
    }
}
