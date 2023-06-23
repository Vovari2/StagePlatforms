package me.vovari2.stageplatforms.utils;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.objects.SPPlatform;
import me.vovari2.stageplatforms.objects.SPSchematic;
import me.vovari2.stageplatforms.objects.SPStage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigUtils {
    private static FileConfiguration loadConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void Initialization() throws Exception {
        File dataFolder = SP.getInstance().getDataFolder();

        // Загрузка основного файла конфигурации
        if (dataFolder.mkdir())
            TextUtils.sendInfoMessage("Folder \"StagePlatforms\" in \"plugins\" was created!");

        File file = new File(dataFolder, "text.yml");
        if (!file.exists())
            SP.getInstance().saveResource("text.yml", false);
        FileConfiguration config = loadConfiguration(file);

        loadLocale(config);

        file = new File(dataFolder, "config.yml");
        if (!file.exists())
            SP.getInstance().saveResource("config.yml", false);
        config = loadConfiguration(file);

        loadSettings(config);
    }

    // Загрузка параметров основного конфига
    private static void loadSettings(FileConfiguration config) throws Exception {
        SP plugin = SP.getInstance();
        plugin.timer = config.getInt("timer", 10);
        plugin.radius_notice = config.getDouble("radius_notice", 100);
        plugin.amountOnOnePage = config.getInt("amount_platform_on_list", 8);


        // Загрузка платформ из конфига
        ConfigurationSection section = config.getConfigurationSection("platforms");
        if (section == null)
            throw new Exception("Section platforms is null!");
        for(String pathPlatform : section.getKeys(false)){
            String fullPathPlatform = "platforms." + pathPlatform;

            String centerStr = config.getString(fullPathPlatform + ".center_point");
            if (centerStr == null)
                throw new Exception("Value " + fullPathPlatform + ".center_point is null!");

            BlockVector3 maxDimensions = BlockVector3.ONE;

            String schematicStr = config.getString(fullPathPlatform + ".general_schematic");
            if (schematicStr == null)
                throw new Exception("Value " + fullPathPlatform + ".general_schematic is null!");

            Clipboard schematic = loadSchematic(schematicStr);
            if (schematic == null)
                throw new Exception("Schematic " + fullPathPlatform + ".general_schematic does not exist!");
            if (checkSchematic(schematic))
                throw new Exception("Schematic " + fullPathPlatform + ".general_schematic is not asymmetrical!");
            setCenterOrigin(schematic);
            maxDimensions = getMaxBlockVector3(maxDimensions, schematic.getDimensions());

            HashMap<String, SPStage> stages = new HashMap<>();
            ConfigurationSection sectionStages = config.getConfigurationSection(fullPathPlatform + ".stages");
            if (sectionStages != null)
                for (String pathStage : sectionStages.getKeys(false)){
                    String fullPathStage = fullPathPlatform + ".stages." + pathStage;

                    String schematicStageStr = config.getString(fullPathStage);
                    if (schematicStageStr == null)
                        throw new Exception("Value " + fullPathStage + " is null!");
                    Clipboard schematicStage = loadSchematic(schematicStageStr);
                    if (schematicStage == null)
                        throw new Exception("Schematic " + fullPathStage + " does not exist!");
                    if (checkSchematic(schematicStage))
                        throw new Exception("Schematic " + fullPathStage + " is not asymmetrical!");
                    setCenterOrigin(schematicStage);
                    maxDimensions = getMaxBlockVector3(maxDimensions, schematicStage.getDimensions());

                    stages.put(pathStage, new SPStage(pathStage, new SPSchematic(schematicStageStr, schematicStage)));
                }

            plugin.platforms.put(pathPlatform, new SPPlatform(pathPlatform, loadLocation(centerStr), maxDimensions, new SPSchematic(schematicStr, schematic), stages));
        }

    }
    public static Location loadLocation(String pos) throws Exception {
        try{
            String[] strings = pos.split(" ");
            String worldStr = strings[0];
            World world = Bukkit.getWorld(worldStr);
            if (world == null)
                throw new Exception("World in platform location is null!");
            return new Location(world, Integer.parseInt(strings[1]), Integer.parseInt(strings[2]), Integer.parseInt(strings[3]));
        }
        catch(Exception error){
            error.printStackTrace();
            throw new Exception("Position not loaded!");
        }
    }
    // Работа со схематиками
    public static Clipboard loadSchematic(String schem) throws Exception{
        Clipboard clipboard;

        File file = new File(WorldEditPlugin.getPlugin(WorldEditPlugin.class).getDataFolder() + "/schematics/" + schem + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null)
            throw new Exception("Schematic not loaded!");
        ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()));
        clipboard = reader.read();

        return clipboard;
    }
    public static void setCenterOrigin(Clipboard clipboard){
        BlockVector3 size = clipboard.getDimensions();
        clipboard.setOrigin(clipboard.getMinimumPoint().add(size.getBlockX()/2, size.getBlockY(), size.getBlockZ()/2));
    }
    public static BlockVector3 getMaxBlockVector3(BlockVector3 pos1, BlockVector3 pos2){
        return BlockVector3.at(Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }
    public static boolean checkSchematic(Clipboard clip){
        BlockVector3 size = clip.getDimensions();
        return size.getBlockX() % 2 != 1 || size.getBlockZ() % 2 != 1;
    }


    // Загрузка локализации плагина
    private static void loadLocale(FileConfiguration configLocale) throws Exception{
        // Надписи, которые сразу можно конвертировать в Component
        String[] array = new String[] {
                "warning.plugin_is_disable",
                "message.reload",
                "message.help_message",
                "message.list_footer",
                "warning.you_not_player",
                "warning.dont_have_permission",
                "warning.command_incorrectly",
                "warning.too_many_arguments",
                "warning.platform_name_incorrectly",
                "warning.stage_name_incorrectly",
                "warning.list_page_incorrectly"
        };
        HashMap<String, Object> localeTexts = new HashMap<>();
        for (String str : array)
            localeTexts.put(str, convertStringToComponent(configLocale.getString(str), str));

        // Надписи, которые нужно оставить в виде строк
        array = new String[] {
                "message.tp",
                "message.clear",
                "message.list_header",
                "message.list_string",
                "message.info_string_stages",
                "message.delay_paste",
                "message.delay_paste_stage",
                "message.paste",
                "message.paste_stage"
        };
        for (String str : array)
            localeTexts.put(str, checkConfigString(configLocale, str));

        // Списки надписей, которые можно сразу конвертировать в списки Component
        array = new String[] {
                // В будущем тут может что-то будет
        };
        HashMap<String, List<Component>> localeListComponent = new HashMap<>();
        for (String str : array)
            localeListComponent.put(str, convertStringListToComponent(configLocale.getStringList(str), str));

        // Списки надписей, которые нужно оставить в виде строк
        array = new String[] {
                "message.info"
        };
        HashMap<String, List<String>> localeListString = new HashMap<>();
        for (String str : array){
            List<String> listStrings = new ArrayList<>();
            for (String str2 : configLocale.getStringList(str))
                listStrings.add(checkString(str2, str));
            localeListString.put(str, listStrings);
        }

        // Запись всех списков в основные переменные
        SP.getInstance().locale = new SPText(localeTexts, localeListComponent, localeListString);
    }
    private static String checkConfigString(FileConfiguration config, String path) throws Exception{
        return checkString(config.getString(path), path);
    }
    private static String checkString(String text, String path) throws Exception{
        if (text == null)
            throw new Exception("Text \"" + path + "\" not exists!");
        if (text.contains("&") || text.contains("§"))
            throw new Exception("Title \"" + path + " must not have char \"&\" or \"§\"!");
        return text;
    } // Проверяет строку на null и на символы & или §

//    private static String[] checkArrayString(List<String> texts, String path) throws Exception{
//        String[] newTexts = new String[texts.size()];
//        for(int i = 0; i < texts.size(); i++)
//            newTexts[i] = checkString(texts.get(i), path);
//        return newTexts;
//    } // Проверяет строки в массиве на null и на символы & или §
//    private static List<String> checkListString(List<String> texts, String path) throws Exception{
//        List<String> newTexts = new ArrayList<>();
//        for (String text : texts)
//            newTexts.add(checkString(text, path));
//        return newTexts;
//    } // Проверяет строки в списке на null и на символы & или §

    private static Component convertStringToComponent(String text, String path) throws Exception{
        return MiniMessage.miniMessage().deserialize(checkString(text, path));
    } // С проверкой конвертирует строку в Component
    private static List<Component> convertStringListToComponent(List<String> texts, String path)throws Exception{
        List<Component> listComponent = new ArrayList<>();
        for (String text : texts)
            listComponent.add(convertStringToComponent(text, path));
        return listComponent;
    } // С проверкой конвертирует список строк в список Component

}
