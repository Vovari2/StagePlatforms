package me.vovari2.stageplatforms;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPText {
    private final HashMap<String, Object> localeTexts;
    private final HashMap<String, List<Component>> localeListComponent;
    private final HashMap<String, List<String>> localeListString;

    public SPText(HashMap<String, Object> localeTexts, HashMap<String, List<Component>> localeListComponent, HashMap<String, List<String>> localeListString){
        this.localeTexts = localeTexts;
        this.localeListComponent = localeListComponent;
        this.localeListString = localeListString;
    }

    public static Component getComponent(String key){
        return (Component) SP.getInstance().locale.localeTexts.get(key);
    }
    public static List<Component> getListComponent(String key){
        return SP.getInstance().locale.localeListComponent.get(key);
    }

    public static String getString(String key){
        return (String) SP.getInstance().locale.localeTexts.get(key);
    }
    private static List<String> getListString(String key){
        return SP.getInstance().locale.localeListString.get(key);
    }

    public static Component replacePlaceHolder(String key, String placeholder, String value){
        return MiniMessage.miniMessage().deserialize(getString(key).replaceAll(placeholder, value));
    }
    public static Component replacePlaceHolder(String key, String[] placeholders, String[] values){
        String str = getString(key);
        for (int i = 0; i < placeholders.length; i++)
            str = str.replaceAll(placeholders[i], values[i]);
        return MiniMessage.miniMessage().deserialize(str);
    }

    public static List<Component> replacePlaceHolderList(String key, String placeholder, String value){
        List<Component> listComponents = new ArrayList<>();
        for (String str : getListString(key))
            listComponents.add(MiniMessage.miniMessage().deserialize(str.replaceAll(placeholder, value)));
        return listComponents;
    }
    public static List<Component> replacePlaceHolderList(String key, String[] placeholders, String[] values){
        List<Component> listComponents = new ArrayList<>();
        for(String str : getListString(key)){
            for (int i = 0; i < placeholders.length; i++)
                str = str.replaceAll(placeholders[i], values[i]);
            listComponents.add(MiniMessage.miniMessage().deserialize(str));
        }
        return listComponents;
    }
}
