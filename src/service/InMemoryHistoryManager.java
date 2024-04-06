package service;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    private Map<Integer, Node> nodeMap = new HashMap<>();

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(item, node.item) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, next, prev);
        }
    }

    @Override
    public void addHistoryView(Task task) {
        if (!(nodeMap.get(task.getId()) == null)) {
            removeNode(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void removeNode(int id) {
        Node curNode = nodeMap.get(id);
        if (curNode == null) {
            return;
        }
        if (curNode.equals(first) && (last == null)) {
            first = null;
            nodeMap.remove(id);
            return;
        }
        if (curNode.equals(first) && (curNode.next.equals(last))) {
            last.prev = null;
            first = last;
            last = null;
            nodeMap.remove(id);
            return;
        }
        if (curNode.equals(last) && (curNode.prev.equals(first))) {
            first.next = null;
            last = null;
            nodeMap.remove(id);
            return;
        }
        if (curNode.equals(last)) {
            curNode.prev.next = null;
            last = curNode.prev;
            nodeMap.remove(id);
            return;
        }
        if (curNode.equals(first)) {
            curNode.next.prev = null;
            first = curNode.next;
            nodeMap.remove(id);
            return;
        }
        curNode.prev.next = curNode.next;
        curNode.next.prev = curNode.prev;
        nodeMap.remove(id);
    }

    @Override
    public List<Task> getHistoryList() {
        List<Task> historyView = new ArrayList<>();
        Node curNode = first;
        while (!(curNode == null)) {
            historyView.add(curNode.item);
            curNode = curNode.next;
        }
        return historyView;
    }

    void linkLast(Task task) {
        if (first == null) {
            first = new Node(null, task, null);
            nodeMap.put(task.getId(), first);
        } else if (last == null) {
            last = new Node(first, task, null);
            first.next = last;
            nodeMap.put(task.getId(), last);
        } else {
            Node node = last;
            last = new Node(node, task, null);
            node.next = last;
            nodeMap.put(task.getId(), last);
        }
    }
}
