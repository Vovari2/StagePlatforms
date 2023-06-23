package me.vovari2.stageplatforms.commands.spcommands;

import me.vovari2.stageplatforms.SP;
import me.vovari2.stageplatforms.SPException;
import me.vovari2.stageplatforms.SPText;
import me.vovari2.stageplatforms.commands.SPCommand;
import me.vovari2.stageplatforms.objects.SPPlatform;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends SPCommand {
    public ListCommand(CommandSender sender, String[] args, SP plugin){
        super(sender, args, plugin);
    }

    @Override
    public void execute() throws SPException {
        // Проверки на количество параметров
        if (args.length > 2)
            throw new SPException(SPText.getComponent("warning.too_many_arguments"));

        int page, index;
        if (args.length == 1){
            page = 1;
            index = 1;
        }
        else {
            try { page = Integer.parseInt(args[1]); }
            catch(Exception error){ throw new SPException(SPText.getComponent("warning.command_incorrectly")); }

            if (!isPlatformOnPage(page))
                throw new SPException(SPText.getComponent("warning.list_page_incorrectly"));

            index = (page - 1) * plugin.amountOnOnePage + 1;
        }

        sender.sendMessage(SPText.replacePlaceHolder("message.list_header", new String[]{"%page%","%prev_page%", "%next_page%"}, new String[]{String.valueOf(page),String.valueOf(page - 1), String.valueOf(page + 1)}));
        for (String name : getPlatformsOnPage(page)){
            sender.sendMessage(SPText.replacePlaceHolder("message.list_string", new String[]{"%index%", "%name%"}, new String[]{String.valueOf(index), name}));
            index++;
        }
        sender.sendMessage(SPText.getComponent("message.list_footer"));

    }
    private static List<String> getPlatformsOnPage(int page){
        SP plugin = SP.getInstance();
        List<String> newDistributors = new ArrayList<>();
        int i = 0;
        for(SPPlatform distributor : SP.getInstance().platforms.values()){
            if (i >= (page - 1) * plugin.amountOnOnePage)
                newDistributors.add(distributor.getName());
            if (i >= page * plugin.amountOnOnePage - 1)
                break;
            i++;
        }
        return newDistributors;
    }
    private static boolean isPlatformOnPage(int page){
        return page - 1 >= 0 && SP.getInstance().platforms.values().size() - 1 >= (page - 1) * SP.getInstance().amountOnOnePage;
    }
}
