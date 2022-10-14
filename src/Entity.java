import java.awt.*;
import java.util.*;

/**
 * Entity which move on the grid
 */
public class Entity {
    //starting position
    private final Position startPosition;

    //arrival position
    private final Position arrivalPosition;

    //current position
    private Position currentPosition;

    //optional previous position
    private Optional<Position> previousPosition;

    //unique ID
    private final int id;

    //entity color
    private Color entityColor;

    //positionManager which decides of the entity next move: move, die, revive or exit
    private final PositionManager positionManager;

    //has been killed
    private boolean kill;

    //once killed, dead for at least 2 rounds
    private int killTime;

    //when an entity arrived to its arrival position, it is destroyed
    private boolean destroyed;

    public Entity(Position startPosition, Position arrivalPosition, PositionManager positionManager, int id) {
        this.startPosition = startPosition;
        this.previousPosition = Optional.empty();
        this.currentPosition = startPosition;
        this.arrivalPosition = arrivalPosition;
        this.id = id;
        this.positionManager = positionManager;
        this.kill = false;
        this.destroyed = false;
        this.killTime = 0;
    }

    /**
     * Change the entity previous position with the current one, and the current one with the new one
     * @return an optional entity which might have been killed during the round
     */
    public Optional<Entity> move(){
        Position position = PositionManager.getNewPosition(this.currentPosition, this.arrivalPosition);
        if(!positionManager.isPositionTaken(position)) {
            positionManager.updatePositionOfEntity(this.currentPosition, position);
            this.previousPosition = Optional.of(currentPosition);
            this.currentPosition = position;
        }
        else
            return Optional.of(positionManager.manageConflict(this, position));

        return Optional.empty();
    }

    /**
     * Check if an entity has moved
     * @return true if the entity has moved, false otherwise
     */
    public boolean hasMoved() {
        return this.previousPosition.isPresent();
    }

    /**
     * Check is the entity reached its arrival position
     * @return true if the entity reached its arrival position, false otherwise
     */
    public boolean isArrived() {
        return this.currentPosition.equals(arrivalPosition);
    }

    /**
     * Check if an entity is destroyed
     * @return destroyed attribute boolean
     */
    public boolean isDestroyed() {
        return !destroyed;
    }

    /**
     * Check if an entity is killed
     * @return kill attribute boolean
     */
    public boolean isKilled(){
        return this.kill;
    }


    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Position getArrivalPosition() {
        return arrivalPosition;
    }

    public Optional<Position> getPreviousPosition() {
        return previousPosition;
    }

    public Color getEntityColor() {
        return entityColor;
    }

    public int getId() {
        return id;
    }

    public Position getStartPosition() {
        return this.startPosition;
    }

    public int getKillTime() {
        return this.killTime;
    }

    public void setEntityColor(Color color) {
        this.entityColor = color;
    }

    public void setDestroyed(boolean destroyed) {this.destroyed = destroyed;}

    public void setKill(boolean kill) {
        this.kill = kill;
    }

    public void resetKillTime() {
        this.killTime = 0;
    }

    public void resetCurrentPosition() {
        this.currentPosition = this.startPosition;
    }

    public void resetPreviousPosition() {
        this.previousPosition = Optional.empty();
    }

    public void incrementKillTime() {
        if(killTime == 2)
            killTime = 0;
        else
            this.killTime++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id && kill == entity.kill && killTime == entity.killTime && destroyed == entity.destroyed
                && Objects.equals(startPosition, entity.startPosition)
                && Objects.equals(arrivalPosition, entity.arrivalPosition)
                && Objects.equals(currentPosition, entity.currentPosition)
                && Objects.equals(previousPosition, entity.previousPosition)
                && Objects.equals(entityColor, entity.entityColor)
                && Objects.equals(positionManager, entity.positionManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, arrivalPosition, currentPosition, previousPosition, id, entityColor,
                positionManager, kill, killTime, destroyed);
    }

}
