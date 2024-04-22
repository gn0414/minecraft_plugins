package cn.simon.simonrpg;

import cn.simon.simonrpg.attack.event.CommonAttackListener;
import cn.simon.simonrpg.attack.event.FishAttackListener;
import cn.simon.simonrpg.attack.event.RangeAttackListener;
import cn.simon.simonrpg.attack.event.RemoveListener;
import cn.simon.simonrpg.bottle.builder.BottleBuilder;
import cn.simon.simonrpg.bottle.entity.Bottle;
import cn.simon.simonrpg.bottle.event.OpenBottleListener;
import cn.simon.simonrpg.bottle.executor.CustomBottleExecutor;
import cn.simon.simonrpg.bottle.holder.BottleHolder;
import cn.simon.simonrpg.bottle.reader.BottleReader;
import cn.simon.simonrpg.equip.builder.EquipmentBuilder;
import cn.simon.simonrpg.equip.event.SoulLevelListener;
import cn.simon.simonrpg.equip.executor.CustomEquipExecutor;
import cn.simon.simonrpg.equip.executor.EquipmentRecoveryExecutor;
import cn.simon.simonrpg.equip.holder.EquipmentHolder;
import cn.simon.simonrpg.equip.reader.EquipmentReader;
import cn.simon.simonrpg.equip.reader.RandomEquipmentDetailReader;
import cn.simon.simonrpg.equip.reader.WeaponReader;
import cn.simon.simonrpg.equip.reader.WeaponSouvenirReader;
import cn.simon.simonrpg.jewel.executor.CustomJewelExecutor;
import cn.simon.simonrpg.jewel.holder.JewelHolder;
import cn.simon.simonrpg.jewel.reader.JewelReader;
import cn.simon.simonrpg.mob.event.MobListener;
import cn.simon.simonrpg.mob.executor.BossFallExecutor;
import cn.simon.simonrpg.mob.holder.MobHolder;
import cn.simon.simonrpg.mob.reader.MobItemReader;
import cn.simon.simonrpg.mob.reader.MobReader;
import cn.simon.simonrpg.mob.reader.SkillReader;
import cn.simon.simonrpg.reload.executor.ReloadExecutor;
import cn.simon.simonrpg.stat.event.StatListener;
import cn.simon.simonrpg.stat.holder.StatHolder;
import cn.simon.simonrpg.strength.event.StrengthenListener;
import cn.simon.simonrpg.task.holder.TaskHolder;
import cn.simon.simonrpg.task.reader.TaskItemReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class SimonRpg extends JavaPlugin {

    Logger logger = Logger.getLogger("SimonRpg");

    @Override
    public void onEnable() {
        logger.info("配置文件读取中==========================>");
        EquipmentHolder.equipments = EquipmentBuilder.makeEquipments(EquipmentReader.ReadConfig(getDataFolder()+ File.separator+ "item"+ File.separator+"equipment"));
        EquipmentHolder.weapons = EquipmentBuilder.makeWeapons(WeaponReader.ReadConfig(getDataFolder() + File.separator+ "item"+ File.separator+"weapon"));
        EquipmentHolder.souvenirs = EquipmentBuilder.makeWeaponSouvenirs(WeaponSouvenirReader.ReadConfig(getDataFolder()+ File.separator+"item" + File.separator+"souvenir"));
        EquipmentHolder.randomEquipments = RandomEquipmentDetailReader.ReadConfig(getDataFolder()+ File.separator+"item" + File.separator+"random");
        BottleHolder.bottles = BottleBuilder.makeBottles(BottleReader.ReadConfig(getDataFolder() + File.separator+ "item"+ File.separator+"bottle"));
        StatHolder.mobStat = MobReader.ReadConfig(getDataFolder() + File.separator+ "mob");
        MobHolder.mobItemHolder = MobItemReader.ReadConfig(getDataFolder() + File.separator+ "mob-item");
        MobHolder.mobSkills = SkillReader.ReadConfig(getDataFolder()+File.separator+"skill");
        TaskHolder.tasks = TaskItemReader.ReadConfig(getDataFolder() + File.separator+ "item"+ File.separator+"task");
        JewelHolder.jewels = JewelReader.ReadConfig(getDataFolder()+ File.separator+ "item"+ File.separator+"jewel");
        logger.info("事件注册中=============================>");
        getServer().getPluginManager().registerEvents(new OpenBottleListener(), this);
        getServer().getPluginManager().registerEvents(new StatListener(),this);
        getServer().getPluginManager().registerEvents(new SoulLevelListener(),this);
        getServer().getPluginManager().registerEvents(new CommonAttackListener(),this);
        getServer().getPluginManager().registerEvents(new RangeAttackListener(),this);
        getServer().getPluginManager().registerEvents(new FishAttackListener(),this);
        getServer().getPluginManager().registerEvents(new MobListener(),this);
        getServer().getPluginManager().registerEvents(new StrengthenListener(),this);
        getServer().getPluginManager().registerEvents(new RemoveListener(),this);
        logger.info("指令注册中=============================>");
        Objects.requireNonNull(this.getCommand("custom-equip")).setExecutor(new CustomEquipExecutor());
        Objects.requireNonNull(this.getCommand("custom-bottle")).setExecutor(new CustomBottleExecutor());
        Objects.requireNonNull(this.getCommand("custom-jewel")).setExecutor(new CustomJewelExecutor());
        Objects.requireNonNull(this.getCommand("recovery")).setExecutor(new EquipmentRecoveryExecutor());
        Objects.requireNonNull(this.getCommand("bossfall")).setExecutor(new BossFallExecutor());
        Objects.requireNonNull(this.getCommand("simonrpg-reload")).setExecutor(getReloadExecutor());
        outLogo();
    }

    @Override
    public void onDisable() {

    }

    private ReloadExecutor getReloadExecutor(){
        ReloadExecutor reloadExecutor = new ReloadExecutor();
        reloadExecutor.setPath(getDataFolder());
        return  reloadExecutor;
    }

    private void outLogo(){
        logger.info(" \n" +
                "   _____ _____ __  __  ____  _   _        _____  _____   _____ \n" +
                "  / ____|_   _|  \\/  |/ __ \\| \\ | |      |  __ \\|  __ \\ / ____|\n" +
                " | (___   | | | \\  / | |  | |  \\| |______| |__) | |__) | |  __ \n" +
                "  \\___ \\  | | | |\\/| | |  | | . ` |______|  _  /|  ___/| | |_ |\n" +
                "  ____) |_| |_| |  | | |__| | |\\  |      | | \\ \\| |    | |__| |\n" +
                " |_____/|_____|_|  |_|\\____/|_| \\_|      |_|  \\_\\_|     \\_____|\n" +
                "                                                               \n" +
                "                                                               ");
    }
}
