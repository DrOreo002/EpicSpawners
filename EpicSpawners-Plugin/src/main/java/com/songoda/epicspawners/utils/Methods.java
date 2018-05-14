package com.songoda.epicspawners.utils;

import com.songoda.arconix.plugin.Arconix;
import com.songoda.epicspawners.EpicSpawnersPlugin;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import org.bukkit.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
@SuppressWarnings("deprecation")
public class Methods {

    public static String formatName(EntityType type, int multi) {
        try {
            if (multi <= 0)
                multi = 1;
            return compileName(type.name(), multi, true);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static void takeItem(Player p, int amt) {
        try {
            if (p.getGameMode() != GameMode.CREATIVE) {
                int result = p.getInventory().getItemInHand().getAmount() - amt;
                if (result > 0) {
                    ItemStack is = p.getItemInHand();
                    is.setAmount(is.getAmount() - amt);
                    p.setItemInHand(is);
                } else {
                    p.setItemInHand(null);
                }
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public static boolean isOffhand(PlayerInteractEvent e) {
        try {
            EpicSpawnersPlugin plugin = EpicSpawnersPlugin.getInstance();
            if (!plugin.v1_8 && !plugin.v1_7) {
                if (e.getHand() == EquipmentSlot.OFF_HAND)
                    return true;
            }
        } catch (Exception ee) {
            Debugger.runReport(ee);
        }
        return false;
    }

    public static boolean isOffhand(BlockPlaceEvent e) {
        try {
            EpicSpawnersPlugin plugin = EpicSpawnersPlugin.getInstance();
            if (!plugin.v1_8 && !plugin.v1_7 && e.getHand() == EquipmentSlot.OFF_HAND)
                return true;
        } catch (Exception ee) {
            Debugger.runReport(ee);
        }
        return false;
    }

    public static String getBoostCost(int time, int amt) {
        try {
            EpicSpawnersPlugin plugin = EpicSpawnersPlugin.getInstance();

            String cost = "";

            String un = plugin.getConfig().getString("Spawner Boosting.Item Charged For A Boost");

            String[] parts = un.split(":");

            String type = parts[0];

            String multi = parts[1];

            int co = boostCost(multi, time, amt);
            if (!type.equals("ECO") && !type.equals("XP")) {
                cost += "&6&l" + co;
                cost += " &7" + type.substring(0, 1).toUpperCase() + type.toLowerCase().substring(1);
                if (co != 1)
                    cost += "s";
            } else if (type.equals("ECO")) {
                cost += "&6&l$" + Arconix.pl().getApi().format().formatEconomy(co);
            } else if (type.equals("XP")) {
                cost += "&6&l" + co;
                cost += " &7Levels";
            }

            return cost;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static int boostCost(String multi, int time, int amt) {
        try {
            return (int) Math.ceil((Double.parseDouble(multi) * time) * amt);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return 99999;
    }

    public static String compileName(String type, int multi, boolean full) {
        try {
            EpicSpawnersPlugin plugin = EpicSpawnersPlugin.getInstance();

            String nameFormat = plugin.getConfig().getString("Main.Spawner Name Format");
            String displayName = plugin.getSpawnerManager().getSpawnerData(type).getDisplayName();

            nameFormat = nameFormat.replace("{TYPE}", displayName);

            if ((multi > 1 || plugin.getConfig().getBoolean("Main.Display Level In Spawner Title If Level 1") || plugin.getConfig().getBoolean("Main.Named Spawners Tiers")) && multi >= 0) {
                if (plugin.getConfig().getBoolean("Main.Named Spawners Tiers") && plugin.getConfig().getStringList("Main.Tier Names").size() > multi) {
                    nameFormat = nameFormat.replace("{AMT}", plugin.getConfig().getStringList("Main.Tier Names").get(multi - 1));
                } else {
                    nameFormat = nameFormat.replace("{AMT}", Integer.toString(multi));
                }
                nameFormat = nameFormat.replace("[", "").replace("]", "");
            } else {
                nameFormat = nameFormat.replaceAll("\\[.*?]", "");
            }

            String info = "";
            if (full) {
                info += Arconix.pl().getApi().format().convertToInvisibleString(type.toUpperCase().replaceAll(" ", "_") + ":" + multi + ":");
            }

            return info + Arconix.pl().getApi().format().formatText(nameFormat).trim();
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static ItemStack getGlass() {
        try {
            EpicSpawnersPlugin plugin = EpicSpawnersPlugin.getInstance();
            return Arconix.pl().getApi().getGUI().getGlass(plugin.getConfig().getBoolean("Interfaces.Replace Glass Type 1 With Rainbow Glass"), plugin.getConfig().getInt("Interfaces.Glass Type 1"));
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static ItemStack getBackgroundGlass(boolean type) {
        try {
            EpicSpawnersPlugin plugin = EpicSpawnersPlugin.getInstance();
            if (type)
                return Arconix.pl().getApi().getGUI().getGlass(false, plugin.getConfig().getInt("Interfaces.Glass Type 2"));
            else
                return Arconix.pl().getApi().getGUI().getGlass(false, plugin.getConfig().getInt("Interfaces.Glass Type 3"));
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static String getTypeFromString(String typ) {
        try {
            if (typ == null)
                return null;
            String type = typ.replaceAll("_", " ");
            type = ChatColor.stripColor(type.substring(0, 1).toUpperCase() + type.toLowerCase().substring(1));
            return type;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static String restoreType(String typ) {
        try {
            String type = typ.replace(" ", "_");
            type = type.toUpperCase();
            return type;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }

    public static boolean isAir(Material type) {
        try {
            if (type == Material.AIR || type == Material.WOOD_PLATE
                    || type == Material.STONE_PLATE || type == Material.IRON_PLATE
                    || type == Material.GOLD_PLATE)
                return true;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    public static Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        if (!EpicSpawnersPlugin.getInstance().v1_7) return location.getWorld().getNearbyEntities(location, x, y, z);

        if (location == null) return Collections.emptyList();

        World world = location.getWorld();
        AxisAlignedBB aabb = AxisAlignedBB.a(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
        List<net.minecraft.server.v1_7_R4.Entity> entityList = ((CraftWorld) world).getHandle().getEntities(null, aabb, null);
        List<Entity> bukkitEntityList = new ArrayList<>();

        for (Object entity : entityList) {
            bukkitEntityList.add(((net.minecraft.server.v1_7_R4.Entity) entity).getBukkitEntity());
        }

        return bukkitEntityList;
    }

    public static int countEntitiesAroundLoation(Location location) {
        try {
            int amt = 0;

            String[] arr = EpicSpawnersPlugin.getInstance().getConfig().getString("Main.Radius To Search Around Spawners").split("x");
            Collection<Entity> nearbyEntite = getNearbyEntities(location.clone().add(0.5, 0.5, 0.5), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
            if (nearbyEntite.size() < 1) return amt;

            for (Entity ee : nearbyEntite) {
                if (!(ee instanceof LivingEntity) || ee instanceof Player || ee.getType().name().toLowerCase().contains("armor")) {
                    continue;
                }
                if (EpicSpawnersPlugin.getInstance().getServer().getPluginManager().getPlugin("StackMob") != null
                        && ee.getMetadata(uk.antiperson.stackmob.tools.extras.GlobalValues.METATAG).size() != 0) {
                    amt = amt + ee.getMetadata(uk.antiperson.stackmob.tools.extras.GlobalValues.METATAG).get(0).asInt();
                } else {
                    amt++;
                }
            }
            return amt;
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return 0;
    }

    public static int getIMulti(ItemStack item) {
        try {
            String arr[] = (item.getItemMeta().getDisplayName().replace("§", "")).split(":");
            return Integer.parseInt(arr[1]);
        } catch (Exception e) {
            return 1;
        }
    }

    /*

    public static String getType(ItemStack item) {
        if (getIType(item) != null && !getIType(item).equals(""))
            return Methods.restoreType(getIType(item));
        else
            return null;
    }*/

    public static String getIType(ItemStack item) {
        try {
            if (item == null) return null;
            try {
                if (item.getItemMeta().getDisplayName().contains(":")) {
                    String arr[] = (item.getItemMeta().getDisplayName().replace("§", "")).split(":");
                    return arr[0].toLowerCase().replace("_", " ");
                }
                for (final EntityType value : EntityType.values()) {
                    if (!value.isSpawnable() || !value.isAlive()) continue;
                    String str = item.getItemMeta().getDisplayName().toLowerCase();
                    str = str.replace("_", " ");

                    if (str.contains(value.name().toLowerCase()))
                        return value.toString().toLowerCase().replace("_", " ");

                }
                return null;
            } catch (Exception e) {
                BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
                CreatureSpawner bs = (CreatureSpawner) bsm.getBlockState();
                return bs.getSpawnedType().toString();
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return null;
    }
}