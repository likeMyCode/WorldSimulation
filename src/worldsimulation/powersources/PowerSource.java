package worldsimulation.powersources;

import worldsimulation.map.cities.City;

public class PowerSource {

    private final int id;
    private final String sourceName;
    private String abilityName;
    private int powerPotential;
    private final static int DRAIN_POWER = 1;

    /**
     * Constructor of the class. Sets basic informations of power source.
     *
     * @param id Unique ID of the power source,
     * @param sourceName Name of the power source.
     * @param abilityName Name of the ability.
     * @param powerPotential Power potential number.
     */
    public PowerSource(int id, String sourceName, String abilityName, int powerPotential) {
        this.id = id;
        this.sourceName = sourceName;
        this.abilityName = abilityName;
        this.powerPotential = powerPotential;
    }

    /**
     * Drain power potential. Gives it to villain and takes from city.
     *
     * @return Number of how many power potential was drained.
     */
    public int drainPowerPotential() {
        int drainedPotential;
        if (this.powerPotential < DRAIN_POWER) {
            drainedPotential = this.powerPotential;
            this.powerPotential = 0;
        } else {
            drainedPotential = DRAIN_POWER;
            this.powerPotential = this.powerPotential - DRAIN_POWER;
        }

        return drainedPotential;
    }

    /**
     *
     * @return The ability name.
     */
    public String getAbilityName() {
        return this.abilityName;
    }

    /**
     *
     * @return The power potential number.
     */
    public int getPowerPotential() {
        return this.powerPotential;
    }

    /**
     *
     * @return The unique power source ID.
     */
    public int getID() {
        return this.id;
    }

    /**
     *
     * @param powerPotential New power potential to set.
     */
    public void setPowerPotential(int powerPotential) {
        this.powerPotential = powerPotential;
    }
}
