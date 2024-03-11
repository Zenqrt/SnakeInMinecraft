package dev.zenqrt.snakeinminecraft;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class SnakeInMinecraft extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommand("snake", (player, args) -> {
            int size = Integer.parseInt(args[0]);

            if (size % 16 != 0) {
                player.sendMessage("Size must be a multiple of 16");
                return;
            }

            SnakeGame snakeGame = new SnakeGame(size);

            spawnControllerEntity(player, snakeGame);

            ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
            MapView mapView = Bukkit.createMap(player.getWorld());
            mapView.getRenderers().forEach(mapView::removeRenderer);
            mapView.addRenderer(new SnakeGameRenderer(snakeGame));
            itemStack.editMeta(MapMeta.class, meta -> meta.setMapView(mapView));

            player.getInventory().setItemInMainHand(itemStack);

            new SnakeGameTask(player, snakeGame, !Arrays.asList(args).contains("--no-crash")).runTaskTimer(this, 0, 5);
        });
    }

    private void spawnControllerEntity(Player player, SnakeGame snakeGame) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        Level level = serverPlayer.level();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        player.teleport(new Location(player.getWorld(), x, y + 1, z));
        ControllerEntity controllerEntity = new ControllerEntity(level, snakeGame);
        controllerEntity.setPos(new Vec3(x, y + 1, z));
        level.addFreshEntity(controllerEntity);
        serverPlayer.startRiding(controllerEntity);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommand(String command, BiConsumer<Player, String[]> executor) {
        Objects.requireNonNull(getCommand(command)).setExecutor((sender, cmd, label, args) -> {
            executor.accept((Player) sender, args);
            return true;
        });
    }

}
