import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMap<K, V> implements Iterable<HashMap.Entity> {

    private  static final int INIT_BUCKET_COUNT = 16;
    private static final double LOAD_FACTOR = 0.5;
    private int size = 0;
    private Bucket[] buckets;

    @Override
    public Iterator<HashMap.Entity> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<HashMap.Entity> {
        private int currentBucketIndex = 0;
        private Bucket.Node currentNode = null;

        public boolean hasNext() {
            // Проверяем, есть ли следующий элемент в текущем бакете
            if (currentNode != null && currentNode.next != null) {
                return true;
            }

            // Проверяем, есть ли следующий бакет
            for (int i = currentBucketIndex + 1; i < buckets.length; i++) {
                if (buckets[i].head != null) {
                    return true;
                }
            }

            return false;
        }

        public Entity next() {
            // Проверяем, есть ли следующий элемент в текущем бакете
            if (currentNode != null && currentNode.next != null) {
                currentNode = currentNode.next;
                return currentNode.value;
            }

            // Ищем следующий непустой бакет
            for (int i = currentBucketIndex + 1; i < buckets.length; i++) {
                if (buckets[i].head != null) {
                    currentBucketIndex = i;
                    currentNode = buckets[i].head;
                    return currentNode.value;
                }
            }

            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    class Entity{
        K key;
        V value;
    }

    class Bucket<K, V>{
        private Node head;
        class Node{
            Node next;
            Entity value;
        }

        public V add(Entity entity){
            Node node = new Node();
            node.value = entity;
            if (head == null) {
                head = node;
                return null;
            }
            Node currentNode = head;
            while (true){
                if (currentNode.value.key.equals(entity.key)) {
                    V buf = (V)currentNode.value.value;
                    currentNode.value.value = entity.value;
                    return buf;
                }
                if (currentNode.next != null) {
                    currentNode = currentNode.next;
                } else {
                    currentNode.next = node;
                    return null;
                }
            }
        }

        public V remove(K key){
            if (head == null){
                return null;
            }
            if (head.value.key.equals(key)){
                V buf = (V)head.value.value;
                head = head.next;
                return buf;
            } else {
                Node node = head;
                while (node.next != null){
                    if (node.next.value.key.equals(key)){
                        V buf = (V)node.next.value.value;
                        node.next = node.next.next;
                        return buf;
                    }
                    node = node.next;
                }
                return null;
            }
        }

        public V get(K key){
            Node node = head;
            while (node != null){
                if (node.value.key.equals(key)){
                    return (V)node.value.value;
                }
                node = node.next;
            }
            return null;
        }
    }

    private int calculateBucketIndex(K kay){
       return Math.abs(kay.hashCode()) % buckets.length;
    }

    private void recalculate(){
        Bucket<K, V>[] old = buckets;
        buckets = new Bucket[old.length * 2];
        for (int i = 0; i < old.length; i++){
            Bucket<K, V> bucket = old[i];
            if (bucket != null){
                Bucket.Node node = bucket.head;
                while (node != null){
                    put((K)node.value.key, (V)node.value.value);
                    node = node.next;
                }
            }
        }
    }

    public V put(K key, V value) {

        if (buckets.length * LOAD_FACTOR <= size){
            recalculate();
        }

        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null){
            bucket = new Bucket();
            buckets[index] = bucket;
        }
        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;
        V res = (V)bucket.add(entity);
        if (res == null) {
            size++;
        }
        return res;
    }

    public V remove(K key){
        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null){
            return null;
        }
        V res = (V)bucket.remove(key);
        if (res != null){
            size--;
        }
        return res;
    }

    public V get(K key){
        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket == null){
            return null;
        }
        return (V)bucket.get(key);
    }

    public HashMap(){
       buckets = new Bucket[INIT_BUCKET_COUNT];
    }
    public HashMap(int initCount){
        buckets = new Bucket[initCount];
    }
}
