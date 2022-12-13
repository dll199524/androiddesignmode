package com.example.designmode.glide.cache;

import java.util.HashMap;

//哈希链表
public class LRU {

    //最近使用原则
    private HashMap<Integer, Node> map;
    private DoubleList cache;
    private int capacity;

    public LRU(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        cache = new DoubleList();
    }


    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        int val = map.get(key).val;
        put(key, val);//把节点提前
        return val;
    }

    public void put(int key, int val) {
        Node node = new Node(key, val);
        if (map.containsKey(key)) {
            cache.remove(map.get(key));
            cache.addFirst(node);
            map.put(key, node);
        } else {
            if (capacity == cache.size()) {
                Node last = cache.removeLast();
                map.remove(last.key);
            }
            cache.addFirst(node);
            map.put(key, node);
        }
    }


    static class Node {
        public int key, val;
        public Node next, pre;
        public Node (int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    static class DoubleList {
        Node head = null;
        public void addFirst(Node node) {}
        public void remove(Node node) {}
        public Node removeLast() {return null;}
        public int size() {return 0;}
    }
}
