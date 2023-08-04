package fr.senseitarzan;

import cn.nukkit.plugin.PluginBase;
import fr.senseitarzan.events.EventListener;

public class FormAPI extends PluginBase {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

}