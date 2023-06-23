package me.vovari2.stageplatforms.utils;

import me.vovari2.stageplatforms.SP;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;

public class TextUtils {
    public static void sendInfoMessage(String message){
        SP.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("[StagePlatforms] <green>" + message));
    }
    public static void sendWarningMessage(String message){
        SP.getPluginLogger().warning("[StagePlatforms]: " + message);
    }

    public static String locationToString(Location location){
        return location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }
}
