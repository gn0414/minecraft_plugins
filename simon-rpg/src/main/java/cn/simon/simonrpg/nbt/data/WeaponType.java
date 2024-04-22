package cn.simon.simonrpg.nbt.data;

import cn.simon.simonrpg.equip.entity.Weapon;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class WeaponType implements PersistentDataType<String, Weapon> {
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<Weapon> getComplexType() {
        return Weapon.class;
    }

    @Override
    public String toPrimitive(Weapon weapon, PersistentDataAdapterContext persistentDataAdapterContext) {
        //判断weapon是否为空
        if (weapon == null)return "{}";
        return JSONObject.toJSONString(weapon);
    }

    @Override
    public Weapon fromPrimitive( String s, PersistentDataAdapterContext persistentDataAdapterContext) {

        return JSONObject.parseObject(s,Weapon.class);
    }
}
