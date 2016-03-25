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

public class Villain extends SpecialCharacter implements Runnable {

    private String attackingCity = null;

    /**
     * Constructor of the class. Sets all of the basic informations about
     * villain.
     *
     * @param positionX Set start position X.
     * @param positionY Set start position Y.
     * @param forename Set forname of Villain.
     */
    public Villain(int positionX, int positionY, String forename) {
        super(positionX, positionY, forename, 0, 0, 0, 0, 0, 0, 0);

        Random randomNumber = new Random();
        int randomHealth = randomNumber.nextInt(200) + 600;
        int randomInteligence = randomNumber.nextInt(10) + 1;
        int randomStrength = randomNumber.nextInt(10) + 1;
        int randomSpeed = randomNumber.nextInt(10) + 1;
        int randomStamina = randomNumber.nextInt(10) + 1;
        int randomEnergy = randomNumber.nextInt(10) + 1;
        int randomFightAbility = randomNumber.nextInt(10) + 1;

        this.setHealth(randomHealth);
        maxHealth = this.health;
        this.setInteligence(randomInteligence);
        this.setStrength(randomStrength);
        this.setSpeed(randomSpeed);
        this.setStamina(randomStamina);
        this.setEnergy(randomEnergy);
        this.setFightAbility(randomFightAbility);
        pickNextCity();
        Simulation.villainOverallInc();
        Simulation.villainsAliveInc();
        this.modifyingList = false;

        new Thread(this).start();
    }

    /**
     * Controling movement of the villain. Enter city. Kill citizens if meet
     * them on the road.
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

        for (Citizen citizen : Simulation.citizenList) {
            if (this.movingDir == 1) {
                if (this.positionX == citizen.getPositionX() && this.positionY == citizen.getPositionY()
                        || this.positionX - 32 == citizen.getPositionX() && this.positionY == citizen.getPositionY()) {
                    if (!citizen.isDead) {
                        citizen.kill();
                    }
                    break;
                }

            } else if (this.movingDir == 2) {
                if (this.positionX == citizen.getPositionX() && this.positionY == citizen.getPositionY()
                        || this.positionX == citizen.getPositionX() && this.positionY + 32 == citizen.getPositionY()) {
                    if (!citizen.isDead) {
                        citizen.kill();
                    }
                    break;
                }

            } else if (this.movingDir == 3) {
                if (this.positionX == citizen.getPositionX() && this.positionY == citizen.getPositionY()
                        || this.positionX == citizen.getPositionX() && this.positionY - 32 == citizen.getPositionY()) {
                    if (!citizen.isDead) {
                        citizen.kill();
                    }
                    break;
                }

            } else if (this.movingDir == 4) {
                if (this.positionX == citizen.getPositionX() && this.positionY == citizen.getPositionY()
                        || this.positionX + 32 == citizen.getPositionX() && this.positionY == citizen.getPositionY()) {
                    if (!citizen.isDead) {
                        citizen.kill();
                    }
                    break;
                }
            }
        }

        for (Superhero superhero : Simulation.superheroList) {
            if (this.movingDir == 1) {
                if (this.positionX == superhero.getPositionX() && this.positionY == superhero.getPositionY()
                        || this.positionX - 32 == superhero.getPositionX() && this.positionY == superhero.getPositionY()) {
                    if (!superhero.isDead && !this.isDead) {
                        SpecialCharacter.fight(superhero, this);
                    }
                    return;
                }

            } else if (this.movingDir == 2) {
                if (this.positionX == superhero.getPositionX() && this.positionY == superhero.getPositionY()
                        || this.positionX == superhero.getPositionX() && this.positionY + 32 == superhero.getPositionY()) {
                    if (!superhero.isDead && !this.isDead) {
                        SpecialCharacter.fight(superhero, this);
                    }
                    return;
                }

            } else if (this.movingDir == 3) {
                if (this.positionX == superhero.getPositionX() && this.positionY == superhero.getPositionY()
                        || this.positionX == superhero.getPositionX() && this.positionY - 32 == superhero.getPositionY()) {
                    if (!superhero.isDead && !this.isDead) {
                        SpecialCharacter.fight(superhero, this);
                    }
                    return;
                }

            } else if (this.movingDir == 4) {
                if (this.positionX == superhero.getPositionX() && this.positionY == superhero.getPositionY()
                        || this.positionX + 32 == superhero.getPositionX() && this.positionY == superhero.getPositionY()) {
                    if (!superhero.isDead && !this.isDead) {
                        SpecialCharacter.fight(superhero, this);
                    }
                    return;
                }
            }
        }

        for (CityEntrance cityEntrance : Simulation.citiesEntranceList) {
            if (positionX == cityEntrance.getPositionX() && positionY == cityEntrance.getPositionY()) {
                this.cityName = cityEntrance.getCityName();
                for (City city : Simulation.citiesList) {
                    if (this.cityName == city.getName()) {
                        city.isAttacked();
                        attackingCity = city.getName();
                        inCity = true;
                    }
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
     * Choose next city to attack.
     */
    public void pickNextCity() {
        String nextAvailableCity = null;
        for (City city : Simulation.citiesList) {
            if (!city.isUnderAttack() && !city.isDestroyed()) {
                nextAvailableCity = city.getName();
                break;
            }
        }

        if (nextAvailableCity == null) {
            for (City city : Simulation.citiesList) {
                if (!city.isDestroyed()) {
                    nextAvailableCity = city.getName();
                    break;
                }
            }
        }

        this.nextCity = nextAvailableCity;

    }

    /**
     * Drain power potential from city.
     */
    private void drainPowerPotential() {
        for (City city : Simulation.citiesList) {
            if (this.attackingCity == city.getName()) {
                for (int powerPotentialID = 0; powerPotentialID < 6; powerPotentialID++) {
                    switch (powerPotentialID) {
                        case 0:
                            this.inteligence = this.inteligence + city.drainPowerPotential(powerPotentialID);
                            break;
                        case 1:
                            this.strength = this.strength + city.drainPowerPotential(powerPotentialID);
                            break;
                        case 2:
                            this.speed = this.speed + city.drainPowerPotential(powerPotentialID);
                            if (this.speed >= 15) {
                                this.speed = 14;
                            }
                            break;
                        case 3:
                            this.stamina = this.stamina + city.drainPowerPotential(powerPotentialID);
                            break;
                        case 4:
                            this.energy = this.energy + city.drainPowerPotential(powerPotentialID);
                            break;
                        case 5:
                            this.fightAbility = this.fightAbility + city.drainPowerPotential(powerPotentialID);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Starts attack on the city.
     */
    private void startCityAssault() {
        positionX = -32;
        positionY = -32;

        for (City city : Simulation.citiesList) {
            if (this.attackingCity == city.getName()) {
                for (Citizen citizen : Simulation.citizenList) {
                    if (citizen.isInCity() && citizen.getCityName() == this.attackingCity) {
                        citizen.kill();
                        break;
                    }
                }
            }
        }

        drainPowerPotential();

        for (City city : Simulation.citiesList) {
            if (this.attackingCity == city.getName()) {
                if (city.getResidentsNumber() <= 0 && city.sumPowerPotential() == 0) {
                    city.destroy();
                    stopCityAssault();
                    return;
                }
            }
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Villain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stop attack on they city.
     */
    private void stopCityAssault() {
        for (CityExit cityExit : Simulation.citiesExitList) {
            if (this.cityName.equals(cityExit.getCityName())) {
                this.inCity = false;
                this.attackingCity = "nothing";
                pickNextCity();
                this.setNewPosition(cityExit.getPositionX(), cityExit.getPositionY());
                this.movingDir = cityExit.getMovingDir();
                break;
            }
        }
    }

    /**
     * Kill villain. Delete him from the Array List.
     */
    @Override
    public void kill() {
        for (Villain villain : Simulation.villainList) {
            villain.modifyingList = true;
        }

        this.isDead = true;
        Simulation.villainsAliveDec();
        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
        }
        Simulation.villainList.remove(this);

        for (Villain villain : Simulation.villainList) {
            villain.modifyingList = false;
        }
    }

    /**
     * Main method reesponsible of all of the villains actions.
     */
    @Override
    public void run() {
        while (!isDead) {
            synchronized (this) {
                if (modifyingList || isFighting) {

                } else if (inCity) {
                    startCityAssault();
                } else {
                    pickNextCity();
                    move();
                }
            }
            try {
                Thread.sleep(4);
            } catch (InterruptedException ex) {
                Logger.getLogger(Villain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @return Actually attacking city.
     */
    public String getAttackingCity() {
        return this.attackingCity;
    }

}
