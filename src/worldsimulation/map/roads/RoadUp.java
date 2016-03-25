package worldsimulation.map.roads;

public class RoadUp extends Road {

    /**
     * Constructor of the class. Sets basic informations about road.
     *
     * @param positionX The start position X of the road.
     * @param positionY The start position Y of the road.
     */
    public RoadUp(int positionX, int positionY) {
        super(positionX, positionY);
        this.movingDir = 1;
    }

}
