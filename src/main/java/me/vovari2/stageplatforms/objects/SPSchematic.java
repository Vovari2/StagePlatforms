package me.vovari2.stageplatforms.objects;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;

public class SPSchematic {
    private final String name;
    private final Clipboard schematic;

    public SPSchematic(String name, Clipboard schematic){
        this.name = name;
        this.schematic = schematic;
    }

    public void paste(Location location) throws Exception {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
            Operation operation = new ClipboardHolder(schematic)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new Exception("Failed to paste schematic \"" + name + "\"");
        }
    }

    public String getName(){
        return name;
    }
}
