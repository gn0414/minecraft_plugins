package cn.simon.simonrpg.attack.event;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.listener.PvpListener;
import io.lumine.mythic.lib.listener.SkillTriggers;
import io.lumine.mythic.lib.listener.event.AttackEventListener;
import io.lumine.mythic.lib.manager.DamageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class RemoveListener implements Listener {

    private boolean executed = false;
    Logger logger = Logger.getLogger("remove");
    public RemoveListener(){

        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }

    /**
     * 取消的事件listener
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelOtherDamage(EntityDamageByEntityEvent event) {
        if (!executed) {
            // 执行你想要做的操作
            HandlerList handlerList = event.getHandlers();
            RegisteredListener[] registeredListeners = handlerList.getRegisteredListeners();
            // 循环遍历已注册的监听器
            for (RegisteredListener registeredListener : registeredListeners) {
                if (registeredListener.getListener().getClass() == SkillTriggers.class ||
                        registeredListener.getListener().getClass() == PvpListener.class
                        || registeredListener.getListener().getClass() == AttackEventListener.class
                        || registeredListener.getListener().getClass() == DamageManager.class
                ){
                    // 取消的监听器
                    handlerList.unregister(registeredListener);
                    logger.info("remove listener: "+registeredListener.getListener().getClass().getName());
                }
            }
            executed = true; // 将标记设置为 true，表示已经执行过了
        }
    }
}
