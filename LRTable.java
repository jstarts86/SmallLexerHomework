import java.util.HashMap;
import java.util.Map;

public class LRTable {
    private Map<Integer, Map<String, Action>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;

    public LRTable() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
    }

    // Method to add ACTION entries
    public void addAction(int state, String token, Action action) {
        actionTable.computeIfAbsent(state, k -> new HashMap<>()).put(token, action);
    }

    // Method to get ACTION entries
    public Action getAction(int state, String token) {
        return actionTable.getOrDefault(state, new HashMap<>()).get(token);
    }

    // Method to add GOTO entries
    public void addGoto(int state, String nonTerminal, int nextState) {
        gotoTable.computeIfAbsent(state, k -> new HashMap<>()).put(nonTerminal, nextState);
    }

    // Method to get GOTO entries
    public Integer getGoto(int state, String nonTerminal) {
        return gotoTable.getOrDefault(state, new HashMap<>()).get(nonTerminal);
    }
}
