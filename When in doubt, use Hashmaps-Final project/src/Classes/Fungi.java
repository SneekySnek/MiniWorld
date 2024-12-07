package Classes;

import Classes.Abstracts.Entity;
import Enums.EntityStatus;
import Interfaces.*;
import itumulator.executable.DisplayInformation;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.List;

/**
 * The Fungi class represents a fungi entity in a simulated world.
 * It extends the Entity class, implements NonBlocking and IPoisonous interfaces,
 * and is responsible for infecting carcasses in its vicinity.
 */
public class Fungi extends Entity implements NonBlocking, IPoisonous {
    private final int ageCap;

    /**
     * Constructs a new Fungi instance with default parameters.
     *
     * @param w The world service interface for the fungi.
     */
    public Fungi(IWorldService w) {
        super(w);
        this.di = new DisplayInformation(Color.red, "fungi");
        this.ageCap =  100;
    }

    /**
     * Constructs a new Fungi instance with the option to specify its size.
     *
     * @param w        The world service interface for the fungi.
     * @param isSmall  Indicates if the fungi is small.
     */
    public Fungi(IWorldService w, Boolean isSmall) {
        super(w);
        this.di = new DisplayInformation(Color.red, "fungi" + (isSmall ? "-small" : ""));
        this.ageCap = isSmall ? 75 : 100;
    }

    /**
     * Defines the behavior of the fungi in the world.
     *
     * @param world The world in which the fungi acts.
     */
    @Override
    public void act(World world){
        if(this.age > this.ageCap) return;
        age++;
        TryToInfect();
        if(this.age == this.ageCap) {
            worldService.Remove(this);
            logs.logEvent("Fungi has died");
        }
    }

    /**
     * Attempts to infect nearby entities, particularly focusing on carcasses.
     */
    public void TryToInfect()
    {
        List<IFauna> list = worldService.getSurroundingEntities(currentLocation, 3, IFauna.class);
        for (IFauna Entity : list)
            if(Entity.getStatus() == EntityStatus.CARCASS) {
                Entity.infect();
                logs.logEvent("Fungi has infected a carcass");
            }
    }
}
