package me.vovari2.stageplatforms.objects;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SPPlatform {
    private final String name;
    private final Location center;
    private final SPSchematic schematic;
    private final CuboidRegion region;
    private final HashMap<String, SPStage> stages;

    public SPPlatform(String name, Location center, BlockVector3 maxDimensions, SPSchematic schematic, HashMap<String, SPStage> stages){
        this.name = name;
        this.center = center;
        this.region = new CuboidRegion(
                BukkitAdapter.adapt(center.getWorld()),
                BlockVector3.at(
                        center.getBlockX() + (maxDimensions.getBlockX() / 2),
                        center.getBlockY() - 1,
                        center.getBlockZ() + (maxDimensions.getBlockZ() / 2)),
                BlockVector3.at(
                        center.getBlockX() - (maxDimensions.getBlockX() / 2),
                        center.getBlockY() - maxDimensions.getBlockY(),
                        center.getBlockZ() - (maxDimensions.getBlockZ() / 2)));
        this.schematic = schematic;
        this.stages = stages;
    }
    public void clear(){
        EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(center.getWorld()));
        try { editSession.setBlocks(region, BukkitAdapter.adapt(Material.AIR.createBlockData()).toBaseBlock()); }
        catch (MaxChangedBlocksException e) { TextUtils.sendWarningMessage("Change too many blocks!"); }
        editSession.close();
    }
    public void sendMessageNearPlayers(Component message){
        double radius = SP.getInstance().radius_notice;
        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getLocation().getWorld().equals(center.getWorld()) && player.getLocation().distance(center) <= radius)
                player.sendMessage(message);
    }

    public String getName() {
        return name;
    }
    public Location getCenter(){
        return center;
    }
    public SPSchematic getSchematic(){
        return schematic;
    }
    public HashMap<String, SPStage> getStages(){
        return stages;
    }

    public static SPPlatform getPlatform(Location location){
        double minDistance = Double.MAX_VALUE;
        SPPlatform platform = null;
        for (SPPlatform targetPlatform : SP.getInstance().platforms.values()){
            double distance = targetPlatform.center.distance(location);
            if (targetPlatform.center.getWorld().equals(location.getWorld()) && minDistance > distance) {
                minDistance = distance;
                platform = targetPlatform;
            }
        }
        return platform;
    }
}
