package us.jcedeno.game.players.types;

import java.util.Map;
import java.util.Stack;

import us.jcedeno.game.players.objects.SquidParticipant;

public class DataStructure {

    private volatile Map<String, SquidParticipant> participants;
    private volatile Stack<Integer> stack = new Stack<>();

    SquidParticipant as(String key, SquidParticipant value) {
        participants.put(key, value);

        new SubSetHashMap<String, SquidParticipant>();

        return null;
    }

}
