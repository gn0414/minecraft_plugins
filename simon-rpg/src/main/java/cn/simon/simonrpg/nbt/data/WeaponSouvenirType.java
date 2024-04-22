package cn.simon.simonrpg.nbt.data;

import cn.simon.simonrpg.equip.entity.WeaponSouvenir;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class WeaponSouvenirType implements PersistentDataType<String, WeaponSouvenir> {
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<WeaponSouvenir> getComplexType() {
        return WeaponSouvenir.class;
    }

    @Override
    public String toPrimitive(WeaponSouvenir weaponSouvenir, PersistentDataAdapterContext persistentDataAdapterContext) {
        if (weaponSouvenir == null)return "{}";
        return JSONObject.toJSONString(weaponSouvenir);
    }

    @Override
    public WeaponSouvenir fromPrimitive(String s, PersistentDataAdapterContext persistentDataAdapterContext) {
        return JSONObject.parseObject(s, WeaponSouvenir.class);
    }
}
