package worldsimulation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Patryk
 */
public class WorldSimulation extends JPanel implements Runnable {

    static WorldSimulation world;
    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGHT = SCREEN_WIDTH * 9 / 16;
    private static final String NAME = "Worsim";

    private final JFrame window;
    public InputHandler input;
    private MainMenu menu;
    private Simulation simulation;
    private BestScore bestScore;

    private boolean gameRunning = false;
    private static int windowFlag = 0;
    private static boolean ready = false;

    private int tickCount = 0;

    /**
     * Starting class constructor. Creates new window and open Main Menu.
     */
    public WorldSimulation() {
        setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        window = new JFrame(NAME);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        window.add(this, BorderLayout.CENTER);
        window.pack();

        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        input = new InputHandler(this);
        new Thread(this).start();
    }

    /**
     * Method which finishes the game and writes best score to XML file by serialization.
     * @param finishTime Time when game has finished.
     * @param citizensOverall Number of borned citizens.
     * @param villainsOverall Number of borned villains.
     * @param superherosAlive Number of superheros which stayed alive.
     */
    public void gameOver(long finishTime, int citizensOverall, int villainsOverall, int superherosAlive) {
        String name = JOptionPane.showInputDialog("Please enter a player name");
        ArrayList<GameScore> gameScoreList = new ArrayList<>();

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

        gameScoreList.add(new GameScore(name, finishTime, citizensOverall, villainsOverall, superherosAlive));

        Collections.sort(gameScoreList, new Comparator<GameScore>() {
            @Override
            public int compare(GameScore gameScore1, GameScore gameScore2) {
                String finishTime1 = Long.toString(gameScore1.getFinishTime());
                String finishTime2 = Long.toString(gameScore2.getFinishTime());

                return finishTime2.compareTo(finishTime1);
            }
        });
        
        XMLEncoder in;
        try {
            in = new XMLEncoder(
                    new BufferedOutputStream(
                            new FileOutputStream("score.xml")));
            int numberOfScores = 0;
            for (GameScore gameScore : gameScoreList) {
                in.writeObject(gameScore);
                numberOfScores++;
                if (numberOfScores == 5) {
                    break;
                }
            }
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorldSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setWindowFlag(0);
    }

    /**
     * Main control of the program: - count when screen should be repainted -
     * control key typing
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 600000000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lasTimer = System.currentTimeMillis();
        double delta = 0;

        while (gameRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                repaint();
            }

            if (System.currentTimeMillis() - lasTimer > 1000) {
                lasTimer += 1000;
                System.out.println("\t\t\tframes: " + frames + ", ticks:" + ticks);
                frames = 0;
                ticks = 0;
            }

        }
    }

    /**
     * Checks where to send method waiting for key typing.
     */
    private void tick() {
        tickCount++;
        switch (windowFlag) {
            case 0:
                menu.tick();
                break;
            case 1:
                simulation.tick();
                break;
            case 2:
                bestScore.tick();
                break;
        }
    }

    /**
     * Repaint screen. Depends on which flag is actually setted: 0 refers to
     * menu, 1 refers to simulation, 2 refers to best score .
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (ready) {
            switch (windowFlag) {
                case 0:
                    menu.render(g);
                    break;
                case 1:
                    simulation.render(g);
                    break;
                case 2:
                    bestScore.render(g);
                    break;
            }
        }
    }

    /**
     * Is used when we want to change our window flag.
     */
    public synchronized void start() {
        this.requestFocusInWindow();
        switch (windowFlag) {
            case 0:
                menu = new MainMenu(input, getWidth(), getHeight());
                ready = true;
                gameRunning = true;
                run();
                break;
            case 1:
                simulation = new Simulation(input, getWidth(), getHeight());
                ready = true;
                break;

            case 2:
                bestScore = new BestScore(input, getWidth(), getHeight());
                ready = true;
                break;

            case 3:
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
                break;
        }
    }

    /**
     * Stops program running.
     */
    public synchronized void stop() {
        gameRunning = false;
    }

    /**
     * Change window flag.
     *
     * @param windowFlag Window flag you want to set (0 - Main Menu, 1 -
     * Simulation)
     */
    public static void setWindowFlag(int windowFlag) {
        WorldSimulation.windowFlag = windowFlag;
        ready = false;
        world.start();
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    /**
     * Main starting method.
     *
     * @param args
     */
    public static void main(String[] args) {
        world = new WorldSimulation();
        world.start();
    }

}
