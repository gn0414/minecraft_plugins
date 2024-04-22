package cn.simon.simonrpg.nbt.data;


import cn.simon.simonrpg.bottle.entity.Bottle;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class BottleType implements PersistentDataType<String, Bottle> {
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<Bottle> getComplexType() {
        return Bottle.class;
    }

    @Override
    public String toPrimitive(Bottle bottle, PersistentDataAdapterContext persistentDataAdapterContext) {

        if (bottle == null)return "{}";

        return JSONObject.toJSONString(bottle);
    }

    @Override
    public Bottle fromPrimitive(String s, PersistentDataAdapterContext persistentDataAdapterContext) {
        return JSONObject.parseObject(s,Bottle.class);
    }
}
