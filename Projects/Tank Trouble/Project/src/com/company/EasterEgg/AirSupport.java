package com.company.EasterEgg;

/**
 * Packages
 */

import com.company.Player;
import com.company.TextureReference;

/**
 * Imports
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * AirSupport class
 * <p>This class provides bombing run</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.3.2
 */
public class AirSupport {

    /**
     * Objects, Variables, Components, ...
     */
    private String airSupportID;                            // Identifier
    private final Player player;                            // Player obj
    private int x, y, xStart, yStart, xEnd, yEnd;           // Required coordinates
    private final int thrust = 15;                          // Thrust
    protected double rotation;                              // Rotation
    private BufferedImage icon;                             // Icon
    private BufferedImage target;                           // Target icon
    private boolean haveDropped = false;                    // Indicates whether bombs have been dropped or not
    private int bombCount = 5;                              // Number of bombs to drop
    public boolean isTransferred;                           // Indicates whether the obj is async with other players

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Object Constructor
     *
     * @param player Locked on player object
     */
    public AirSupport(Player player, int x, int y, int xStart, int yStart, int xEnd, int yEnd, double rotation) {

        this.player = player;
        this.x = x;
        this.y = y;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.rotation = rotation;
        this.airSupportID = "@airSupport" + System.currentTimeMillis();

        try {
            this.icon = ImageIO.read(new File(TextureReference.getPath("plane")));
        } catch (Exception ex) {
            this.icon = null;
            ex.printStackTrace();
        }
        try {
            this.target = ImageIO.read(new File(TextureReference.getPath("target")));
        } catch (Exception ex) {
            this.target = null;
            ex.printStackTrace();
        }
    }

    /**
     * Draws the player.getTank() & its tracks
     * <p>Note that tracks must be drawn before the player.getTank() itself in order to seem like real life</p>
     *
     * @param g2d Graphics2D
     */
    public void draw(Graphics2D g2d) {
        AffineTransform at = new AffineTransform();

        // Draw the player.getTank()
        at.translate(this.x, this.y);                                                                     // Translate icon
        at.rotate(-Math.PI / 2 + this.rotation);                                                    // Rotate icon

        // (Upward by default)
        double scale = 0.4d;
        at.scale(scale, scale);                                                                          // Scale icon
        at.translate(-this.icon.getWidth() / (float) 2, -this.icon.getHeight() / (float) 2);      // Translate (Again), for easier rotation around the center
        g2d.drawImage(this.icon, at, null);                                                         // Draw hall

        drawTarget(g2d);
    }

    /**
     * Draws the target icon on map
     *
     * @param g2d Graphics2D
     */
    private void drawTarget(Graphics2D g2d) {
        AffineTransform at = new AffineTransform();

        // Draw the player.getTank()
        at.translate(xStart + Math.abs(xStart - xEnd) / (float) 2, yStart + Math.abs(yStart - yEnd) / (float) 2);
        double scale = 0.1d;
        at.scale(scale, scale);                                                                          // Scale icon
        at.translate(-this.target.getWidth() / (float) 2, -this.target.getHeight() / (float) 2);
        g2d.drawImage(this.target, at, null);
    }

    /**
     * Moves the jet
     */
    public void move() {

        // Calculate velocity
        int dir = +1;
        int xVel = (int) (Math.cos(this.rotation) * this.thrust * dir);
        int yVel = (int) (Math.sin(this.rotation) * this.thrust * dir);

        // If we are set to move
        this.x += xVel;
        this.y += yVel;
    }

    /**
     * Indicates whether jet can drop the bombs or not
     *
     * @return Indicator
     */
    public boolean canDrop() {
        return ((x >= xStart && x <= xEnd) && (y >= yStart && y <= yEnd)) && !haveDropped;
    }

    /**
     * Drops the bombs
     */
    public void drop() {
        if (haveDropped) return;
        haveDropped = true;
    }

    /**
     * Exist condition
     *
     * @return Indicator
     */
    public boolean exit() {
        return !((player.getTank().getX() >= 0 && x <= screenSize.getWidth()) && (y >= 0 && y <= screenSize.getHeight()));
    }

    /**
     * Setter
     *
     * @param x Current X-Axis
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter
     *
     * @param y Current Y-Axis
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Setter
     *
     * @param xStart X-Axis starting point
     */
    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    /**
     * Setter
     *
     * @param yStart Y-Axis starting point
     */
    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    /**
     * Setter
     *
     * @param xEnd X-Axis end point
     */
    public void setxEnd(int xEnd) {
        this.xEnd = xEnd;
    }

    /**
     * Setter
     *
     * @param yEnd Y-Axis end point
     */
    public void setyEnd(int yEnd) {
        this.yEnd = yEnd;
    }

    /**
     * Setter
     *
     * @param bombCount Number of bombs
     */
    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    /**
     * Setter
     *
     * @param haveDropped Bombs have dropped or not
     */
    public void setHaveDropped(boolean haveDropped) {
        this.haveDropped = haveDropped;
    }

    /**
     * Getter
     *
     * @return X-Axis
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     *
     * @return X-Axis
     */
    public int getY() {
        return y;
    }

    /**
     * Getter
     *
     * @return Bomb count
     */
    public int getBombCount() {
        return bombCount;
    }

    /**
     * Getter
     *
     * @return player obj
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter
     *
     * @return X start
     */
    public int getxStart() {
        return xStart;
    }

    /**
     * Getter
     *
     * @return Y start
     */
    public int getyStart() {
        return yStart;
    }

    /**
     * Getter
     *
     * @return X end
     */
    public int getxEnd() {
        return xEnd;
    }

    /**
     * Getter
     *
     * @return Y end
     */
    public int getyEnd() {
        return yEnd;
    }

    /**
     * Getter
     *
     * @return Rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Getter
     *
     * @return ID
     */
    public String getAirSupportID() {
        return airSupportID;
    }

    /**
     * Setter
     *
     * @param airSupportID ID
     */
    public void setAirSupportID(String airSupportID) {
        this.airSupportID = airSupportID;
    }
}