package me.vovari2.stageplatforms;

import me.vovari2.stageplatforms.objects.SPDelayPaste;
import me.vovari2.stageplatforms.utils.TextUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SPTaskSeconds extends BukkitRunnable {

    public int seconds;
    public final HashMap<String, SPDelayPaste> waitDelayPaste;

    private final SP plugin;
    public SPTaskSeconds(SP plugin) {
        this.plugin = plugin;
        seconds = 0;
        waitDelayPaste = new HashMap<>();
    }

    @Override
    public void run() {
        // Работа с waitDelayPaste
        for (Map.Entry<String, SPDelayPaste> entry : waitDelayPaste.entrySet()){
            SPDelayPaste delay = entry.getValue();
            int timeTo = getSecondsToTime(delay.getSecond());
            switch(timeTo){
                case 5: case 4: case 3: case 2: case 1: {
                    delay.getPlatform().sendMessageNearPlayers(
                            delay.getStageName().equals("*") ?
                                    SPText.replacePlaceHolder("message.delay_paste", "%second%", String.valueOf(timeTo)) :
                                    SPText.replacePlaceHolder("message.delay_paste_stage", new String[]{"%stage_name%", "%second%"}, new String[]{delay.getStageName(),String.valueOf(timeTo)}));
                } continue;
                default:
                    if (timeTo > plugin.timer || timeTo == 0){
                        delay.getPlatform().clear();
                        try { delay.getSchematic().paste(delay.getPlatform().getCenter()); }
                        catch (Exception error) {
                            TextUtils.sendWarningMessage(error.getMessage());}
                        delay.getPlatform().sendMessageNearPlayers(
                                delay.getStageName().equals("*") ?
                                        SPText.replacePlaceHolder("message.paste", "%name%", delay.getPlatform().getName()) :
                                        SPText.replacePlaceHolder("message.paste_stage", new String[]{"%stage_name%", "%name%"}, new String[]{delay.getStageName(), delay.getPlatform().getName()}));
                        waitDelayPaste.remove(entry.getKey());
                    }
            }
        }

        // Добавление секунд
        seconds++;
        if (seconds > 3599)
            seconds = 0;
    }

    public int getSecondsAfterPeriod(int period){
        int time = seconds + period;
        return time > 3599 ? time - 3600 : time;
    } // Число, в какую секунду нужно вызвать событие из промежутка 0 - 71999
    public int getSecondsToTime(int time){
        return time < seconds ? 3600 - seconds + time : time - seconds;
    } // Количество секунд, оставшееся до вызова события
}
