package worldsimulation.map;

public class MapItem {

    protected int positionX;
    protected int positionY;

    /**
     * Constructor of the class. Set start position of object.
     *
     * @param positionX Position X on the map of the new item.
     * @param positionY Position Y on the map of the new item.
     */
    public MapItem(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Set new position of object.
     *
     * @param positionX New Position X on the map of the object.
     * @param positionY New Position Y on the map of the object.
     */
    public void setNewPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     *
     * @return Actual position X of the object.
     */
    public int getPositionX() {
        return this.positionX;
    }

    /**
     *
     * @return Actual position Y of the object.
     */
    public int getPositionY() {
        return this.positionY;
    }

}
