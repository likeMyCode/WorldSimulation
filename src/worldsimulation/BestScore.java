package worldsimulation;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class BestScore {

    private BufferedImage bestScorePanelIcon = null;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private final InputHandler input;
    private ArrayList<GameScore> gameScoreList = new ArrayList<>();

    /**
     * Constructor of the class. Sets basic informations about best score.
     *
     * @param input Checks if we pressed key.
     * @param width Width of the window.
     * @param height Height of the window.
     */
    public BestScore(InputHandler input, int width, int height) {

        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        this.input = input;

        XMLDecoder out;
        try {
            out = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream("score.xml")));
            for (int i = 0; i < 5; i++) {
                gameScoreList.add((GameScore) out.readObject());
            }

            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorldSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            bestScorePanelIcon = ImageIO.read(new File("res/components/bestScorePanel.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Display best score in the window.
     *
     * @param g
     */
    public void render(Graphics g) {
        g.drawImage(bestScorePanelIcon, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);

        int positionY = 0;
        for (GameScore gameScore : gameScoreList) {
            long minutes = gameScore.getFinishTime() / 60;
            long seconds = gameScore.getFinishTime() % 60;
            String timeDisplay = "";

            if (minutes < 10) {
                timeDisplay = timeDisplay + "0";
            }
            timeDisplay = timeDisplay + minutes + ":";

            if (seconds < 10) {
                timeDisplay = timeDisplay + "0";
            }

            timeDisplay = timeDisplay + seconds;

            g.setFont(new Font("Sans", Font.BOLD, 22));
            g.drawString(gameScore.getPlayerName(), 240, 260 + positionY);
            g.drawString(Integer.toString(gameScore.getCitizensOverall()), 420, 260 + positionY);
            g.drawString(Integer.toString(gameScore.getVillainsOverall()), 580, 260 + positionY);
            g.drawString(Integer.toString(gameScore.getSuperherosAlive()), 770, 260 + positionY);
            g.drawString(timeDisplay, 910, 260 + positionY);
            positionY += 80;
        }
    }

    public void tick() {
        if (input.escape.isPressed()) {
            WorldSimulation.setWindowFlag(0);
        }
    }
}
