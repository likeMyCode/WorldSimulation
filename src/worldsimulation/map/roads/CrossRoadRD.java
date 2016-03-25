package worldsimulation.map.roads;

public class CrossRoadRD extends CrossRoad {

    /**
     * Constructor of the class. Sets basic informations about crossroad.
     *
     * @param positionX The start position X of the crossroad.
     * @param positionY The start position Y of the crossroad.
     * @param citiesList Name of the cities where crossroad goes.
     */
    public CrossRoadRD(int positionX, int positionY, String[] citiesList) {
        super(positionX, positionY, citiesList);

        this.movingDir = 3;
        this.nextCrossRoad = 1;
    }

}
