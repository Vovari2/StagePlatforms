package me.vovari2.stageplatforms.objects;

public class SPStage {
    private final String name;
    private final SPSchematic schematic;

    public SPStage(String name, SPSchematic schematic){
        this.name = name;
        this.schematic = schematic;
    }

    public String getName(){
        return name;
    }
    public SPSchematic getSchematic(){
        return schematic;
    }
}
