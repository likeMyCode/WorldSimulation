package worldsimulation.map.characters;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import worldsimulation.Simulation;
import worldsimulation.map.cities.City;
import worldsimulation.map.cities.CityEntrance;
import worldsimulation.map.cities.CityExit;
import worldsimulation.map.roads.CrossRoad;
import worldsimulation.map.roads.Road;

public class Superhero extends SpecialCharacter implements Runnable {

    private String savingCity;
    private boolean waitInCapitalCity;
    private boolean isGoingToCapitalCity = false;

    /**
     * Constructor of the class. Sets all basic informations about superhero.
     *
     * @param positionX Set start position X.
     * @param positionY Set start position Y.
     * @param forename Set forename of the Superhero.
     */
    public Superhero(int positionX, int positionY, String forename) {
        super(positionX, positionY, forename, 0, 0, 0, 0, 0, 0, 0);

        Random randomNumber = new Random();
        int randomHealth = randomNumber.nextInt(500) + 1500;
        int randomInteligence = randomNumber.nextInt(15) + 1;
        int randomStrength = randomNumber.nextInt(15) + 1;
        int randomSpeed = randomNumber.nextInt(15) + 1;
        int randomStamina = randomNumber.nextInt(15) + 1;
        int randomEnergy = randomNumber.nextInt(15) + 1;
        int randomFightAbility = randomNumber.nextInt(15) + 1;

        this.setHealth(randomHealth);
        this.setInteligence(randomInteligence);
        this.setStrength(randomStrength);
        this.setSpeed(randomSpeed);
        this.setStamina(randomStamina);
        this.setEnergy(randomEnergy);
        this.setFightAbility(randomFightAbility);
        waitInCapitalCity = true;
        this.inCity = true;
        this.cityName = "Metropolis";
        this.movingDir = 2;
        Simulation.superheroAliveInc();
        this.modifyingList = false;
        this.maxHealth = this.health;

        new Thread(this).start();

    }

    /**
     * Control movement of the superhero. Enter city. Start fight with villain.
     */
    @Override
    public void move() {
        for (Road road : Simulation.roadList) {
            if (positionX == road.getPositionX() && positionY == road.getPositionY()) {
                this.movingDir = road.getMovingDir();
                break;
            }
        }

        for (CrossRoad cross : Simulation.crossRoadList) {
            if (positionX == cross.getPositionX() && positionY == cross.getPositionY()) {
                String[] citiesList = cross.getCitiesList();

                for (int i = 0; i < citiesList.length; i++) {
                    if (this.nextCity == citiesList[i]) {
                        this.movingDir = cross.getMovingDir();
                        break;
                    } else {
                        this.movingDir = cross.getNextCrossRoad();
                    }
                }
                break;
            }
        }

        for (CityEntrance cityEntrance : Simulation.citiesEntranceList) {
            if (positionX == cityEntrance.getPositionX() && positionY == cityEntrance.getPositionY()) {
                if (cityEntrance.getCityName() == "Metropolis") {
                    positionX = -32;
                    positionY = -32;
                    movingDir = 2;
                    waitInCapitalCity = true;
                    isGoingToCapitalCity = false;
                    return;
                }

                for (City city : Simulation.citiesList) {
                    if (cityEntrance.getCityName() == city.getName() && city.isDestroyed()) {
                        pickCityToSave();
                        for (CityExit cityExit : Simulation.citiesExitList) {
                            if (city.getName().equals(cityExit.getCityName())) {
                                this.setNewPosition(cityExit.getPositionX(), cityExit.getPositionY());
                                this.movingDir = cityExit.getMovingDir();
                                break;
                            }
                        }
                        return;
                    }
                }

                this.cityName = cityEntrance.getCityName();
                for (City city : Simulation.citiesList) {
                    if (this.cityName == city.getName()) {
                        savingCity = city.getName();
                        inCity = true;

                    }
                }
            }

        }

        for (Villain villain : Simulation.villainList) {
            if (this.movingDir == 1) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX - 32 == villain.getPositionX() && this.positionY == villain.getPositionY()) {
                    if (!this.isDead && !villain.isDead) {
                        SpecialCharacter.fight(this, villain);
                    }
                    return;
                }

            } else if (this.movingDir == 2) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX == villain.getPositionX() && this.positionY + 32 == villain.getPositionY()) {
                    if (!this.isDead && !villain.isDead) {
                        SpecialCharacter.fight(this, villain);
                    }
                    return;
                }

            } else if (this.movingDir == 3) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX == villain.getPositionX() && this.positionY - 32 == villain.getPositionY()) {
                    if (!this.isDead && !villain.isDead) {
                        SpecialCharacter.fight(this, villain);
                    }
                    return;
                }

            } else if (this.movingDir == 4) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX + 32 == villain.getPositionX() && this.positionY == villain.getPositionY()) {
                    if (!this.isDead && !villain.isDead) {
                        SpecialCharacter.fight(this, villain);
                    }
                    return;
                }
            }
        }

        switch (this.movingDir) {
            case 1:
                this.setNewPosition(positionX, positionY - MOVEMENT_SPEED);
                break;
            case 2:
                this.setNewPosition(positionX - MOVEMENT_SPEED, positionY);
                break;
            case 3:
                this.setNewPosition(positionX + MOVEMENT_SPEED, positionY);
                break;
            case 4:
                this.setNewPosition(positionX, positionY + MOVEMENT_SPEED);
                break;
        }
    }

    /**
     * Send superhero to capital city. (After won fight)
     */
    public void goCapitalCity() {
        this.nextCity = "Metropolis";
        this.isGoingToCapitalCity = true;
    }

    /**
     * Fight with villain in city.
     */
    private void inCity() {
        this.positionX = -32;
        this.positionY = -32;

        for (Villain villain : Simulation.villainList) {
            if (villain.getAttackingCity() == this.cityName) {
                SpecialCharacter.fight(this, villain);
                break;
            }
        }

        if (!this.isDead) {
            for (CityExit cityExit : Simulation.citiesExitList) {
                if (this.cityName.equals(cityExit.getCityName())) {
                    this.inCity = false;
                    this.setNewPosition(cityExit.getPositionX(), cityExit.getPositionY());
                    this.movingDir = cityExit.getMovingDir();
                    break;
                }
            }

            for (City city : Simulation.citiesList) {
                if (this.cityName.equals(city.getName())) {
                    city.isNotAttacked();
                }
            }
        }
    }

    /**
     * Pick next city which is attacked by villain, and needs to be defended.
     */
    private void pickCityToSave() {
        if (this.isGoingToCapitalCity) {
            this.nextCity = "Metropolis";
            return;
        }

        for (City city : Simulation.citiesList) {
            boolean cityIsGoingToBeSaved = false;
            if (city.isUnderAttack() && !city.isDestroyed()) {
                for (Superhero superhero : Simulation.superheroList) {
                    if (superhero.nextCity == city.getName() && superhero.getName() != this.getName()) {
                        cityIsGoingToBeSaved = true;
                        break;
                    }
                }

                if (!cityIsGoingToBeSaved) {
                    this.nextCity = city.getName();
                    break;
                }
            }
        }
    }

    /**
     * Kill superhero. Delete him from Array List.
     */
    @Override
    public void kill() {
        for (Superhero superhero : Simulation.superheroList) {
            superhero.modifyingList = true;
        }

        this.isDead = true;
        Simulation.superheroAliveDec();
        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
        }
        Simulation.superheroList.remove(this);

        for (Superhero superhero : Simulation.superheroList) {
            superhero.modifyingList = false;
        }
    }

    /**
     * Main thread method. Responsible of all of the superhero actions.
     */
    @Override
    public void run() {
        while (!isDead) {
            if (modifyingList || isFighting || waitInCapitalCity) {

            } else if (inCity) {
                inCity();
            } else {
                pickCityToSave();
                move();
            }
            try {
                Thread.sleep(4);
            } catch (InterruptedException ex) {
                Logger.getLogger(Superhero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param inCity Set if superhero is in city.
     */
    public void setInCity(boolean inCity) {
        this.inCity = inCity;
    }

    /**
     *
     * @param waitInCapitalCity Set if superhero need to wait in capital city.
     */
    public void setWaitInCapitalCity(boolean waitInCapitalCity) {
        this.waitInCapitalCity = waitInCapitalCity;
    }

    /**
     *
     * @return If superhero is waiting in capital city.
     */
    public boolean isWaitingInCapitalCity() {
        return this.waitInCapitalCity;
    }
}
