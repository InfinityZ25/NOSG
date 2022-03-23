package us.jcedeno.game.players.types;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link ConcurrentHashMap} that holds (n+1) subsets of the original
 * EntrySet.
 */
public class SubSetHashMap<K, V, E> extends ConcurrentHashMap<K, V> {

    private List<K> subsets;
    private Stack<E> idStack;

    public SubSetHashMap() {
        super();
    }

    @Override
    public V put(K key, V value) {


        return super.put(key, value);
    }

    @Override
    public V remove(Object key) {


        return super.remove(key);
    }

    

}
