package worldsimulation.map.roads;

import worldsimulation.map.MapItem;

public class Road extends MapItem {

    protected int movingDir;

    /**
     * Constructor of the class. Sets basic informations about road.
     *
     * @param positionX The start position X of the road.
     * @param positionY The start position Y of the road.
     */
    public Road(int positionX, int positionY) {
        super(positionX, positionY);
    }

    /**
     *
     * @return The direction of the road.
     */
    public int getMovingDir() {
        return movingDir;
    }

}
