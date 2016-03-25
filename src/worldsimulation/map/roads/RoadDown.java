package worldsimulation.map.roads;

public class RoadDown extends Road {

    /**
     * Constructor of the class. Sets basic informations about road.
     *
     * @param positionX The start position X of the road.
     * @param positionY The start position Y of the road.
     */
    public RoadDown(int positionX, int positionY) {
        super(positionX, positionY);
        this.movingDir = 4;
    }

}
