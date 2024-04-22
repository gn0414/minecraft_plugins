package cn.simon.simonrpg.nbt.data;


import cn.simon.simonrpg.jewel.entity.Jewel;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class JewelType implements PersistentDataType<String, Jewel> {



    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }


    @Override
    public Class<Jewel> getComplexType() {
        return Jewel.class;
    }


    @Override
    public String toPrimitive( Jewel jewel, PersistentDataAdapterContext persistentDataAdapterContext) {
        if (jewel == null)return "{}";
        return JSONObject.toJSONString(jewel);
    }


    @Override
    public Jewel fromPrimitive(String string,PersistentDataAdapterContext persistentDataAdapterContext) {
        return JSONObject.parseObject(string, Jewel.class);
    }
}
