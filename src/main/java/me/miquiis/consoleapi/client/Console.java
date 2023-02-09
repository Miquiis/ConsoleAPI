package me.miquiis.consoleapi.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.miquiis.consoleapi.ConsoleAPI;
import me.miquiis.consoleapi.common.events.ExecuteConsoleCommandEvent;
import me.miquiis.consoleapi.server.network.ModNetwork;
import me.miquiis.consoleapi.server.network.messages.ExecuteConsoleCommandPacket;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Console {

    private final Minecraft minecraft;

    private TextFieldWidget consoleInputField;
    private String consoleOutput;

    private final List<String> previousCommands;
    private boolean isConsoleOpen;
    private int consoleTicks;
    private int currentPreviousCommandIndex;
    private int consoleOutputTick;

    public Console()
    {
        minecraft = Minecraft.getInstance();
        previousCommands = new ArrayList<>();
    }

    public void init()
    {
        consoleInputField = new TextFieldWidget(minecraft.fontRenderer, 15, 3, minecraft.getMainWindow().getScaledWidth(), 8, new StringTextComponent("Command here"));
        consoleInputField.setMaxStringLength(256);
        consoleInputField.setEnableBackgroundDrawing(false);
        consoleInputField.setVisible(true);
        consoleInputField.setTextColor(16777215);
        consoleInputField.setText("");
        consoleInputField.setFocused2(true);
        consoleInputField.setEnabled(true);

        consoleOutput = "";
        currentPreviousCommandIndex = 0;
    }

    public void tick()
    {
        consoleTicks++;
        if (consoleInputField != null)
        {
            consoleInputField.tick();
        }

        if (consoleOutput != null && !consoleOutput.isEmpty() && consoleTicks >= consoleOutputTick + 100)
        {
            consoleOutput = "";
            consoleOutputTick = 0;
        }
    }

    public void executeCommand(String command)
    {
        if (command.isEmpty()) return;

        consoleInputField.setText("");
        previousCommands.add(0, command);
        currentPreviousCommandIndex = 0;

        if (command.startsWith("/"))
        {
            // Vanilla Command
            Minecraft.getInstance().player.sendChatMessage(command);
        } else {
            // Custom Command
            MinecraftForge.EVENT_BUS.post(new ExecuteConsoleCommandEvent(minecraft.player, command));
            ModNetwork.CHANNEL.sendToServer(new ExecuteConsoleCommandPacket(command));
        }
    }

    public void setConsoleOutput(String output)
    {
        this.consoleOutput = output;
        this.consoleOutputTick = consoleTicks;
    }

    public void cyclePreviousCommand(boolean up)
    {
        if (up)
        {
            currentPreviousCommandIndex = Math.min(previousCommands.size(), currentPreviousCommandIndex + 1);
        } else {
            currentPreviousCommandIndex = Math.max(0, currentPreviousCommandIndex - 1);
        }
        consoleInputField.setText(previousCommands.get(currentPreviousCommandIndex - 1));
    }

    public void openConsole()
    {
        isConsoleOpen = true;
        init();
    }

    public void closeConsole()
    {
        isConsoleOpen = false;
    }

    public void render(MatrixStack matrixStack, float partialTicks, boolean darkerBackground)
    {
        if (isConsoleOpen)
        {
            matrixStack.push();
            MainWindow mainWindow = minecraft.getMainWindow();
            double mouseX = minecraft.mouseHelper.getMouseX() * (double) mainWindow.getScaledWidth() / (double) mainWindow.getWidth();
            double mouseY = minecraft.mouseHelper.getMouseY() * (double) mainWindow.getScaledHeight() / (double) mainWindow.getHeight();

            int margin = 2;
            Screen.fill(matrixStack, margin, margin, mainWindow.getScaledWidth() - margin - 1, 10 + margin, new Color(0, 0, 0, darkerBackground ? 0.82f : 0.12f).getRGB());

            int textX = 6;
            int textY = 3;

            Screen.drawString(matrixStack, minecraft.fontRenderer, "> ", textX, textY, Color.WHITE.getRGB());
            consoleInputField.render(matrixStack, (int)mouseX, (int)mouseY, partialTicks);

            Screen.drawString(matrixStack, minecraft.fontRenderer, consoleOutput, 3, 15, new Color(245/255f, 178/255f, 39/255f, 0.8f).getRGB());

            matrixStack.pop();
        }
    }


    public void resize(Minecraft minecraft, int scaledWidth, int scaledHeight)
    {
        init();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.closeConsole();
            return true;
        } else if (keyCode == 257 || keyCode == 335) {
            executeCommand(consoleInputField.getText());
        } else if (keyCode == 265 || keyCode == 264) {
            cyclePreviousCommand(keyCode == 265);
        } else {
            consoleInputField.keyPressed(keyCode, scanCode, modifiers);
            return false;
        }
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        consoleInputField.keyReleased(keyCode, scanCode, modifiers);
        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        final KeyBinding[] keyBindings = ClientKeybinds.keyBindings;
        if (codePoint == keyBindings[0].func_238171_j_().getString().charAt(0)) return false;
        consoleInputField.charTyped(codePoint, modifiers);
        currentPreviousCommandIndex = 0;
        return false;
    }

    public boolean isConsoleOpen() {
        return isConsoleOpen;
    }

}
