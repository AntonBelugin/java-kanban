package service;

import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    Node first;
    Node last;
    Map <Integer, Node> nodeMap = new HashMap<>();

    private static class Node {
        Task item;
        Node next;
        Node prev;
        Node (Node prev, Task element, Node next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
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
