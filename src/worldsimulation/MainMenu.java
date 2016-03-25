package worldsimulation;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu {

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private final InputHandler input;

    private BufferedImage mainMenu;
    private BufferedImage playGame;
    private BufferedImage bestScore;
    private BufferedImage quitGame;
    private BufferedImage playGameActive;
    private BufferedImage bestScoreActive;
    private BufferedImage quitGameActive;

    private int menuState = 0;
    private int holdButton = 0;

    /**
     * Creator of the class which creates Main Menu.
     *
     * @param input Class responsible of key typing.
     * @param width Width of the wndow.
     * @param height Height of the window.
     */
    public MainMenu(InputHandler input, int width, int height) {
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        this.input = input;
        init();
    }

    /**
     * Loads images.
     */
    private void init() {
        try {
            mainMenu = ImageIO.read(new File("res/components/mainMenu.png"));
            playGame = ImageIO.read(new File("res/components/playGame.png"));
            bestScore = ImageIO.read(new File("res/components/bestScore.png"));
            quitGame = ImageIO.read(new File("res/components/quitGame.png"));
            playGameActive = ImageIO.read(new File("res/components/playGameActive.png"));
            bestScoreActive = ImageIO.read(new File("res/components/bestScoreActive.png"));
            quitGameActive = ImageIO.read(new File("res/components/quitGameActive.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Display menu in the window.
     *
     * @param g
     */
    public void render(Graphics g) {
        g.drawImage(mainMenu, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

        if (menuState == 1) {
            g.drawImage(playGameActive, SCREEN_WIDTH / 2 - playGameActive.getWidth() / 2, 300, null);
        } else {
            g.drawImage(playGame, SCREEN_WIDTH / 2 - playGame.getWidth() / 2, 300, null);
        }

        if (menuState == 2) {
            g.drawImage(bestScoreActive, SCREEN_WIDTH / 2 - bestScoreActive.getWidth() / 2, 380, null);
        } else {
            g.drawImage(bestScore, SCREEN_WIDTH / 2 - bestScore.getWidth() / 2, 380, null);
        }

        if (menuState == 3) {
            g.drawImage(quitGameActive, SCREEN_WIDTH / 2 - quitGameActive.getWidth() / 2, 460, null);
        } else {
            g.drawImage(quitGame, SCREEN_WIDTH / 2 - quitGame.getWidth() / 2, 460, null);
        }
    }

    /**
     * Check if we type a key.
     */
    public void tick() {
        if (input.up.isPressed()) {
            holdButton++;
            if (holdButton > 10) {
                stateDown();
                holdButton = 0;
            }
        }
        if (input.down.isPressed()) {
            holdButton++;
            if (holdButton > 10) {
                stateUp();
                holdButton = 0;
            }
        }

        if (input.enter.isPressed()) {
            WorldSimulation.setWindowFlag(menuState);
        }
        if (!input.down.isPressed() && !input.up.isPressed()) {
            holdButton = 0;
        }
    }

    /**
     * Increase actual state.
     */
    private void stateUp() {
        menuState++;
        if (menuState >= 4) {
            menuState = 1;
        }
    }

    /**
     * Decrease actual state.
     */
    private void stateDown() {
        menuState--;
        if (menuState <= 0) {
            menuState = 3;
        }
    }

}
