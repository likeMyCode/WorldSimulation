package worldsimulation.map.cities;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import worldsimulation.Simulation;
import worldsimulation.map.MapItem;
import worldsimulation.map.characters.Citizen;
import worldsimulation.powersources.PowerSource;

public class City extends MapItem implements Runnable {

    private final String name;
    private int residentsNumber;
    private boolean cityDestroyed = false;
    private boolean underAttack = false;
    private ArrayList<PowerSource> powerSourceList = new ArrayList<>();
    private double powerPotentialMultipler = 0.35;

    /**
     * Constructor of the class. Sets all basic informations about city.
     *
     * @param positionX Start position X of the city.
     * @param positionY Start position Y of the city.
     * @param name Name of the city.
     * @param residentsNumber Number of residents in the city.
     */
    public City(int positionX, int positionY, String name, int residentsNumber) {
        super(positionX, positionY);
        this.name = name;
        this.residentsNumber = residentsNumber;

        Random randomNumber = new Random();
        int randomInteligence = randomNumber.nextInt(10) + 1;
        int randomStrength = randomNumber.nextInt(10) + 1;
        int randomSpeed = randomNumber.nextInt(10) + 1;
        int randomStamina = randomNumber.nextInt(10) + 1;
        int randomEnergy = randomNumber.nextInt(10) + 1;
        int randomFightAbility = randomNumber.nextInt(10) + 1;
        int numberOfPowerSources = randomNumber.nextInt(5) + 1;

        boolean[] nums = new boolean[6];

        for (int i = 0; i < numberOfPowerSources; i++) {
            int randomPowerSource;
            while (true) {
                randomPowerSource = randomNumber.nextInt(6);
                if (!nums[randomPowerSource]) {
                    nums[randomPowerSource] = true;
                    break;
                }
            }

            switch (randomPowerSource) {
                case 0:
                    powerSourceList.add(new PowerSource(0, "Red Saphire", "inteligence", randomInteligence));
                    break;
                case 1:
                    powerSourceList.add(new PowerSource(1, "Eye of the beholder", "strength", randomStrength));
                    break;
                case 2:
                    powerSourceList.add(new PowerSource(2, "Big Shrimp", "speed", randomSpeed));
                    break;
                case 3:
                    powerSourceList.add(new PowerSource(3, "Emerald Banglek", "stamina", randomStamina));
                    break;
                case 4:
                    powerSourceList.add(new PowerSource(4, "Yellow Gem", "energy", randomEnergy));
                    break;
                case 5:
                    powerSourceList.add(new PowerSource(5, "Cavebar Skull", "fightAbility", randomFightAbility));
                    break;
            }
        }
        new Thread(this).start();
    }

    /**
     * Increase power potential of all power source in the city.
     */
    private void increasePowerPotential() {
        for (PowerSource powerSource : powerSourceList) {
            powerSource.setPowerPotential(powerSource.getPowerPotential() + (int) (this.residentsNumber * powerPotentialMultipler));
        }
    }

    /**
     * Loses power potential in city, and gives it to the villain.
     *
     * @param powerPotentialID Power potential number.
     * @return Number of how many power potential was drained.
     */
    public int drainPowerPotential(int powerPotentialID) {
        int powerPotential = 0;
        for (PowerSource powerSource : powerSourceList) {
            if (powerSource.getID() == powerPotentialID) {
                powerPotential = powerSource.drainPowerPotential();
            }
        }
        return powerPotential;
    }

    /**
     *
     * @return Summarized number of all power potentials.
     */
    public int sumPowerPotential() {
        int sum = 0;
        for (PowerSource powerSource : powerSourceList) {
            sum += powerSource.getPowerPotential();
        }
        return sum;
    }

    /**
     * Destroy city.
     */
    public void destroy() {
        this.cityDestroyed = true;
        this.underAttack = false;
        for (Citizen citizen : Simulation.citizenList) {
            if (citizen.getHometown() == this.name) {
                citizen.changeHometown();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            increasePowerPotential();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @return The name of the city.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The residents number.
     */
    public int getResidentsNumber() {
        return residentsNumber;
    }

    /**
     * @param residentsNumber The residents number to set.
     */
    public void setResidentsNumber(int residentsNumber) {
        this.residentsNumber = residentsNumber;
    }

    /**
     *
     * @return If city is destroyed.
     */
    public boolean isDestroyed() {
        return this.cityDestroyed;
    }

    /**
     *
     * @return If city is under attack.
     */
    public boolean isUnderAttack() {
        return this.underAttack;
    }

    /**
     * Sets flag that city is under attack.
     */
    public void isAttacked() {
        this.underAttack = true;
    }

    /**
     * Sets flag that city is no more under attack.
     */
    public void isNotAttacked() {
        this.underAttack = false;
    }

    /**
     *
     * @return The list of power sources in the city.
     */
    public ArrayList<PowerSource> getPowerSourceList() {
        return this.powerSourceList;
    }
}
