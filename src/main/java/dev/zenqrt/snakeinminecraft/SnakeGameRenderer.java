package dev.zenqrt.snakeinminecraft;

import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class SnakeGameRenderer extends MapRenderer {

    private static final int MAP_SIZE = 128;

    private final SnakeGame snakeGame;
    private final int spaceSize;

    public SnakeGameRenderer(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        this.spaceSize = MAP_SIZE / snakeGame.size();
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        setBackground(mapCanvas, Color.BLACK);
        drawSnake(mapCanvas);
        drawFood(mapCanvas);

        mapCanvas.drawText(0, 0, MinecraftFont.Font, "Score: " + snakeGame.score);

        if (!snakeGame.gameActive)
            mapCanvas.drawText(snakeGame.size() / 3, snakeGame.size() / 2, MinecraftFont.Font, "Game Over");
    }

    private void drawSnake(MapCanvas mapCanvas) {
        for (SnakeGame.Position position : snakeGame.snake().positions()) {
            drawPixel(mapCanvas, position.x(), position.y(), Color.GREEN);
        }
    }

    private void drawFood(MapCanvas mapCanvas) {
        drawPixel(mapCanvas, snakeGame.food().position().x(), snakeGame.food().position().y(), Color.RED);
    }

    private void drawPixel(MapCanvas mapCanvas, int pixelX, int pixelY, Color color) {
        if (spaceSize == 1)
            mapCanvas.setPixelColor(pixelX, pixelY, color);
        else
            for (int x = 0; x < spaceSize; x++) {
                for (int y = 0; y < spaceSize; y++) {
                    mapCanvas.setPixelColor(pixelX * spaceSize + x, pixelY * spaceSize + y, color);
                }
            }
    }

    private static void setBackground(MapCanvas mapCanvas, Color color) {
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                mapCanvas.setPixelColor(x, y, color);
            }
        }
    }
}
