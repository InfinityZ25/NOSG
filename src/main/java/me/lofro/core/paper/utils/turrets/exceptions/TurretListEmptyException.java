package me.lofro.core.paper.utils.turrets.exceptions;

public class TurretListEmptyException extends Exception {
    private static final long serialVersionUID = 1L;

    public TurretListEmptyException() {
        super("Turret list is empty.");
    }
}
