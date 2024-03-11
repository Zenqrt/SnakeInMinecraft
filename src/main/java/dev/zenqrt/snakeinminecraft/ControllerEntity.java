package dev.zenqrt.snakeinminecraft;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class ControllerEntity extends ArmorStand {

    private final SnakeGame snakeGame;

    public ControllerEntity(Level world, SnakeGame snakeGame) {
        super(EntityType.ARMOR_STAND, world);

        this.snakeGame = snakeGame;
        System.out.println("Yes this works");
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.hasPassenger(entity -> entity instanceof Player)) {
            Player player = (Player) this.getPassengers().get(0);

            handleMovementDetection(player);
        }
    }

    private void handleMovementDetection(Player player) {
        float xDirection = player.xxa;
        float zDirection = player.zza;

        if (xDirection > 0) {
            snakeGame.snake().turn(Direction.LEFT);
        } else if (xDirection < 0) {
            snakeGame.snake().turn(Direction.RIGHT);
        } else if (zDirection > 0) {
            snakeGame.snake().turn(Direction.UP);
        } else if (zDirection < 0) {
            snakeGame.snake().turn(Direction.DOWN);
        }
    }
}
