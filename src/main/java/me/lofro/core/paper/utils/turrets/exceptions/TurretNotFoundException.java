package me.lofro.core.paper.utils.turrets.exceptions;

public class TurretNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public TurretNotFoundException() {
        super("An error has ocurred, a turret could not be found for the given location.");
    }
}
