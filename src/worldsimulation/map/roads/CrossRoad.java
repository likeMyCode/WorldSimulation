package worldsimulation.map.roads;

import java.util.concurrent.Semaphore;
import worldsimulation.map.MapItem;

public class CrossRoad extends MapItem {

    protected int movingDir;
    protected int nextCrossRoad;
    private String[] citiesList;
    public Semaphore sem = new Semaphore(1, true);

    /**
     * Constructor of the class. Sets basic informations about crossroad.
     * @param positionX The start position X of the crossroad.
     * @param positionY The start position Y of the crossroad.
     * @param citiesList Cities list to which this crossroads goes.
     */
    public CrossRoad(int positionX, int positionY, String[] citiesList) {
        super(positionX, positionY);
        this.citiesList = citiesList;
    }

    /**
     * 
     * @return Moving direction.
     */
    public int getMovingDir() {
        return movingDir;
    }

    /**
     * 
     * @return Direction where next crossroad is.
     */
    public int getNextCrossRoad() {
        return nextCrossRoad;
    }

    /**
     * 
     * @return Cities to which this crossroads goes.
     */
    public String[] getCitiesList() {
        return this.citiesList;
    }
}
