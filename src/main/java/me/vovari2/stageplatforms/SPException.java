package me.vovari2.stageplatforms;

import net.kyori.adventure.text.Component;

public class SPException extends Exception {
    private final Component compMessage;
    public SPException(Component message){
        compMessage = message;
    }
    public Component getCompMessage() {
        return compMessage;
    }
}
