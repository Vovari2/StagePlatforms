package me.vovari2.stageplatforms.objects;

import me.vovari2.stageplatforms.SP;

public class SPDelayPaste {
    private final SPPlatform platform;
    private final SPSchematic schematic;
    private final String stageName;
    private final int second;

    public SPDelayPaste(SPPlatform platform, SPSchematic schematic, String stageName){
        this.platform = platform;
        this.schematic = schematic;
        this.stageName = stageName;
        second = SP.getTaskSeconds().getSecondsAfterPeriod(SP.getInstance().timer);
    }

    public SPPlatform getPlatform(){
        return platform;
    }
    public SPSchematic getSchematic(){
        return schematic;
    }
    public String getStageName(){
        return stageName;
    }
    public int getSecond(){
        return second;
    }
}
