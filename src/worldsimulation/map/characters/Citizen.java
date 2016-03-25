package worldsimulation.map.characters;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import worldsimulation.map.roads.Road;
import worldsimulation.Simulation;
import worldsimulation.map.cities.City;
import worldsimulation.map.cities.CityEntrance;
import worldsimulation.map.cities.CityExit;
import worldsimulation.map.roads.CrossRoad;

public class Citizen extends Character implements Runnable {

    private String surname;
    private String hometown;

    private boolean halt = false;
    private boolean wait = false;

    public int flagmovement = 1;

    /**
     * Constructor of the class. Sets start informations.
     *
     * @param positionX Set start position X.
     * @param positionY Set start position Y.
     * @param forename Set forename for the Citizen.
     * @param surname Set surname for the Citizen.
     * @param hometown Set hometown for the Citizen.
     */
    public Citizen(int positionX, int positionY, String forename, String surname, String hometown) {
        super(positionX, positionY, forename);
        this.surname = surname;
        this.hometown = hometown;
        goHometown();
        Simulation.citizenOverallInc();
        Simulation.citizensAliveInc();
        new Thread(this).start();
    }

    /**
     * Control movement of the citizen. - Checks if there is any other citizen
     * in front of him. - Checks if anybody else is on the crossroad. - Move on
     * normal roads. - Enter city.
     */
    @Override
    public synchronized void move() {

        // Sprawdzanie czy nie ma przed nim żadnego cywila (zatrzymywanie się)
        for (Citizen citizen : Simulation.citizenList) {
            switch (this.movingDir) {
                case 1:
                    wait = this.positionX == citizen.getPositionX() && this.positionY - 32 == citizen.getPositionY();
                    break;
                case 2:
                    wait = this.positionX - 32 == citizen.getPositionX() && this.positionY == citizen.getPositionY();
                    break;
                case 3:
                    wait = this.positionX + 32 == citizen.getPositionX() && this.positionY == citizen.getPositionY();
                    break;
                case 4:
                    wait = this.positionX == citizen.getPositionX() && this.positionY + 32 == citizen.getPositionY();
                    break;
            }
            if (wait) {
                break;
            }
        }

        // Ustalenie kierunku w którym zmierza dany cywil w zależności od drogi na której się znajduje
        for (Road road : Simulation.roadList) {
            if (positionX == road.getPositionX() && positionY == road.getPositionY()) {
                this.movingDir = road.getMovingDir();
                break;
            }
        }

        // Obsługa skrzyżowania (zastosowano mechanizm semaforów) 
        synchronized (this) {
            for (CrossRoad crossRoad : Simulation.crossRoadList) {
                switch (this.movingDir) {

                    case 1:
                        if (this.positionX == crossRoad.getPositionX() && this.positionY - 32 == crossRoad.getPositionY()) {
                            try {
                                for (CrossRoad restCrossRoad : Simulation.crossRoadList) {
                                    if (restCrossRoad.getPositionX() == crossRoad.getPositionX() - 32 && restCrossRoad.getPositionY() == crossRoad.getPositionY() - 32) {
                                        restCrossRoad.sem.acquire();
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                moveThroughCrossRoad();
                            } finally {
                                for (CrossRoad restCrossRoad : Simulation.crossRoadList) {
                                    if (restCrossRoad.getPositionX() == crossRoad.getPositionX() - 32 && restCrossRoad.getPositionY() == crossRoad.getPositionY() - 32) {
                                        restCrossRoad.sem.release();
                                    }
                                }
                            }
                        }
                        break;

                    case 2:
                        if (this.positionX - 32 == crossRoad.getPositionX() && this.positionY == crossRoad.getPositionY()) {
                            try {
                                for (CrossRoad restCrossRoad : Simulation.crossRoadList) {
                                    if (restCrossRoad.getPositionX() == crossRoad.getPositionX() - 32 && restCrossRoad.getPositionY() == crossRoad.getPositionY()) {
                                        restCrossRoad.sem.acquire();
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            try {
                                moveThroughCrossRoad();
                            } finally {
                                for (CrossRoad restCrossRoad : Simulation.crossRoadList) {
                                    if (restCrossRoad.getPositionX() == crossRoad.getPositionX() - 32 && restCrossRoad.getPositionY() == crossRoad.getPositionY()) {
                                        restCrossRoad.sem.release();
                                    }
                                }
                            }
                        }
                        break;

                    case 3:
                        if (this.positionX + 32 == crossRoad.getPositionX() && this.positionY == crossRoad.getPositionY()) {
                            try {
                                for (CrossRoad restCrossRoad : Simulation.crossRoadList) {
                                    if (restCrossRoad.getPositionX() == crossRoad.getPositionX() && restCrossRoad.getPositionY() == crossRoad.getPositionY() - 32) {
                                        restCrossRoad.sem.acquire();
                                    }
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                moveThroughCrossRoad();
                            } finally {
                                for (CrossRoad restCrossRoad : Simulation.crossRoadList) {
                                    if (restCrossRoad.getPositionX() == crossRoad.getPositionX() && restCrossRoad.getPositionY() == crossRoad.getPositionY() - 32) {
                                        restCrossRoad.sem.release();
                                    }
                                }
                            }
                        }
                        break;

                    case 4:
                        if (this.positionX == crossRoad.getPositionX() && this.positionY + 32 == crossRoad.getPositionY()) {
                            try {
                                crossRoad.sem.acquire();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                moveThroughCrossRoad();
                            } finally {
                                crossRoad.sem.release();
                            }
                        }
                        break;
                }
            }
        }

        // Wejście do miasta (jeżeli znajduje się w jego "bramie")
        for (CityEntrance cityEntrance : Simulation.citiesEntranceList) {
            if (positionX == cityEntrance.getPositionX() && positionY == cityEntrance.getPositionY()) {
                this.inCity = true;
                this.cityName = cityEntrance.getCityName();
                for (City city : Simulation.citiesList) {
                    if (this.cityName == city.getName()) {

                        if (city.isDestroyed()) {
                            this.kill();
                        }

                        city.setResidentsNumber(city.getResidentsNumber() + 1);
                        System.out.println("#" + city.getName() + " POPULATION INCREASE: " + city.getResidentsNumber());
                        break;
                    }
                }
            }
        }

        for (Villain villain : Simulation.villainList) {
            if (this.movingDir == 1) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX - 32 == villain.getPositionX() && this.positionY == villain.getPositionY()) {
                    if (!this.isDead) {
                        this.kill();
                    }
                    return;
                }

            } else if (this.movingDir == 2) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX == villain.getPositionX() && this.positionY + 32 == villain.getPositionY()) {
                    if (!this.isDead) {
                        this.kill();
                    }
                    return;
                }

            } else if (this.movingDir == 3) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX == villain.getPositionX() && this.positionY - 32 == villain.getPositionY()) {
                    if (!this.isDead) {
                        this.kill();
                    }
                    return;
                }

            } else if (this.movingDir == 4) {
                if (this.positionX == villain.getPositionX() && this.positionY == villain.getPositionY()
                        || this.positionX + 32 == villain.getPositionX() && this.positionY == villain.getPositionY()) {
                    if (!this.isDead) {
                        this.kill();
                    }
                    return;
                }
            }
        }

        // Jeżeli nie został zatrzymany przez innego cywila, to przejście o jednostkę do przodu w zadanym kierunku
        if (!wait) {
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
    }

    /**
     * Control movement on the crossroad.
     */
    public void moveThroughCrossRoad() {
        boolean ready = false;
        boolean waitSecond = false;
        int j = 0;
        while (j != 32) {
            if (!ready) {
                for (CrossRoad cross : Simulation.crossRoadList) {
                    if (positionX == cross.getPositionX() && positionY == cross.getPositionY()) {
                        String[] citiesList = cross.getCitiesList();

                        for (int i = 0; i < citiesList.length; i++) {
                            if (this.nextCity == citiesList[i]) {
                                this.movingDir = cross.getMovingDir();
                                ready = true;
                                break;
                            } else {
                                this.movingDir = cross.getNextCrossRoad();
                            }
                        }
                        break;
                    }
                }
            } else {
                for (Citizen citizen : Simulation.citizenList) {
                    switch (this.movingDir) {
                        case 1:
                            waitSecond = this.positionX == citizen.getPositionX() && this.positionY - 32 == citizen.getPositionY();
                            break;
                        case 2:
                            waitSecond = this.positionX - 32 == citizen.getPositionX() && this.positionY == citizen.getPositionY();
                            break;
                        case 3:
                            waitSecond = this.positionX + 32 == citizen.getPositionX() && this.positionY == citizen.getPositionY();
                            break;
                        case 4:
                            waitSecond = this.positionX == citizen.getPositionX() && this.positionY + 32 == citizen.getPositionY();
                            break;
                    }
                    if (waitSecond) {
                        break;
                    }
                }
                if (!waitSecond) {
                    j++;
                }
            }

            if (!waitSecond) {
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
            try {
                Thread.sleep(6);
            } catch (InterruptedException ex) {
                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Random some time to take a break. And after that take a journey.
     */
    private void inCity() {
        positionX = -32;
        positionY = -32;

        try {
            Random randomNumber = new Random();
            int randomTravelTime = randomNumber.nextInt(9) + 1;
            Thread.sleep(6000 * randomTravelTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (City city : Simulation.citiesList) {
            if (this.cityName.equals(city.getName()) && city.isUnderAttack()) {
                while (city.isUnderAttack()) {
                    try {
                        Random randomNumber = new Random();
                        int randomTravelTime = randomNumber.nextInt(9) + 1;
                        Thread.sleep(6000 * randomTravelTime);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        boolean readyToGo = false;

        while (!readyToGo) {
            for (City city : Simulation.citiesList) {
                if (!city.isUnderAttack() && !city.isDestroyed()) {
                    readyToGo = true;
                    break;
                }
            }
            try {
                Random randomNumber = new Random();
                int randomTravelTime = randomNumber.nextInt(9) + 1;
                Thread.sleep(2000 * randomTravelTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (CityExit cityExit : Simulation.citiesExitList) {
            if (this.cityName.equals(cityExit.getCityName())) {
                this.inCity = false;
                for (City city : Simulation.citiesList) {
                    if (this.cityName.equals(city.getName())) {
                        if (this.isDead) {
                            return;
                        }
                        city.setResidentsNumber(city.getResidentsNumber() - 1);
                        System.out.println("#" + city.getName() + " POPULATION DECREASE: " + city.getResidentsNumber());
                    }
                }
                this.setNewPosition(cityExit.getPositionX(), cityExit.getPositionY());
                this.movingDir = cityExit.getMovingDir();

                if (this.cityName == this.hometown) {
                    do {
                        this.nextCity = randomCity();
                    } while (this.cityName == this.nextCity);
                    System.out.println("BOHATER: " + surname + " WYBIERA SIĘ NA WYCIECZKĘ DO: " + nextCity);
                } else {
                    goHometown();
                    System.out.println("BOHATER: " + surname + " IDZIE DO HOMETOWN!");
                }
                break;
            }
        }
    }

    /**
     * Random next city.
     *
     * @return Next random city.
     */
    public static String randomCity() {
        Random randomNumber = new Random();
        String randomCityName = null;
        boolean thereIsPlaceToGo = false;
        for (City city : Simulation.citiesList) {
            if (!city.isUnderAttack() && !city.isDestroyed()) {
                thereIsPlaceToGo = true;
                break;
            }
        }

        if (thereIsPlaceToGo) {
            while (true) {
                int randomCityNumber = randomNumber.nextInt(Simulation.citiesList.size());
                randomCityName = Simulation.citiesList.get(randomCityNumber).getName();
                if (!Simulation.citiesList.get(randomCityNumber).isDestroyed() && !Simulation.citiesList.get(randomCityNumber).isUnderAttack()) {
                    break;
                }
            }
        } else {
            while (true) {
                int randomCityNumber = randomNumber.nextInt(Simulation.citiesList.size());
                randomCityName = Simulation.citiesList.get(randomCityNumber).getName();
                if (!Simulation.citiesList.get(randomCityNumber).isDestroyed()) {
                    break;
                }
            }
        }

        return randomCityName;
    }

    /**
     * Kill citizen, delete him from Array List and kill the thread.
     */
    @Override
    public void kill() {
        for (Citizen citizen : Simulation.citizenList) {
            citizen.modifyingList = true;
        }

        if (this.isInCity()) {
            for (City city : Simulation.citiesList) {
                if (this.nextCity == city.getName()) {
                    city.setResidentsNumber(city.getResidentsNumber() - 1);
                }
            }
        }

        this.inCity = false;
        this.isDead = true;
        Simulation.citizensAliveDec();
        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
        }
        Simulation.citizenList.remove(this);

        for (Citizen citizen : Simulation.citizenList) {
            citizen.modifyingList = false;
        }
    }

    /**
     * Main thread method, responsible of whole citizen actions.
     */
    @Override
    public void run() {
        while (!isDead) {
            synchronized (this) {
                if (modifyingList) {

                } else if (inCity) {
                    inCity();
                } else if (!halt) {
                    Random randomNumber = new Random();
                    int random = randomNumber.nextInt(50000) + 1;
                    if (random == 1) {
                        halt = true;
                    }
                    move();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Citizen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     *
     * @return Citizen's surname.
     */
    public String getSurname() {
        return this.surname;
    }

    /**
     *
     * @return Citizen's hometown.
     */
    public String getHometown() {
        return this.hometown;
    }

    /**
     *
     * @return If citizen is dead.
     */
    public boolean isDead() {
        return this.isDead;
    }

    /**
     *
     * @return If citizen is stopped by user.
     */
    public boolean isHalted() {
        return this.halt;
    }

    /**
     * Set next city, to which citizen is going.
     *
     * @param nextCity
     */
    public void setNextCity(String nextCity) {
        this.nextCity = nextCity;
    }

    /**
     *
     * @return City in which citizen actually is.
     */
    public String getCityName() {
        return this.cityName;
    }

    /**
     * Change citizen's moving state.
     */
    public void changeMovingState() {
        this.halt = !this.halt;
    }

    /**
     * Change citizen hometown. Used when his hometown is destroyed.
     */
    public void changeHometown() {
        this.hometown = randomCity();
    }

    /**
     * Change next citizen's city to his hometown.
     */
    public void goHometown() {
        this.nextCity = this.hometown;
    }

}
