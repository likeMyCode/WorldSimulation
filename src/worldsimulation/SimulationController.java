package worldsimulation;

import java.util.logging.Level;
import java.util.logging.Logger;
import static worldsimulation.Simulation.citiesList;
import worldsimulation.map.cities.City;

public class SimulationController implements Runnable {

    private Simulation simulation;
    
    /**
     * Constructor of the class. Which control when game has to end.
     * @param simulation Reference to simulation.
     */
    public SimulationController(Simulation simulation) {
        this.simulation = simulation;
        new Thread(this).start();
    }

    /**
     * Main thread method. Monitor if game needs to be finished.
     */
    @Override
    public void run() {
        boolean finishThat = false;
        while (!finishThat) {
            boolean canGameOver = true;
            for (City city : citiesList) {
                if (!city.isDestroyed()) {
                    canGameOver = false;
                    break;
                }
            }

            if (Simulation.getCitizensAlive() <= 0 && canGameOver) {
                finishThat = true;
                simulation.gameOver();
            }
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
