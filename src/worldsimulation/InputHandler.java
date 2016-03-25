package worldsimulation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    /**
     * Constructor of the class, which add key listener to main project class.
     *
     * @param world Main class created object.
     */
    public InputHandler(WorldSimulation world) {
        world.addKeyListener(this);
    }

    public class Key {

        private int numTimesPressed = 0;
        private boolean pressed = false;

        /**
         * @return Number of how many times key was pressed.
         */
        public int getNumTimesPressed() {
            return numTimesPressed;
        }

        /**
         *
         * @return If key is actually pressed.
         */
        public boolean isPressed() {
            return pressed;
        }

        /**
         * Public method which is used to check if key is pressed right now.
         *
         * @param isPressed
         */
        public void toggle(boolean isPressed) {
            pressed = isPressed;

            if (isPressed) {
                numTimesPressed++;
            }
        }
    }

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key enter = new Key();
    public Key escape = new Key();

    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Method which define keys to which we respond.
     *
     * @param keyCode Code of the key.
     * @param isPressed Boolean value if key is actually pressed.
     */
    public void toggleKey(int keyCode, boolean isPressed) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            up.toggle(isPressed);
        }

        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            down.toggle(isPressed);
        }

        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            left.toggle(isPressed);
        }

        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            right.toggle(isPressed);
        }

        if (keyCode == KeyEvent.VK_ENTER) {
            enter.toggle(isPressed);
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            escape.toggle(isPressed);
        }
    }
}
