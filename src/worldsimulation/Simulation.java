package worldsimulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import worldsimulation.map.MapItem;
import worldsimulation.map.characters.Citizen;
import worldsimulation.map.characters.Superhero;
import worldsimulation.map.characters.Villain;
import worldsimulation.map.cities.CapitalCity;
import worldsimulation.map.cities.City;
import worldsimulation.map.cities.CityEntrance;
import worldsimulation.map.cities.CityExit;
import worldsimulation.map.roads.CrossRoad;
import worldsimulation.map.roads.CrossRoadLU;
import worldsimulation.map.roads.CrossRoadRU;
import worldsimulation.map.roads.CrossRoadLD;
import worldsimulation.map.roads.CrossRoadRD;
import worldsimulation.map.roads.Road;
import worldsimulation.map.roads.RoadDown;
import worldsimulation.map.roads.RoadLeft;
import worldsimulation.map.roads.RoadRight;
import worldsimulation.map.roads.RoadUp;
import worldsimulation.powersources.PowerSource;
import static worldsimulation.WorldSimulation.world;

public class Simulation implements MouseListener, Runnable {

    private static final int WORLD_WIDTH = 2973;
    private static final int WORLD_HEIGHT = 1500;
    private int xOffset = 20;
    private int yOffset = 450;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private final static int MOVEMENT_SPEED = 6;
    private long startTime = 0;
    private long actualTime = 0;
    private static boolean gameOver = false;

    private InputHandler input;
    private BufferedImage citizenDownIcon = null;
    private BufferedImage citizenUpIcon = null;
    private BufferedImage citizenRightIcon = null;
    private BufferedImage citizenLeftIcon = null;
    private BufferedImage villainDownIcon = null;
    private BufferedImage villainUpIcon = null;
    private BufferedImage villainRightIcon = null;
    private BufferedImage villainLeftIcon = null;
    private BufferedImage superheroDownIcon = null;
    private BufferedImage superheroUpIcon = null;
    private BufferedImage superheroRightIcon = null;
    private BufferedImage superheroLeftIcon = null;
    private BufferedImage grassIcon = null;
    private BufferedImage treeIcon = null;
    private BufferedImage roadUpIcon = null;
    private BufferedImage roadDownIcon = null;
    private BufferedImage roadLeftIcon = null;
    private BufferedImage roadRightIcon = null;
    private BufferedImage cityIcon = null;
    private BufferedImage cityDestroyedIcon = null;
    private BufferedImage cityEntranceIcon = null;
    private BufferedImage cityExitIcon = null;
    private BufferedImage capitalCityIcon = null;
    private BufferedImage crossRoadRD = null;
    private BufferedImage crossRoadLU = null;
    private BufferedImage crossRoadRU = null;
    private BufferedImage crossRoadLD = null;
    private BufferedImage addCitizenIcon = null;
    private BufferedImage addVillainIcon = null;
    private BufferedImage citizenPanelIcon = null;
    private BufferedImage citizenDeleteIcon = null;
    private BufferedImage citizenPauseIcon = null;
    private BufferedImage citizenResumeIcon = null;
    private BufferedImage citizenChangeCityIcon = null;
    private BufferedImage characterPointerIcon = null;
    private BufferedImage villainPanelIcon = null;
    private BufferedImage gameInfoPanelIcon = null;

    private MapItem citizenPanel, citizenDelete, citizenPause, citizenChangeCity;
    private boolean citizenSelected = false;
    private boolean villainSelected = false;
    private boolean superheroSelected = false;
    private boolean citySelected = false;
    private String selectedCitizen;
    private String selectedVillain;
    private String selectedSuperhero;
    private String selectedCity;

    private static ArrayList<MapItem> treeList = new ArrayList<>();
    private static ArrayList<MapItem> grassList = new ArrayList<>();
    public static ArrayList<Citizen> citizenList = new ArrayList<>();
    public static ArrayList<Villain> villainList = new ArrayList<>();
    public static ArrayList<Road> roadList = new ArrayList<>();
    public static ArrayList<City> citiesList = new ArrayList<>();
    public static ArrayList<Superhero> superheroList = new ArrayList<>();
    public static ArrayList<CrossRoad> crossRoadList = new ArrayList<>();
    public static ArrayList<CityExit> citiesExitList = new ArrayList<>();
    public static ArrayList<CityEntrance> citiesEntranceList = new ArrayList<>();

    private static int citizenOverall = 0;
    private static int citizensAlive = 0;
    private static int villainOverall = 0;
    private static int villainsAlive = 0;
    private static int superheroAlive = 0;
    private static int populationNumber = 0;

    /**
     * Constructor of the simulation class. Loads all of the needed images. Set
     * most important fields. Generate world, characters, entities, game panel.
     *
     * @param input Object which is responsible of key typing.
     * @param width Width of the window.
     * @param height Height of the window.
     */
    public Simulation(InputHandler input, int width, int height) {

        world.addMouseListener(this);
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        this.input = input;

        citizenOverall = 0;
        citizensAlive = 0;
        villainOverall = 0;
        villainsAlive = 0;
        superheroAlive = 0;
        populationNumber = 0;

        //** Open files with map items icons.
        try {
            citizenDownIcon = ImageIO.read(new File("res/characters/citizenDownIcon.png"));
            citizenUpIcon = ImageIO.read(new File("res/characters/citizenUpIcon.png"));
            citizenRightIcon = ImageIO.read(new File("res/characters/citizenRightIcon.png"));
            citizenLeftIcon = ImageIO.read(new File("res/characters/citizenLeftIcon.png"));
            villainDownIcon = ImageIO.read(new File("res/characters/villainDownIcon.png"));
            villainUpIcon = ImageIO.read(new File("res/characters/villainUpIcon.png"));
            villainRightIcon = ImageIO.read(new File("res/characters/villainRightIcon.png"));
            villainLeftIcon = ImageIO.read(new File("res/characters/villainLeftIcon.png"));
            superheroDownIcon = ImageIO.read(new File("res/characters/superheroDownIcon.png"));
            superheroUpIcon = ImageIO.read(new File("res/characters/superheroUpIcon.png"));
            superheroRightIcon = ImageIO.read(new File("res/characters/superheroRightIcon.png"));
            superheroLeftIcon = ImageIO.read(new File("res/characters/superheroLeftIcon.png"));
            grassIcon = ImageIO.read(new File("res/components/grassIcon.png"));
            treeIcon = ImageIO.read(new File("res/components/treeIcon.png"));
            roadUpIcon = ImageIO.read(new File("res/components/roadUpIcon.png"));
            roadDownIcon = ImageIO.read(new File("res/components/roadDownIcon.png"));
            roadLeftIcon = ImageIO.read(new File("res/components/roadLeftIcon.png"));
            roadRightIcon = ImageIO.read(new File("res/components/roadRightIcon.png"));
            cityIcon = ImageIO.read(new File("res/components/cityIcon.png"));
            cityDestroyedIcon = ImageIO.read(new File("res/components/cityDestroyed.png"));
            cityEntranceIcon = ImageIO.read(new File("res/components/cityEntranceIcon.png"));
            cityExitIcon = ImageIO.read(new File("res/components/cityExitIcon.png"));
            capitalCityIcon = ImageIO.read(new File("res/components/capitalCityIcon.png"));
            crossRoadRD = ImageIO.read(new File("res/components/crossRoadRDIcon.png"));
            crossRoadLU = ImageIO.read(new File("res/components/crossRoadLUIcon.png"));
            crossRoadRU = ImageIO.read(new File("res/components/crossRoadRUIcon.png"));
            crossRoadLD = ImageIO.read(new File("res/components/crossRoadLDIcon.png"));
            addCitizenIcon = ImageIO.read(new File("res/components/addCitizenIcon.png"));
            addVillainIcon = ImageIO.read(new File("res/components/addVillainIcon.png"));
            citizenPanelIcon = ImageIO.read(new File("res/components/citizenPanel2.png"));  //
            citizenDeleteIcon = ImageIO.read(new File("res/components/citizenDelete.png"));
            citizenPauseIcon = ImageIO.read(new File("res/components/citizenPause.png"));
            citizenResumeIcon = ImageIO.read(new File("res/components/citizenResume.png"));
            citizenChangeCityIcon = ImageIO.read(new File("res/components/citizenChangeCity.png"));
            characterPointerIcon = ImageIO.read(new File("res/components/characterPointer.png"));
            villainPanelIcon = ImageIO.read(new File("res/components/villainPanel2.png"));
            gameInfoPanelIcon = ImageIO.read(new File("res/components/gameInfoPanel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        generateWorld();
        generateCities();
        generateRoads();
        generateCrossRoads();
        generateCitizens(7);
        generateSuperheros();
        generateGamePanel();

        this.startTime = System.currentTimeMillis();

        SimulationController control = new SimulationController(this);
        new Thread(this).start();
    }

    /**
     * Display simulation in the window.
     *
     * @param g
     */
    public void render(Graphics g) {

        renderGrass(g);
        renderTree(g);
        renderRoad(g);
        renderCity(g);
        renderCrossroad(g);
        renderCitizen(g);
        renderVillain(g);
        renderSuperhero(g);
        renderGamePanel(g);
    }

    /**
     * Draw grass.
     *
     * @param g
     */
    private void renderGrass(Graphics g) {
        for (MapItem grass : grassList) {
            if (grass.getPositionX() + 32 >= xOffset && grass.getPositionX() <= xOffset + SCREEN_WIDTH
                    && grass.getPositionY() + 32 >= yOffset && grass.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                g.drawImage(grassIcon, grass.getPositionX() - xOffset, grass.getPositionY() - yOffset, null);
            }
        }
    }

    /**
     * Draw Tree.
     *
     * @param g
     */
    private void renderTree(Graphics g) {
        for (MapItem tree : treeList) {
            if (tree.getPositionX() + 64 >= xOffset && tree.getPositionX() <= xOffset + SCREEN_WIDTH
                    && tree.getPositionY() + 64 >= yOffset && tree.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                g.drawImage(treeIcon, tree.getPositionX() - xOffset, tree.getPositionY() - yOffset, null);
            }
        }
    }

    /**
     * Draw Roads.
     *
     * @param g
     */
    private void renderRoad(Graphics g) {
        for (Road road : roadList) {
            if (road.getPositionX() + 32 >= xOffset && road.getPositionX() <= xOffset + SCREEN_WIDTH
                    && road.getPositionY() + 32 >= yOffset && road.getPositionY() <= yOffset + SCREEN_HEIGHT) {

                switch (road.getMovingDir()) {
                    case 1:
                        g.drawImage(roadUpIcon, road.getPositionX() - xOffset, road.getPositionY() - yOffset, null);
                        break;
                    case 2:
                        g.drawImage(roadLeftIcon, road.getPositionX() - xOffset, road.getPositionY() - yOffset, null);
                        break;
                    case 3:
                        g.drawImage(roadRightIcon, road.getPositionX() - xOffset, road.getPositionY() - yOffset, null);
                        break;
                    case 4:
                        g.drawImage(roadDownIcon, road.getPositionX() - xOffset, road.getPositionY() - yOffset, null);
                        break;
                }
            }
        }
    }

    /**
     * Draw Cities and city entrances.
     *
     * @param g
     */
    private void renderCity(Graphics g) {
        for (CityEntrance cityEntrance : citiesEntranceList) {
            if (cityEntrance.getPositionX() + 32 >= xOffset && cityEntrance.getPositionX() <= xOffset + SCREEN_WIDTH
                    && cityEntrance.getPositionY() + 32 >= yOffset && cityEntrance.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                g.drawImage(cityEntranceIcon, cityEntrance.getPositionX() - xOffset, cityEntrance.getPositionY() - yOffset, null);
            }
        }

        for (CityExit cityExit : citiesExitList) {
            if (cityExit.getPositionX() + 32 >= xOffset && cityExit.getPositionX() <= xOffset + SCREEN_WIDTH
                    && cityExit.getPositionY() + 32 >= yOffset && cityExit.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                g.drawImage(cityExitIcon, cityExit.getPositionX() - xOffset, cityExit.getPositionY() - yOffset, null);
            }
        }

        for (City city : citiesList) {
            if (city.getPositionX() + 192 >= xOffset && city.getPositionX() <= xOffset + SCREEN_WIDTH
                    && city.getPositionY() + 192 >= yOffset && city.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                if (city.getName() == "Metropolis") {
                    g.drawImage(capitalCityIcon, city.getPositionX() - xOffset, city.getPositionY() - yOffset, null);
                    break;
                }
                if (citySelected) {
                    if (selectedCity == city.getName()) {
                        //g.setColor(new Color(255, 136, 104));
                        g.setColor(new Color(48, 57, 60));
                        g.fillRect(city.getPositionX() - xOffset - 2, city.getPositionY() - yOffset - 2, 196, 196);
                        g.setColor(Color.BLACK);
                    }
                }

                if (city.isDestroyed()) {
                    g.drawImage(cityDestroyedIcon, city.getPositionX() - xOffset, city.getPositionY() - yOffset, null);
                } else {
                    g.drawImage(cityIcon, city.getPositionX() - xOffset, city.getPositionY() - yOffset, null);
                }
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString(city.getName(), city.getPositionX() - xOffset, city.getPositionY() - 4 - yOffset);

                if (city.isUnderAttack()) {
                    g.setColor(Color.RED);
                    g.fillRoundRect(city.getPositionX() + 165 - xOffset, city.getPositionY() - 20 - yOffset, 15, 15, 3, 3);
                    g.setColor(Color.black);
                }
            }
        }
    }

    /**
     * Draw crossroads.
     *
     * @param g
     */
    private void renderCrossroad(Graphics g) {

        for (CrossRoad crossRoad : crossRoadList) {
            if (crossRoad.getPositionX() + 32 >= xOffset && crossRoad.getPositionX() <= xOffset + SCREEN_WIDTH
                    && crossRoad.getPositionY() + 32 >= yOffset && crossRoad.getPositionY() <= yOffset + SCREEN_HEIGHT) {

                switch (crossRoad.getMovingDir()) {
                    case 1:
                        g.drawImage(crossRoadRU, crossRoad.getPositionX() - xOffset, crossRoad.getPositionY() - yOffset, null);
                        break;
                    case 2:
                        g.drawImage(crossRoadLU, crossRoad.getPositionX() - xOffset, crossRoad.getPositionY() - yOffset, null);
                        break;
                    case 3:
                        g.drawImage(crossRoadRD, crossRoad.getPositionX() - xOffset, crossRoad.getPositionY() - yOffset, null);
                        break;
                    case 4:
                        g.drawImage(crossRoadLD, crossRoad.getPositionX() - xOffset, crossRoad.getPositionY() - yOffset, null);
                        break;
                }
            }
        }
    }

    /**
     * Draw citizens.
     *
     * @param g
     */
    private void renderCitizen(Graphics g) {
        for (Citizen citizen : citizenList) {
            if (citizen.getPositionX() + 32 >= xOffset && citizen.getPositionX() <= xOffset + SCREEN_WIDTH
                    && citizen.getPositionY() + 32 >= yOffset && citizen.getPositionY() <= yOffset + SCREEN_HEIGHT && !citizen.isDead()) {
                switch (citizen.getMovingDir()) {
                    case 1:
                        g.drawImage(citizenUpIcon, citizen.getPositionX() - xOffset, citizen.getPositionY() - yOffset, null);

                        break;
                    case 2:
                        g.drawImage(citizenLeftIcon, citizen.getPositionX() - xOffset, citizen.getPositionY() - yOffset, null);

                        break;
                    case 3:
                        g.drawImage(citizenRightIcon, citizen.getPositionX() - xOffset, citizen.getPositionY() - yOffset, null);

                        break;
                    case 4:
                        g.drawImage(citizenDownIcon, citizen.getPositionX() - xOffset, citizen.getPositionY() - yOffset, null);

                        break;
                }
                g.setFont(new Font("Arial", Font.PLAIN, 13));
                g.drawString(citizen.getNextCity(), citizen.getPositionX() - xOffset, citizen.getPositionY() - 4 - yOffset);
            }
        }
    }

    /**
     * Draw villains.
     *
     * @param g
     */
    private void renderVillain(Graphics g) {
        for (Villain villain : villainList) {
            if (villain.getPositionX() + 32 >= xOffset && villain.getPositionX() <= xOffset + SCREEN_WIDTH
                    && villain.getPositionY() + 32 >= yOffset && villain.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                switch (villain.getMovingDir()) {
                    case 1:
                        g.drawImage(villainUpIcon, villain.getPositionX() - xOffset, villain.getPositionY() - yOffset, null);
                        break;
                    case 2:
                        g.drawImage(villainLeftIcon, villain.getPositionX() - xOffset, villain.getPositionY() - yOffset, null);
                        break;
                    case 3:
                        g.drawImage(villainRightIcon, villain.getPositionX() - xOffset, villain.getPositionY() - yOffset, null);
                        break;
                    case 4:
                        g.drawImage(villainDownIcon, villain.getPositionX() - xOffset, villain.getPositionY() - yOffset, null);
                        break;
                }

                g.fillRect(villain.getPositionX() - xOffset - 7, villain.getPositionY() - yOffset - 12, 45, 6);
                if ((100 * villain.getHealth()) / villain.getMaxHeath() > 60) {
                    g.setColor(Color.GREEN);
                } else if ((100 * villain.getHealth()) / villain.getMaxHeath() > 30) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.RED);
                }
                g.fillRect(villain.getPositionX() - xOffset - 6, villain.getPositionY() - yOffset - 11, (43 * (100 * villain.getHealth()) / villain.getMaxHeath()) / 100, 4);
                g.setColor(Color.BLACK);
            }
        }
    }

    /**
     * Draw superheros.
     *
     * @param g
     */
    private void renderSuperhero(Graphics g) {
        for (Superhero superhero : superheroList) {
            if (superhero.getPositionX() + 32 >= xOffset && superhero.getPositionX() <= xOffset + SCREEN_WIDTH
                    && superhero.getPositionY() + 32 >= yOffset && superhero.getPositionY() <= yOffset + SCREEN_HEIGHT) {
                switch (superhero.getMovingDir()) {
                    case 1:
                        g.drawImage(superheroUpIcon, superhero.getPositionX() - xOffset, superhero.getPositionY() - yOffset, null);
                        break;
                    case 2:
                        g.drawImage(superheroLeftIcon, superhero.getPositionX() - xOffset, superhero.getPositionY() - yOffset, null);
                        break;
                    case 3:
                        g.drawImage(superheroRightIcon, superhero.getPositionX() - xOffset, superhero.getPositionY() - yOffset, null);
                        break;
                    case 4:
                        g.drawImage(superheroDownIcon, superhero.getPositionX() - xOffset, superhero.getPositionY() - yOffset, null);
                        break;
                }

                g.fillRect(superhero.getPositionX() - xOffset - 7, superhero.getPositionY() - yOffset - 12, 45, 6);
                if ((100 * superhero.getHealth()) / superhero.getMaxHeath() > 60) {
                    g.setColor(Color.GREEN);
                } else if ((100 * superhero.getHealth()) / superhero.getMaxHeath() > 30) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.RED);
                }
                g.fillRect(superhero.getPositionX() - xOffset - 6, superhero.getPositionY() - yOffset - 11, (43 * (100 * superhero.getHealth()) / superhero.getMaxHeath()) / 100, 4);
                g.setColor(Color.BLACK);
            }
        }
    }

    /**
     * Draw game panel.
     *
     * @param g
     */
    private void renderGamePanel(Graphics g) {
        this.actualTime = System.currentTimeMillis() - startTime;
        long minutes = actualTime / (1000 * 60);
        long seconds = actualTime / 1000 % 60;
        String timeDisplay = "";

        if (minutes < 10) {
            timeDisplay = timeDisplay + "0";
        }
        timeDisplay = timeDisplay + minutes + ":";

        if (seconds < 10) {
            timeDisplay = timeDisplay + "0";
        }

        timeDisplay = timeDisplay + seconds;

        g.drawImage(addCitizenIcon, 0, SCREEN_HEIGHT / 2 - addCitizenIcon.getHeight() / 2 - 3, null);
        g.drawImage(addVillainIcon, 0, SCREEN_HEIGHT / 2 + addCitizenIcon.getHeight() / 2 + 3, null);
        g.drawImage(gameInfoPanelIcon, SCREEN_WIDTH - gameInfoPanelIcon.getWidth(), 0, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Sans", Font.PLAIN, 18));
        g.drawString(Integer.toString(citizensAlive), SCREEN_WIDTH - gameInfoPanelIcon.getWidth() + 54, 26);
        g.drawString(Integer.toString(villainsAlive), SCREEN_WIDTH - gameInfoPanelIcon.getWidth() + 132, 26);
        g.drawString(Integer.toString(superheroAlive), SCREEN_WIDTH - gameInfoPanelIcon.getWidth() + 209, 26);
        g.setColor(new Color(63, 63, 63));
        g.drawString(timeDisplay, SCREEN_WIDTH - gameInfoPanelIcon.getWidth() + 264, 26);
        g.setColor(Color.BLACK);

        if (citizenSelected) {
            g.drawImage(citizenPanelIcon, citizenPanel.getPositionX(), citizenPanel.getPositionY(), null);
            g.drawImage(citizenDeleteIcon, citizenDelete.getPositionX(), citizenDelete.getPositionY(), null);
            g.drawImage(citizenChangeCityIcon, citizenChangeCity.getPositionX(), citizenChangeCity.getPositionY(), null);
            for (Citizen citizen : citizenList) {
                if (citizen.getName() == selectedCitizen) {
                    if (citizen.isInCity() || citizen.isDead()) {
                        citizenSelected = false;
                    }
                    if (citizen.isHalted()) {
                        g.drawImage(citizenResumeIcon, citizenPause.getPositionX(), citizenPause.getPositionY(), null);

                    } else {
                        g.drawImage(citizenPauseIcon, citizenPause.getPositionX(), citizenPause.getPositionY(), null);

                    }
                    g.setFont(new Font("Microsoft New Tai Lue Bold", Font.PLAIN, 11));
                    g.drawString("Name: " + citizen.getName(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 30);
                    g.drawString("Surname: " + citizen.getSurname(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 48);
                    g.drawString("Hometown: " + citizen.getHometown(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 66);
                    g.drawString("Heading: " + citizen.getNextCity(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 84);
                    g.drawImage(characterPointerIcon, citizen.getPositionX() - xOffset + 7, citizen.getPositionY() - yOffset - 40, null);
                }
            }
        }

        if (villainSelected) {
            g.drawImage(villainPanelIcon, citizenPanel.getPositionX(), citizenPanel.getPositionY(), null);
            for (Villain villain : villainList) {
                if (villain.getName() == selectedVillain) {
                    if (villain.isInCity()) {
                        villainSelected = false;
                    }

                    g.setFont(new Font("Microsoft New Tai Lue Bold", Font.PLAIN, 11));
                    g.drawString("Name: " + villain.getName(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 30);
                    g.drawString("Heading: " + villain.getNextCity(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 48);
                    g.drawString("Health: " + villain.getHealth(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 66);
                    g.drawString("Inteligence: " + villain.getInteligence(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 84);
                    g.drawString("Strength: " + villain.getStrength(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 102);
                    g.drawString("Speed: " + villain.getSpeed(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 120);
                    g.drawString("Stamina: " + villain.getStamina(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 138);
                    g.drawString("Energy: " + villain.getEnergy(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 156);
                    g.drawString("Fight Ability: " + villain.getFightAbility(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 174);

                    g.drawImage(characterPointerIcon, villain.getPositionX() - xOffset + 7, villain.getPositionY() - yOffset - 40, null);
                }
            }
        }

        if (superheroSelected) {
            g.drawImage(villainPanelIcon, citizenPanel.getPositionX(), citizenPanel.getPositionY(), null);
            for (Superhero superhero : superheroList) {
                if (superhero.getName() == selectedSuperhero) {
                    if (superhero.isInCity() || superhero.isWaitingInCapitalCity()) {
                        superheroSelected = false;
                    }

                    g.setFont(new Font("Microsoft New Tai Lue Bold", Font.PLAIN, 11));
                    g.drawString("Name: " + superhero.getName(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 30);
                    g.drawString("Heading: " + superhero.getNextCity(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 48);
                    g.drawString("Health: " + superhero.getHealth(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 66);
                    g.drawString("Inteligence: " + superhero.getInteligence(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 84);
                    g.drawString("Strength: " + superhero.getStrength(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 102);
                    g.drawString("Speed: " + superhero.getSpeed(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 120);
                    g.drawString("Stamina: " + superhero.getStamina(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 138);
                    g.drawString("Energy: " + superhero.getEnergy(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 156);
                    g.drawString("Fight Ability: " + superhero.getFightAbility(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 174);

                    g.drawImage(characterPointerIcon, superhero.getPositionX() - xOffset + 7, superhero.getPositionY() - yOffset - 40, null);
                }
            }
        }

        if (citySelected) {
            for (City city : citiesList) {
                if (city.getName() == selectedCity) {
                    ArrayList<PowerSource> powerSourceList = city.getPowerSourceList();
                    g.drawImage(villainPanelIcon, citizenPanel.getPositionX(), citizenPanel.getPositionY(), null);
                    g.setFont(new Font("Microsoft New Tai Lue Bold", Font.PLAIN, 11));
                    g.drawString("Name: " + city.getName(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 30);
                    g.drawString("Residents Number: " + city.getResidentsNumber(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 48);
                    g.drawString("Power Sources: ", citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + 84);
                    int i = 84 + 18;
                    for (PowerSource powerSource : powerSourceList) {
                        g.drawString("- " + powerSource.getAbilityName() + ": " + powerSource.getPowerPotential(), citizenPanel.getPositionX() + 30, citizenPanel.getPositionY() + i);
                        i += 18;
                    }
                }
            }
        }
    }

    /**
     * Create world: - grass - tree.
     */
    private void generateWorld() {

        for (int i = 0; i < WORLD_WIDTH; i = i + 32) {
            for (int j = 0; j < WORLD_HEIGHT; j = j + 32) {
                grassList.add(new MapItem(i, j));
            }
        }

        File treeFile = new File("res/tree.txt");
        try {
            Scanner treeScanner = new Scanner(treeFile);
            while (treeScanner.hasNextLine()) {
                String readText = treeScanner.nextLine();
                int x1 = Integer.parseInt(readText);
                readText = treeScanner.nextLine();
                int x2 = Integer.parseInt(readText);
                readText = treeScanner.nextLine();
                int y1 = Integer.parseInt(readText);
                readText = treeScanner.nextLine();
                int y2 = Integer.parseInt(readText);

                for (int x = x1; x < x2; x++) {
                    for (int y = y1; y < y2; y++) {
                        treeList.add(new MapItem(55 * x + 6, 50 * y));

                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Simulation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Generate cities.
     */
    private void generateCities() {
        citiesList.add(new City(258, 145, "Aldhurst", 0));
        citiesList.add(new City(288, 980, "Valcoast", 0));
        citiesList.add(new City(673, 17, "Strongshade", 0));
        citiesList.add(new City(669, 1297, "Marbleshore", 0));
        citiesList.add(new City(1151, 342, "Springfield", 0));
        citiesList.add(new City(1408, 664, "Valhalla", 0));
        citiesList.add(new City(1536, 83, "Atlantis", 0));
        citiesList.add(new City(2332, 625, "Arkham", 0));
        citiesList.add(new City(2744, 82, "Rapture", 0));
        citiesList.add(new CapitalCity(2428, 906, "Metropolis", 0));
    }

    /**
     * Generate citizens.
     *
     * @param citizenNumber
     */
    private void generateCitizens(int citizenNumber) {
        for (int i = 0; i < citizenNumber; i++) {
            citizenList.add(new Citizen(32 * i, 721 + 32 + 2, "citezennio" + citizenOverall, "surname" + citizenOverall, Citizen.randomCity()));
            populationInc();
        }

        for (Citizen citizen : citizenList) {
            citizen.setModifyingList(false);
        }
    }

    /**
     * Generate superheros.
     */
    private void generateSuperheros() {
        superheroList.add(new Superhero(-32, -32, "Wolverine"));
        superheroList.add(new Superhero(-32, -32, "Captain Obvious"));
        superheroList.add(new Superhero(-32, -32, "Daredevil"));
        superheroList.add(new Superhero(-32, -32, "Yellow Priest"));
        superheroList.add(new Superhero(-32, -32, "Profesor X"));
        superheroList.add(new Superhero(-32, -32, "Black Panther"));
        superheroList.add(new Superhero(-32, -32, "Nick Fury"));
        superheroList.add(new Superhero(-32, -32, "Ghost Rider"));
        superheroList.add(new Superhero(-32, -32, "Britney Spears"));
        superheroList.add(new Superhero(-32, -32, "Nicholas Cage"));
    }

    /**
     * Generate crossroads.
     */
    private void generateCrossRoads() {
        crossRoadList.add(new CrossRoadLD(320, 721 + 32, new String[]{""}));
        crossRoadList.add(new CrossRoadRD(320 + 32, 721 + 32, new String[]{"Strongshade", "Springfield", "Valcoast", "Marbleshore", "Valhalla", "Atlantis", "Arkham", "Rapture", "Metropolis"}));
        crossRoadList.add(new CrossRoadRU(320 + 32, 721, new String[]{"Aldhurst"}));
        crossRoadList.add(new CrossRoadLU(320, 721, new String[]{""}));

        crossRoadList.add(new CrossRoadLD(736, 721 + 32, new String[]{"Valcoast", "Marbleshore", "Valhalla", "Atlantis", "Arkham", "Rapture", "Metropolis"}));
        crossRoadList.add(new CrossRoadRD(736 + 32, 721 + 32, new String[]{""}));
        crossRoadList.add(new CrossRoadRU(736 + 32, 721, new String[]{"Strongshade", "Springfield"}));
        crossRoadList.add(new CrossRoadLU(736, 721, new String[]{"Aldhurst"}));

        crossRoadList.add(new CrossRoadLD(736, 401 + 32, new String[]{"Aldhurst", "Valcoast", "Marbleshore", "Valhalla", "Atlantis", "Arkham", "Rapture", "Metropolis"}));
        crossRoadList.add(new CrossRoadRD(736 + 32, 401 + 32, new String[]{"Springfield"}));
        crossRoadList.add(new CrossRoadRU(736 + 32, 401, new String[]{"Strongshade"}));
        crossRoadList.add(new CrossRoadLU(736, 401, new String[]{""}));

        crossRoadList.add(new CrossRoadLD(736, 1041 + 32, new String[]{"Marbleshore"}));
        crossRoadList.add(new CrossRoadRD(736 + 32, 1041 + 32, new String[]{"Valhalla", "Atlantis", "Arkham", "Rapture", "Metropolis"}));
        crossRoadList.add(new CrossRoadRU(736 + 32, 1041, new String[]{"Aldhurst", "Strongshade", "Springfield"}));
        crossRoadList.add(new CrossRoadLU(736, 1041, new String[]{"Valcoast"}));

        crossRoadList.add(new CrossRoadLD(1952, 1041 + 32, new String[]{""}));
        crossRoadList.add(new CrossRoadRD(1952 + 32, 1041 + 32, new String[]{"Metropolis"}));
        crossRoadList.add(new CrossRoadRU(1952 + 32, 1041, new String[]{"Valhalla", "Atlantis", "Arkham", "Rapture"}));
        crossRoadList.add(new CrossRoadLU(1952, 1041, new String[]{"Aldhurst", "Strongshade", "Springfield", "Valcoast", "Marbleshore"}));

        crossRoadList.add(new CrossRoadLD(1952, 721 + 32, new String[]{"Aldhurst", "Strongshade", "Springfield", "Valcoast", "Marbleshore", "Metropolis"}));
        crossRoadList.add(new CrossRoadRD(1952 + 32, 721 + 32, new String[]{""}));
        crossRoadList.add(new CrossRoadRU(1952 + 32, 721, new String[]{"Atlantis", "Arkham", "Rapture"}));
        crossRoadList.add(new CrossRoadLU(1952, 721, new String[]{"Valhalla"}));

        crossRoadList.add(new CrossRoadLD(1952, 145 + 32, new String[]{"Aldhurst", "Strongshade", "Springfield", "Valcoast", "Marbleshore", "Metropolis", "Valhalla"}));
        crossRoadList.add(new CrossRoadRD(1952 + 32, 145 + 32, new String[]{"Arkham", "Rapture"}));
        crossRoadList.add(new CrossRoadRU(1952 + 32, 145, new String[]{""}));
        crossRoadList.add(new CrossRoadLU(1952, 145, new String[]{"Atlantis"}));

        crossRoadList.add(new CrossRoadLD(2400, 145 + 32, new String[]{"Arkham"}));
        crossRoadList.add(new CrossRoadRD(2400 + 32, 145 + 32, new String[]{"Rapture"}));
        crossRoadList.add(new CrossRoadRU(2400 + 32, 145, new String[]{""}));
        crossRoadList.add(new CrossRoadLU(2400, 145, new String[]{"Aldhurst", "Strongshade", "Springfield", "Valcoast", "Marbleshore", "Metropolis", "Valhalla", "Atlantis"}));
    }

    /**
     * Generate roads.
     */
    private void generateRoads() {

        for (int i = 0; i < 10; i++) {
            roadList.add(new RoadLeft(0 + i * 32, 721));
            roadList.add(new RoadRight(0 + i * 32, 721 + 32));
        }

        for (int i = 0; i < 11; i++) {
            roadList.add(new RoadLeft(384 + i * 32, 721));
            roadList.add(new RoadRight(384 + i * 32, 721 + 32));
        }

        for (int i = 0; i < 10; i++) {
            roadList.add(new RoadLeft(1632 + i * 32, 721));
            roadList.add(new RoadRight(1632 + i * 32, 721 + 32));
        }

        for (int i = 0; i < 11; i++) {
            roadList.add(new RoadDown(320, 369 + i * 32));
            roadList.add(new RoadUp(320 + 32, 369 + i * 32));
        }

        for (int i = 0; i < 5; i++) {
            roadList.add(new RoadDown(736, 241 + i * 32));
            roadList.add(new RoadUp(736 + 32, 241 + i * 32));
        }

        for (int i = 0; i < 8; i++) {
            roadList.add(new RoadDown(736, 465 + i * 32));
            roadList.add(new RoadUp(736 + 32, 465 + i * 32));
        }

        for (int i = 0; i < 8; i++) {
            roadList.add(new RoadDown(736, 785 + i * 32));
            roadList.add(new RoadUp(736 + 32, 785 + i * 32));
        }

        for (int i = 0; i < 5; i++) {
            roadList.add(new RoadDown(736, 1105 + i * 32));
            roadList.add(new RoadUp(736 + 32, 1105 + i * 32));
        }

        for (int i = 0; i < 7; i++) {
            roadList.add(new RoadLeft(512 + i * 32, 1041));
            roadList.add(new RoadRight(512 + i * 32, 1041 + 32));
        }

        for (int i = 0; i < 36; i++) {
            roadList.add(new RoadLeft(800 + i * 32, 1041));
            roadList.add(new RoadRight(800 + i * 32, 1041 + 32));
        }

        for (int i = 0; i < 10; i++) {
            roadList.add(new RoadLeft(800 + i * 32, 401));
            roadList.add(new RoadRight(800 + i * 32, 401 + 32));
        }

        for (int i = 0; i < 12; i++) {
            roadList.add(new RoadLeft(2016 + i * 32, 1041));
            roadList.add(new RoadRight(2016 + i * 32, 1041 + 32));
        }

        for (int i = 0; i < 8; i++) {
            roadList.add(new RoadDown(1952, 785 + i * 32));
            roadList.add(new RoadUp(1952 + 32, 785 + i * 32));
        }

        for (int i = 0; i < 16; i++) {
            roadList.add(new RoadDown(1952, 209 + i * 32));
            roadList.add(new RoadUp(1952 + 32, 209 + i * 32));
        }

        for (int i = 0; i < 6; i++) {
            roadList.add(new RoadLeft(1760 + i * 32, 145));
            roadList.add(new RoadRight(1760 + i * 32, 145 + 32));
        }

        for (int i = 0; i < 12; i++) {
            roadList.add(new RoadLeft(2016 + i * 32, 145));
            roadList.add(new RoadRight(2016 + i * 32, 145 + 32));
        }

        for (int i = 0; i < 8; i++) {
            roadList.add(new RoadLeft(2464 + i * 32, 145));
            roadList.add(new RoadRight(2464 + i * 32, 145 + 32));
        }

        for (int i = 0; i < 12; i++) {
            roadList.add(new RoadDown(2400, 209 + i * 32));
            roadList.add(new RoadUp(2400 + 32, 209 + i * 32));
        }

        citiesEntranceList.add(new CityEntrance(320 + 32, 337, "Aldhurst"));
        citiesExitList.add(new CityExit(320, 337, "Aldhurst", 4));

        citiesEntranceList.add(new CityEntrance(736 + 32, 209, "Strongshade"));
        citiesExitList.add(new CityExit(736, 209, "Strongshade", 4));

        citiesEntranceList.add(new CityEntrance(736, 1265, "Marbleshore"));
        citiesExitList.add(new CityExit(736 + 32, 1265, "Marbleshore", 1));

        citiesEntranceList.add(new CityEntrance(2400, 593, "Arkham"));
        citiesExitList.add(new CityExit(2400 + 32, 593, "Arkham", 1));

        citiesEntranceList.add(new CityEntrance(480, 1041, "Valcoast"));
        citiesExitList.add(new CityExit(480, 1041 + 32, "Valcoast", 3));

        citiesEntranceList.add(new CityEntrance(1600, 721, "Valhalla"));
        citiesExitList.add(new CityExit(1600, 721 + 32, "Valhalla", 3));

        citiesEntranceList.add(new CityEntrance(1728, 145, "Atlantis"));
        citiesExitList.add(new CityExit(1728, 145 + 32, "Atlantis", 3));

        citiesEntranceList.add(new CityEntrance(1120, 401 + 32, "Springfield"));
        citiesExitList.add(new CityExit(1120, 401, "Springfield", 2));

        citiesEntranceList.add(new CityEntrance(2397, 1041 + 32, "Metropolis"));
        citiesExitList.add(new CityExit(2397, 1041, "Metropolis", 2));

        citiesEntranceList.add(new CityEntrance(2712, 145 + 32, "Rapture"));
        citiesExitList.add(new CityExit(2712, 145, "Rapture", 2));
    }

    /**
     * Generate game panel.
     */
    private void generateGamePanel() {
        citizenPanel = new MapItem(SCREEN_WIDTH - 165, SCREEN_HEIGHT / 2 - 120);
        citizenDelete = new MapItem(citizenPanel.getPositionX() + 20, citizenPanel.getPositionY() + 112);
        citizenPause = new MapItem(citizenDelete.getPositionX() + 50, citizenDelete.getPositionY());
        citizenChangeCity = new MapItem(citizenPause.getPositionX() + 50, citizenPause.getPositionY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * This method is responsible of checking what user actually clicked, and
     * set proper flags. Also adding new citizens, and sending superheros from
     * Capital city.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        boolean noPlaceForCitizen = false;
        boolean havePlaceToGo = false;

        // Dodawanie nowego cywila
        if (e.getX() >= 0 && e.getX() <= addCitizenIcon.getWidth() && e.getY() >= SCREEN_HEIGHT / 2 - addCitizenIcon.getHeight() / 2 - 3
                && e.getY() <= SCREEN_HEIGHT / 2 - addCitizenIcon.getHeight() / 2 - 3 + addCitizenIcon.getHeight()) {

            for (Citizen citizen : citizenList) {
                if (citizen.getPositionX() >= 0 && citizen.getPositionX() < 64 && citizen.getPositionY() == 721 + 32) {
                    noPlaceForCitizen = true;
                }
            }

            for (City city : citiesList) {
                if (!city.isUnderAttack() && !city.isDestroyed()) {
                    havePlaceToGo = true;
                }
            }

            if (!noPlaceForCitizen && havePlaceToGo) {
                for (Citizen citizen : citizenList) {
                    citizen.setModifyingList(true);
                }

                citizenList.add(new Citizen(0, 721 + 32, "citizennio" + citizenOverall, "surname" + citizenOverall, Citizen.randomCity()));
                citizenSelected = false;
                populationInc();

                for (Citizen citizen : citizenList) {
                    citizen.setModifyingList(false);
                }
            }
            return;
        }

        // Dodawanie nowego superbohatera
        boolean canSendSuperhero = false;
        if (e.getX() >= 0 && e.getX() <= addCitizenIcon.getWidth() && e.getY() >= SCREEN_HEIGHT / 2 - addCitizenIcon.getHeight() / 2 + +addCitizenIcon.getHeight() + 3
                && e.getY() <= SCREEN_HEIGHT / 2 - addCitizenIcon.getHeight() / 2 + 3 + addCitizenIcon.getHeight() + addCitizenIcon.getHeight()) {
            int citiesUnderAttack = 0;
            int superHerosSendToSave = 0;

            for (City city : citiesList) {
                if (city.isUnderAttack()) {
                    citiesUnderAttack++;
                }
            }

            for (Superhero superhero : superheroList) {
                if (!superhero.isInCity() && superhero.getNextCity() != "Metropolis") {
                    superHerosSendToSave++;
                }
            }

            for (City city : citiesList) {
                if (city.isUnderAttack() && !city.isDestroyed() && superHerosSendToSave < citiesUnderAttack) {
                    canSendSuperhero = true;
                }
            }
            if (canSendSuperhero) {
                for (Superhero superhero : superheroList) {
                    if (superhero.isWaitingInCapitalCity()) {
                        superhero.setInCity(false);
                        superhero.setWaitInCapitalCity(false);
                        superhero.setNewPosition(2397, 1041);
                        return;
                    }
                }
            }
            return;
        }

        // Obsługa zaznaczonego cywila
        if (citizenSelected) {
            if (e.getX() >= citizenPause.getPositionX() && e.getX() <= citizenPause.getPositionX() + 31
                    && e.getY() >= citizenPause.getPositionY() && e.getY() <= citizenPause.getPositionY() + 31) {
                for (Citizen citizen : citizenList) {
                    if (citizen.getName() == selectedCitizen) {
                        citizen.changeMovingState();
                        break;
                    }
                }
                return;
            }

            if (e.getX() >= citizenDelete.getPositionX() && e.getX() <= citizenDelete.getPositionX() + 31
                    && e.getY() >= citizenDelete.getPositionY() && e.getY() <= citizenDelete.getPositionY() + 31) {
                for (Citizen citizen : citizenList) {
                    if (citizen.getName() == selectedCitizen) {
                        citizen.kill();
                        citizenSelected = false;
                        break;
                    }
                }
                return;
            }

            if (e.getX() >= citizenChangeCity.getPositionX() && e.getX() <= citizenChangeCity.getPositionX() + 31
                    && e.getY() >= citizenChangeCity.getPositionY() && e.getY() <= citizenChangeCity.getPositionY() + 31) {
                for (Citizen citizen : citizenList) {
                    if (citizen.getName() == selectedCitizen) {
                        citizen.setNextCity(Citizen.randomCity());
                        break;
                    }
                }
                return;
            }
        }

        // Sprawdzenie co aktualnie zaznaczono
        for (Citizen citizen : citizenList) {
            if (e.getX() >= citizen.getPositionX() - xOffset && e.getX() <= citizen.getPositionX() + 32 - xOffset
                    && e.getY() >= citizen.getPositionY() - yOffset && e.getY() <= citizen.getPositionY() + 32 - yOffset) {
                selectedCitizen = citizen.getName();
                citizenSelected = true;
                break;
            } else if (e.getX() >= citizenPanel.getPositionX() && e.getX() <= citizenPanel.getPositionX() + 165
                    && e.getY() >= citizenPanel.getPositionY() && e.getY() <= citizenPanel.getPositionY() + 158) {

            } else {
                citizenSelected = false;
            }
        }

        for (Villain villain : villainList) {
            if (e.getX() >= villain.getPositionX() - xOffset && e.getX() <= villain.getPositionX() + 32 - xOffset
                    && e.getY() >= villain.getPositionY() - yOffset && e.getY() <= villain.getPositionY() + 32 - yOffset) {
                selectedVillain = villain.getName();
                villainSelected = true;
                break;
            } else if (e.getX() >= citizenPanel.getPositionX() && e.getX() <= citizenPanel.getPositionX() + 165
                    && e.getY() >= citizenPanel.getPositionY() && e.getY() <= citizenPanel.getPositionY() + 158) {

            } else {
                villainSelected = false;
            }
        }

        for (Superhero superhero : superheroList) {
            if (e.getX() >= superhero.getPositionX() - xOffset && e.getX() <= superhero.getPositionX() + 32 - xOffset
                    && e.getY() >= superhero.getPositionY() - yOffset && e.getY() <= superhero.getPositionY() + 32 - yOffset) {
                selectedSuperhero = superhero.getName();
                superheroSelected = true;
                break;
            } else if (e.getX() >= citizenPanel.getPositionX() && e.getX() <= citizenPanel.getPositionX() + 165
                    && e.getY() >= citizenPanel.getPositionY() && e.getY() <= citizenPanel.getPositionY() + 158) {

            } else {
                superheroSelected = false;
            }
        }

        for (City city : citiesList) {
            if (e.getX() >= city.getPositionX() - xOffset && e.getX() <= city.getPositionX() + 192 - xOffset
                    && e.getY() >= city.getPositionY() - yOffset && e.getY() <= city.getPositionY() + 192 - yOffset
                    || e.getX() >= city.getPositionX() - xOffset && e.getX() <= city.getPositionX() + 500 - xOffset
                    && e.getY() >= city.getPositionY() - yOffset && e.getY() <= city.getPositionY() + 390 - yOffset
                    && city.getName() == "Metropolis") {
                selectedCity = city.getName();
                citySelected = true;
                break;
            } else if (e.getX() >= citizenPanel.getPositionX() && e.getX() <= citizenPanel.getPositionX() + 165
                    && e.getY() >= citizenPanel.getPositionY() && e.getY() <= citizenPanel.getPositionY() + 158) {

            } else {
                citySelected = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {

    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {

    }

    @Override
    public void mouseExited(MouseEvent e
    ) {

    }

    /**
     * New thread method. Add new villains in random time.
     */
    @Override
    public void run() {
        while (!gameOver) {
            try {
                Random randomNumber = new Random();
                int randomTime = randomNumber.nextInt(10) + 1;
                Thread.sleep(5000 * randomTime);

            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            for (City city : citiesList) {
                if (!city.isUnderAttack() && !city.isDestroyed()) {
                    villainList.add(new Villain(0, 721 + 32, "villainero" + villainOverall));
                    break;
                }
            }
        }
    }

    /**
     * Finalize game.
     */
    public void gameOver() {
        long finishTime = System.currentTimeMillis() - startTime;
        gameOver = true;
        world.setGameRunning(false);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
        }
        finalizeAllData();
        world.gameOver(finishTime, citizenOverall, villainOverall, superheroAlive);
    }

    private void finalizeAllData() {

        for (Citizen citizen : citizenList) {
            citizen.setModifyingList(true);
        }

        for (Villain villain : villainList) {
            villain.setModifyingList(true);
        }

        for (Superhero superhero : superheroList) {
            superhero.setModifyingList(true);
        }

        try {
            Thread.sleep(1000);

        } catch (InterruptedException ex) {
            Logger.getLogger(Simulation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        grassList.clear();
        treeList.clear();
        citizenList.clear();
        villainList.clear();
        roadList.clear();
        citiesList.clear();
        superheroList.clear();
        crossRoadList.clear();
        citiesExitList.clear();
        citiesEntranceList.clear();

    }

    /**
     * Checks what key we actually pressed.
     */
    public void tick() {
        if (input.up.isPressed()) {
            if (yOffset - 3 >= 0) {
                yOffset -= MOVEMENT_SPEED;
            }
        }

        if (input.down.isPressed()) {
            if (yOffset + 3 <= WORLD_HEIGHT - SCREEN_HEIGHT) {
                yOffset += MOVEMENT_SPEED;
            }
        }

        if (input.left.isPressed()) {
            if (xOffset - 3 >= 0) {
                xOffset -= MOVEMENT_SPEED;
            }
        }

        if (input.right.isPressed()) {
            if (xOffset + 3 <= WORLD_WIDTH - SCREEN_WIDTH) {
                xOffset += MOVEMENT_SPEED;
            }
        }

        if (input.escape.isPressed()) {
            gameOver = true;
            world.setGameRunning(false);
            try {
                Thread.sleep(20);

            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            finalizeAllData();
            WorldSimulation.setWindowFlag(0);
        }
    }

    //-------Getters-&-Setters-----------
    /**
     * Increment population number.
     */
    public void populationInc() {
        populationNumber++;
    }

    /**
     * Decrement population number.
     */
    public void populationDec() {
        populationNumber--;
    }

    /**
     * Increment citizens borned overall.
     */
    public static void citizenOverallInc() {
        citizenOverall++;
    }

    /**
     * Increment villains borned overall.
     */
    public static void villainOverallInc() {
        villainOverall++;
    }

    /**
     * Increment superheros borned overall.
     */
    public static void superheroAliveInc() {
        superheroAlive++;
    }

    /**
     * Decrement alive superheros number.
     */
    public static void superheroAliveDec() {
        superheroAlive--;
    }

    /**
     * Increment alive citizens number.
     */
    public static void citizensAliveInc() {
        citizensAlive++;
    }

    /**
     * Decrement citizens superheros number.
     */
    public static void citizensAliveDec() {
        citizensAlive--;
    }

    /**
     * Increment alive villains number.
     */
    public static void villainsAliveInc() {
        villainsAlive++;
    }

    /**
     * Decrement alive villains number.
     */
    public static void villainsAliveDec() {
        villainsAlive--;
    }

    public static int getCitizensAlive() {
        return citizensAlive;
    }
}
