package dev.zenqrt.snakeinminecraft;

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public final class SnakeGameTask extends BukkitRunnable {

    private final Player player;
    private final SnakeGame snakeGame;
    private final boolean shouldCrashPlayer;

    public SnakeGameTask(Player player, SnakeGame snakeGame, boolean shouldCrashPlayer) {
        this.player = player;
        this.snakeGame = snakeGame;
        this.shouldCrashPlayer = shouldCrashPlayer;
    }

    @Override
    public void run() {
        if (snakeGame.gameActive) {
            snakeGame.tick();
        } else {
            snakeGame.tick();
            this.cancel();

            if (shouldCrashPlayer) {
                crashPlayer();
            }
        }
    }

    private void crashPlayer() {
        ClientboundPlayerPositionPacket packet = new ClientboundPlayerPositionPacket(
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Float.MAX_VALUE,
                Float.MAX_VALUE,
                Set.of(),
                Integer.MAX_VALUE
        );
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }
}
