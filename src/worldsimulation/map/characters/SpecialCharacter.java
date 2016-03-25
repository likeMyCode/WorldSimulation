package worldsimulation.map.characters;

public abstract class SpecialCharacter extends Character {

    protected int health;
    protected int inteligence;
    protected int strength;
    protected int speed;
    protected int stamina;
    protected int energy;
    protected int fightAbility;
    protected boolean isFighting = false;
    protected int maxHealth;

    /**
     * Constructor of the class. Sets all of the basic informations about
     * object.
     *
     * @param positionX Sets start position X of the object.
     * @param positionY Sets start position Y of the object.
     * @param forename Sets forname of the object.
     * @param health Sets health of the object.
     * @param inteligence Sets inteligence of the object.
     * @param strength Sets strength of the object.
     * @param speed Sets speed of the object.
     * @param stamina Sets stamina of the object.
     * @param energy Sets energy of the object.
     * @param fightAbility Sets fight ability of the object.
     */
    public SpecialCharacter(int positionX, int positionY, String forename, int health, int inteligence, int strength, int speed, int stamina, int energy, int fightAbility) {
        super(positionX, positionY, forename);
        this.health = health;
        this.inteligence = inteligence;
        this.strength = strength;
        this.speed = speed;
        this.stamina = stamina;
        this.energy = energy;
        this.fightAbility = fightAbility;
    }

    /**
     * Method responsible of fight between superhero and villain.
     *
     * @param superhero Superhero reference.
     * @param villain Villain reference.
     */
    public static void fight(Superhero superhero, Villain villain) {
        villain.setFight(true);
        superhero.setFight(true);

        boolean endFight = false;
        int attacking; // 0 - superhero, 1 - villain
        int damage;
        if (superhero.getSpeed() > villain.getSpeed()) {
            attacking = 0;
        } else {
            attacking = 1;
        }

        while (!endFight) {
            switch (attacking) {
                case 0:
                    damage = superhero.getInteligence() * superhero.getFightAbility() - villain.getStamina();
                    if (damage > 0) {
                        villain.setHealth(villain.getHealth() - damage);
                    }

                    if (villain.getHealth() <= 0) {
                        villain.kill();
                        superhero.goCapitalCity();
                        endFight = true;
                    }

                    attacking = 1;
                    break;
                case 1:
                    damage = villain.getStrength() * villain.getFightAbility() - superhero.getStamina();
                    if (damage > 0) {
                        superhero.setHealth(superhero.getHealth() - damage);
                    }

                    if (superhero.getHealth() <= 0) {
                        superhero.kill();
                        endFight = true;
                    }

                    attacking = 0;
                    break;
            }
        }
        villain.setFight(false);
        superhero.setFight(false);
    }

    /**
     * @return The health of the object.
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health The health to set.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     *
     * @return Max health of the object.
     */
    public int getMaxHeath() {
        return this.maxHealth;
    }

    /**
     * @return The inteligence of the object.
     */
    public int getInteligence() {
        return inteligence;
    }

    /**
     * @param inteligence The inteligence to set.
     */
    public void setInteligence(int inteligence) {
        this.inteligence = inteligence;
    }

    /**
     * @return The strength of the object.
     */
    public int getStrength() {
        return strength;
    }

    /**
     * @param strength The strength to set.
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * @return The speed of the object.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed The speed to set.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return The stamina of the object.
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * @param stamina The stamina to set.
     */
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    /**
     * @return The energy of the object.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * @param energy The energy to set.
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * @return The fightAbility of the object.
     */
    public int getFightAbility() {
        return fightAbility;
    }

    /**
     * @param fightAbility The fightAbility to set.
     */
    public void setFightAbility(int fightAbility) {
        this.fightAbility = fightAbility;
    }

    /**
     *
     * @param isFighting Set if object is actually in fight.
     */
    public void setFight(boolean isFighting) {
        this.isFighting = isFighting;
    }

}
