package worldsimulation.map.characters;

import worldsimulation.map.MapItem;

public abstract class Character extends MapItem {

    protected static final int MOVEMENT_SPEED = 1;  // Najlepiej, żeby pozostała jedynką, inne możliwe wartosći do wielokrotności 2 (2,4,8,16,32, ...)

    private final String forename;
    protected int movingDir = 1;
    protected boolean modifyingList = true;
    protected String nextCity;
    protected String cityName;
    protected boolean inCity = false;
    protected boolean isDead = false;

    /**
     * Constructor of the class. Creates new character.
     *
     * @param positionX Set start position X of the object.
     * @param positionY Set start position Y of the object.
     * @param forename Set forename of the object.
     */
    public Character(int positionX, int positionY, String forename) {
        super(positionX, positionY);
        this.forename = forename;
    }

    /**
     *
     * @return The name of the object.
     */
    public String getName() {
        return this.forename;
    }

    /**
     *
     * @return If object is actually in city.
     */
    public boolean isInCity() {
        return this.inCity;
    }

    /**
     *
     * @return Next city to which object is heading.
     */
    public String getNextCity() {
        return this.nextCity;
    }

    /**
     *
     * @return Direction which object is faced (1 - Top, 2 - Left, 3 - Right, 4
     * - Down)
     */
    public int getMovingDir() {
        return this.movingDir;
    }

    /**
     * Sets if Array List is actually modifying.
     *
     * @param modifyingList
     */
    public void setModifyingList(boolean modifyingList) {
        this.modifyingList = modifyingList;
    }

    public abstract void move();

    public abstract void kill();

}
