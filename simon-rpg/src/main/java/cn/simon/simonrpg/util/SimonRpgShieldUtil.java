package cn.simon.simonrpg.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Random;

public class SimonRpgShieldUtil {
    public static ItemStack createRandomPatternBanner() {
        Random random = new Random();
        ItemStack shieldItem = new ItemStack(Material.SHIELD);
        BlockStateMeta meta = (BlockStateMeta) shieldItem.getItemMeta();
        Banner banner = (Banner) meta.getBlockState();
        // 创建一个图案
        int color =  random.nextInt(0,DyeColor.values().length);
        int type = random.nextInt(0,PatternType.values().length);
        DyeColor patternColor = null;
        PatternType patternType = null;
        for (DyeColor value : DyeColor.values()) {
            if (color == 0){
                patternColor = value;
                break;
            }
            color--;
        }
        for (PatternType value : PatternType.values()) {
            if (type == 0){
                patternType = value;
                break;
            }
            type--;
        }
        if (patternColor == null){
            patternColor = DyeColor.BLACK;
        }
        if (patternType == null){
            patternType = PatternType.BASE;
        }
        Pattern pattern = new Pattern(patternColor,patternType);
        // 将图案添加到盾牌
        banner.addPattern(pattern);
        // 将盾牌的 BlockStateMeta 更新
        meta.setBlockState(banner);
        shieldItem.setItemMeta(meta);
        return shieldItem;
    }


    private static PatternType getRandomPatternType() {
        PatternType[] patternTypes = PatternType.values();
        Random random = new Random();
        return patternTypes[random.nextInt(patternTypes.length)];
    }

    private static DyeColor getRandomColor() {
        DyeColor[] colors = DyeColor.values();
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }



}