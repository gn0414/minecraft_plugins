package cn.simon.simonrpg.nbt.data;


import cn.simon.simonrpg.equip.entity.Equipment;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class EquipmentType implements PersistentDataType<String, Equipment> {
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<Equipment> getComplexType() {
        return Equipment.class;
    }

    @Override
    public String toPrimitive(Equipment equipment, PersistentDataAdapterContext persistentDataAdapterContext) {
        //判断weapon是否为空
        if (equipment == null)return "{}";
        return JSONObject.toJSONString(equipment);
    }

    @Override
    public Equipment fromPrimitive( String s, PersistentDataAdapterContext persistentDataAdapterContext) {

        return JSONObject.parseObject(s, Equipment.class);
    }
}
