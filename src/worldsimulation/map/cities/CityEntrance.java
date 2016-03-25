package worldsimulation.map.cities;

import worldsimulation.map.MapItem;

public class CityEntrance extends MapItem {

    private String cityName;

    /**
     * The constructor of the class. Sets basic informations about city
     * entrance.
     *
     * @param positionX The start position X of the city entrance.
     * @param positionY The start position Y of the city entrance.
     * @param cityName The city to which this entrance is referred.
     */
    public CityEntrance(int positionX, int positionY, String cityName) {
        super(positionX, positionY);
        this.cityName = cityName;
    }

    /**
     *
     * @return Name of the city to which this entrance refers to.
     */
    public String getCityName() {
        return this.cityName;
    }

}
