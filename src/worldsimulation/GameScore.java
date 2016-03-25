package worldsimulation;

public class GameScore {

    private String playerName;
    private long finishTime;
    private int citizensOverall;
    private int villainsOverall;
    private int superherosAlive;

    public GameScore(String playerName, long finishTime, int citizensOverall, int villainsOverall, int superherosAlive) {
        this.playerName = playerName;
        this.finishTime = finishTime / 1000;
        this.citizensOverall = citizensOverall;
        this.villainsOverall = villainsOverall;
        this.superherosAlive = superherosAlive;
    }
    
    public GameScore() {
        
    }

    /**
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return the finishTime
     */
    public long getFinishTime() {
        return this.finishTime;
    }

    /**
     * @param finishTime the finishTime to set
     */
    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * @return the citizensOverall
     */
    public int getCitizensOverall() {
        return citizensOverall;
    }

    /**
     * @param citizensOverall the citizensOverall to set
     */
    public void setCitizensOverall(int citizensOverall) {
        this.citizensOverall = citizensOverall;
    }

    /**
     * @return the villainsOverall
     */
    public int getVillainsOverall() {
        return villainsOverall;
    }

    /**
     * @param villainsOverall the villainsOverall to set
     */
    public void setVillainsOverall(int villainsOverall) {
        this.villainsOverall = villainsOverall;
    }

    /**
     * @return the superherosAlive
     */
    public int getSuperherosAlive() {
        return superherosAlive;
    }

    /**
     * @param superherosAlive the superherosAlive to set
     */
    public void setSuperherosAlive(int superherosAlive) {
        this.superherosAlive = superherosAlive;
    }
    
    
}
