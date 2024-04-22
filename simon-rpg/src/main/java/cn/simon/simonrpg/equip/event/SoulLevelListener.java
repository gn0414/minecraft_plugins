package cn.simon.simonrpg.equip.event;

import cn.simon.simonrpg.SimonRpg;
import cn.simon.simonrpg.constants.ServerConst;
import cn.simon.simonrpg.equip.holder.SoulBindInventoryHolder;
import cn.simon.simonrpg.constants.ColorConst;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.WeaponType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static cn.simon.simonrpg.stat.event.StatListener.setStat;


public class SoulLevelListener implements Listener {

    public static ConcurrentHashMap<Player, Boolean> isViewingSoulBindInventory = new ConcurrentHashMap<>();

    public SoulLevelListener(){
        Logger logger = Logger.getLogger("soul-level");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }



    @EventHandler
    public void openInventory(InventoryOpenEvent event){
        //每次打开都清理指针物品
        event.getPlayer().setItemOnCursor(null);
    }

    /**
     * 非自己绑定无法拾取
     * @param event 物品拾取事件
     */
    @EventHandler
    public void PickUpHandler(EntityPickupItemEvent event){
        if (event.getEntity() instanceof Player player) {
            if (event.getItem().getItemStack().hasItemMeta()) {
                ItemMeta itemMeta = event.getItem().getItemStack().getItemMeta();

                assert itemMeta != null;
                if (itemMeta.getPersistentDataContainer().getKeys().contains(NameSpaceKeyBuilder.getEquipmentNameSpaceKey())) {
                    for (String lore : Objects.requireNonNull(itemMeta.getLore())) {
                        if (lore.contains(ColorConst.SOUL_BIND + "灵魂绑定 ")) {
                            String[] split = lore.split(" ");
                            if (!split[1].equals(ChatColor.GREEN + player.getName())) {
                                event.setCancelled(true);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 开启灵魂绑定
     */

    @EventHandler
    public void testOpenSoulBind(PlayerToggleSneakEvent event){
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Player player = event.getPlayer();
        if (item.hasItemMeta()) {
                boolean isSoulBind = soulBind(player, item);
                if (!isSoulBind) {
                    SoulBindInventoryHolder holder = new SoulBindInventoryHolder();

                    Inventory soulInventory = Bukkit.createInventory(holder, 27, "§4§l灵魂绑定");
                    holder.setInventory(soulInventory);

                    ItemStack yes = new ItemStack(Material.LIME_CONCRETE);
                    ItemStack no = new ItemStack(Material.RED_CONCRETE);

                    ItemMeta yesItemMeta = yes.getItemMeta();
                    ItemMeta noItemMeta = no.getItemMeta();

                    List<String> yesList = new ArrayList<>();
                    List<String> noList = new ArrayList<>();

                    yesList.add("§7物品将无法交易 无法上架.");
                    noList.add("§7取消物品灵魂绑定.");


                    yesItemMeta.setDisplayName("§a§l[接受]");
                    noItemMeta.setDisplayName("§4§l[取消]");

                    yesItemMeta.setLore(yesList);
                    noItemMeta.setLore(noList);

                    yes.setItemMeta(yesItemMeta);
                    no.setItemMeta(noItemMeta);

                    soulInventory.setItem(9, yes);
                    soulInventory.setItem(13, item); // 这里保持原来的item
                    soulInventory.setItem(17, no);

                    //放入遮光玻璃
                    putBlackGlass(soulInventory);
                    player.openInventory(soulInventory); // 打开 inventory
                    event.setCancelled(true);
                }
            }
    }

    /**
     * 开启灵魂绑定
     * @param event 自定义背包点击事件
     */
    @EventHandler
    public void openSoulBind(InventoryClickEvent event){
        if (event.getAction().equals(InventoryAction.PICKUP_ONE) || event.getAction().equals(InventoryAction.PICKUP_SOME)
        || event.getAction().equals(InventoryAction.PICKUP_ALL) || event.getAction().equals(InventoryAction.PICKUP_HALF)
        ) {
                if (event.getClick().isRightClick()) {
                    if (event.getCurrentItem() != null) {
                        ItemStack item = event.getCurrentItem();
                        if (item.hasItemMeta()) {
                            if (event.getWhoClicked() instanceof Player player) {
                                boolean isSoulBind = soulBind(player, item);
                                if (!isSoulBind) {
                                    SoulBindInventoryHolder holder = new SoulBindInventoryHolder();

                                    Inventory soulInventory = Bukkit.createInventory(holder, 27, "§4§l灵魂绑定");
                                    holder.setInventory(soulInventory);

                                    ItemStack yes = new ItemStack(Material.LIME_CONCRETE);
                                    ItemStack no = new ItemStack(Material.RED_CONCRETE);

                                    ItemMeta yesItemMeta = yes.getItemMeta();
                                    ItemMeta noItemMeta = no.getItemMeta();

                                    List<String> yesList = new ArrayList<>();
                                    List<String> noList = new ArrayList<>();

                                    yesList.add("§7物品将无法交易 无法上架.");
                                    noList.add("§7取消物品灵魂绑定.");


                                    yesItemMeta.setDisplayName("§a§l[接受]");
                                    noItemMeta.setDisplayName("§4§l[取消]");

                                    yesItemMeta.setLore(yesList);
                                    noItemMeta.setLore(noList);

                                    yes.setItemMeta(yesItemMeta);
                                    no.setItemMeta(noItemMeta);

                                    soulInventory.setItem(9, yes);
                                    soulInventory.setItem(13, item); // 这里保持原来的item
                                    soulInventory.setItem(17, no);

                                    //放入遮光玻璃
                                    putBlackGlass(soulInventory);
                                    player.openInventory(soulInventory); // 打开 inventory
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }

        }
    }
    /**
     * 灵魂背包打开
     */

    @EventHandler
    public void soulBindInventoryOpen(InventoryOpenEvent event){
        if (event.getInventory().getHolder() instanceof SoulBindInventoryHolder){
            if (event.getPlayer() instanceof  Player player){
                isViewingSoulBindInventory.put(player, true);
            }
        }
    }

    @EventHandler
    public void soulBindInventoryClose(InventoryCloseEvent event){
        if (event.getInventory().getHolder() instanceof SoulBindInventoryHolder){
            if (event.getPlayer() instanceof  Player player){
                isViewingSoulBindInventory.remove(player);
            }
        }
    }


    /**
     * 灵魂绑定具体逻辑
     * @param event 背包点击事件
     */
    @EventHandler
    public void soulBindInventory(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof  Player player){
            if (isViewingSoulBindInventory.containsKey(player)){
                if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof SoulBindInventoryHolder) {
                    if (event.getSlot() == 9){
                            int itemSlot = findItemSlot(event.getInventory().getItem(13), player);
                            if (itemSlot != -1){
                                ItemStack item = doSoulBind(player, Objects.requireNonNull(event.getInventory().getItem(13)));
                                player.getInventory().remove(Objects.requireNonNull(event.getInventory().getItem(13)));
                                player.getInventory().setItem(itemSlot,item);
                                player.sendMessage(soulBindSuccessMessage());
                                player.closeInventory();
                                return;
                            }
                    }

                    if (event.getSlot() == 17){
                        player.sendMessage(soulBindCancelMessage());
                        player.closeInventory();
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    /**
     * 若主手物品改变则触发stat修改
     * @param event 主手物品改变
     */
    @EventHandler
    public void mainHandChange(PlayerItemHeldEvent event){
            Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                setStat(event.getPlayer(), event.getPlayer().getInventory());
            });
    }


    /**
     *
     * @param event 使用装备事件
     */
    @EventHandler
    public void useEquipmentHandler(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (!soulBind(player,player.getInventory().getItemInMainHand())){
            player.sendMessage(soulBindMessage());
            player.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_AMBIENT,1.0f,1.0f);
            event.setCancelled(true);
            return;
        }

        if (!checkLevel(player,player.getInventory().getItemInMainHand())){
            player.sendMessage(levelMessage());
            player.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_AMBIENT,1.0f,1.0f);
            event.setCancelled(true);
            return;
        }
        ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
        if (itemMeta == null)return;
        Weapon weapon = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
        if (weapon == null)return;
        if (weapon.getDamageType() != null)return;
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
            setStat(player,player.getInventory());
        });
    }

    /**
     *
     * @param event 拖拽事件
     */

    @EventHandler
    public void dragStatHandler(InventoryDragEvent event){
        Player player = JavaPlugin.getPlugin(SimonRpg.class).getServer().getPlayer(event.getWhoClicked().getName());

        if (!soulBind(player, event.getOldCursor())) {
            assert player != null;
            player.sendMessage(soulBindMessage());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            event.setCancelled(true);
            return;
        }
        if (!checkLevel(player, event.getOldCursor())) {
            assert player != null;
            player.sendMessage(levelMessage());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
            assert player != null;
            setStat(player, player.getInventory());
        });

    }

    /**
     * @param event 交换装备事件
     */
    @EventHandler
    public void changeStatHandler(InventoryClickEvent event) {
        Player player = JavaPlugin.getPlugin(SimonRpg.class).getServer().getPlayer(event.getWhoClicked().getName());
        if (event.getSlot() >= 36 && event.getSlot() <=40) {
            if (event.getAction().equals(InventoryAction.PLACE_ALL) || event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)
                    || event.getAction().equals(InventoryAction.PLACE_ONE) || event.getAction().equals(InventoryAction.PLACE_SOME)
            ){
                if (event.getCursor() != null){
                    if (!soulBind(player, event.getCursor() )) {
                        assert player != null;
                        player.sendMessage(soulBindMessage());
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        event.setCancelled(true);
                        return;
                    }
                    if (!checkLevel(player, event.getCursor())) {
                        assert player != null;
                        player.sendMessage(levelMessage());
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        event.setCancelled(true);
                        return;
                    }
                }
                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                    assert player != null;
                    setStat(player, player.getInventory());
                });
            } else if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                    assert player != null;
                    setStat(player, player.getInventory());
                });
            }else if (event.getAction().equals(InventoryAction.PICKUP_ALL)){
                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                    assert player != null;
                    setStat(player, player.getInventory());
                });
            }
        }else{
            if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                if (!soulBind(player, Objects.requireNonNull(event.getCurrentItem()))) {
                    assert player != null;
                    player.sendMessage(soulBindMessage());
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    event.setCancelled(true);
                    return;
                }
                if (!checkLevel(player, event.getCurrentItem())) {
                    assert player != null;
                    player.sendMessage(levelMessage());
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    event.setCancelled(true);
                    return;
                }
                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                    assert player != null;
                    setStat(player, player.getInventory());
                });
            }
        }
    }


    /**
     * 判断状态是否灵魂绑定
     * @param player 玩家
     * @param item 物品
     * @return 是否灵魂绑定
     */
    public static boolean soulBind(Player player, ItemStack item){
        if (item.hasItemMeta()) {
            if (Objects.requireNonNull(item.getItemMeta()).getLore() != null) {
                for (String lore : item.getItemMeta().getLore()) {
                    if (lore.contains(ChatColor.RED + "需求绑定后使用!")) {
                        return false;
                    }
                    if (lore.contains(ChatColor.DARK_GREEN + "灵魂绑定")) {
                        String[] split = lore.split(" ");
                        if (!split[1].equals(ChatColor.GREEN + player.getName())) return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 玩家等级是否符合条件
     * @param player 玩家
     * @param item 物品
     * @return 等级是否符合
     */
    public static boolean checkLevel(Player player,ItemStack item){
        if (item.hasItemMeta()) {
            if (item.getItemMeta().getLore() != null) {
                for (String lore : item.getItemMeta().getLore()) {
                    if (lore.contains(ChatColor.RED + "需求等级")) {
                        String[] split = lore.split(" ");
                        if (player.getLevel() < Integer.parseInt(split[1])){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private static int findItemSlot(ItemStack item,Player player){
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (item.isSimilar(inventory.getItem(i)))return i;
        }
        return -1;
    }

    public static String soulBindMessage(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GRAY+"想使用这个物品必须先进行 "+ChatColor.GOLD+"灵魂绑定.\n" +
                ChatColor.GRAY+"可以在背包中右键该物品进行绑定";
    }

    public static String levelMessage(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GRAY+"等级不足,快去试炼提升经验!";
    }

    private static void putBlackGlass(Inventory inventory){
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i,new ItemStack(Material.TINTED_GLASS));
            }
        }
    }

    private static String soulBindSuccessMessage(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ ChatColor.GREEN+"灵魂绑定成功";
    }

    private static String soulBindCancelMessage(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ ChatColor.DARK_RED+"取消灵魂绑定";
    }

    /**
     *
     * @param player 玩家
     * @param item 灵魂绑定物品
     * @return 绑定成功的物品
     */
    private static ItemStack doSoulBind(Player player,ItemStack item){
        if (item.hasItemMeta()) {
            Weapon weapon = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
            if (weapon != null) {
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = itemMeta.getLore();
                for (int i = 0; i < Objects.requireNonNull(lore).size(); i++) {
                    String line = lore.get(i);
                    if (line.contains(ChatColor.RED + "需求绑定后使用!")) {
                        lore.set(i, ColorConst.SOUL_BIND + "灵魂绑定 " + ColorConst.SOUL_NAME + player.getName());
                        weapon.setSoulBind(player.getName());
                        itemMeta.getPersistentDataContainer().remove(NameSpaceKeyBuilder.getEquipmentNameSpaceKey());
                        itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType(), weapon);
                        itemMeta.setLore(lore);
                        item.setItemMeta(itemMeta);
                        break;
                    }
                }
            }
        }
        return item;
    }
}
