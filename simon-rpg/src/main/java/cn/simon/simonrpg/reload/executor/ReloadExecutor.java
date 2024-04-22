package cn.simon.simonrpg.reload.executor;

import cn.simon.simonrpg.bottle.builder.BottleBuilder;
import cn.simon.simonrpg.bottle.holder.BottleHolder;
import cn.simon.simonrpg.bottle.reader.BottleReader;
import cn.simon.simonrpg.constants.ServerConst;
import cn.simon.simonrpg.equip.builder.EquipmentBuilder;
import cn.simon.simonrpg.equip.holder.EquipmentHolder;
import cn.simon.simonrpg.equip.reader.EquipmentReader;
import cn.simon.simonrpg.equip.reader.RandomEquipmentDetailReader;
import cn.simon.simonrpg.equip.reader.WeaponReader;
import cn.simon.simonrpg.equip.reader.WeaponSouvenirReader;
import cn.simon.simonrpg.jewel.holder.JewelHolder;
import cn.simon.simonrpg.jewel.reader.JewelReader;
import cn.simon.simonrpg.mob.holder.MobHolder;
import cn.simon.simonrpg.mob.reader.MobItemReader;
import cn.simon.simonrpg.mob.reader.MobReader;
import cn.simon.simonrpg.stat.holder.StatHolder;
import cn.simon.simonrpg.task.holder.TaskHolder;
import cn.simon.simonrpg.task.reader.TaskItemReader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class ReloadExecutor implements CommandExecutor {

    private  File path;


    public void setPath(File path) {
        this.path = path;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equals("simonrpg-reload")){
            EquipmentHolder.equipments = EquipmentBuilder.makeEquipments(EquipmentReader.ReadConfig(path+ File.separator+ "item"+ File.separator+"equipment"));
            EquipmentHolder.weapons = EquipmentBuilder.makeWeapons(WeaponReader.ReadConfig(path + File.separator+ "item"+ File.separator+"weapon"));
            EquipmentHolder.souvenirs = EquipmentBuilder.makeWeaponSouvenirs(WeaponSouvenirReader.ReadConfig(path+ File.separator+"item" + File.separator+"souvenir"));
            EquipmentHolder.randomEquipments = RandomEquipmentDetailReader.ReadConfig(path+ File.separator+"item" + File.separator+"random");
            BottleHolder.bottles = BottleBuilder.makeBottles(BottleReader.ReadConfig(path + File.separator+ "item"+ File.separator+"bottle"));
            StatHolder.mobStat = MobReader.ReadConfig(path + File.separator+ "mob");
            MobHolder.mobItemHolder = MobItemReader.ReadConfig(path + File.separator+ "mob-item");
            TaskHolder.tasks = TaskItemReader.ReadConfig(path + File.separator+ "item"+ File.separator+"task");
            JewelHolder.jewels = JewelReader.ReadConfig(path+ File.separator+ "item"+ File.separator+"jewel");
            commandSender.sendMessage(ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GRAY+"配置文件重载完毕");
            return true;
        }
        return false;
    }
}
