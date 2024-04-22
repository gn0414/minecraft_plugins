package cn.simon.simonrpg.equip.holder;

import cn.simon.simonrpg.equip.entity.RandomEquipmentDetail;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EquipmentHolder {

    public static List<ItemStack> weapons;
    public static List<ItemStack> equipments;
    public static List<ItemStack> souvenirs;

    public static ConcurrentHashMap<String, RandomEquipmentDetail> randomEquipments;
}
