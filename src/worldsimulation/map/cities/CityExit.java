package worldsimulation.map.cities;

import worldsimulation.map.MapItem;

public class CityExit extends MapItem {

    private final String cityName;
    private final int movingDir;

    /**
     * The constructor of the class. Sets basic informations about city exit.
     *
     * @param positionX The start position X of the city exit.
     * @param positionY The start position Y of the city exit.
     * @param cityName The city to which this exit is referred.
     */
    public CityExit(int positionX, int positionY, String cityName, int movingDir) {
        super(positionX, positionY);
        this.cityName = cityName;
        this.movingDir = movingDir;
    }

    /**
     *
     * @return Name of the city to which this exit refers.
     */
    public String getCityName() {
        return this.cityName;
    }

    /**
     *
     * @return The direction where citizen should go after leaving city.
     */
    public int getMovingDir() {
        return this.movingDir;
    }
}
