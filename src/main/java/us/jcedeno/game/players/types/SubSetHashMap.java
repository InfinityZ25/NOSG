package us.jcedeno.game.players.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link ConcurrentHashMap} that holds (n+1) subsets of the original
 * EntrySet.
 */
public class SubSetHashMap<K, SquidParticipant> extends ConcurrentHashMap<K, SquidParticipant> {

    // Index 0 participant, index 1
    private List<List<SquidParticipant>> subsets;
    private Stack<Integer> idStack;
    private Integer currentId;

    public SubSetHashMap() {
        super();
        this.subsets = new ArrayList<>();
        this.idStack = new Stack<>();

    }

}
