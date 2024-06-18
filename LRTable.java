import java.util.HashMap;
import java.util.Map;

public class LRTable {
    private Map<Integer, Map<String, Action>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;

    public LRTable() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
    }

    public void addAction(int state, String token, Action action) {
        actionTable.computeIfAbsent(state, k -> new HashMap<>()).put(token, action);
    }

    public Action getAction(int state, String token) {
        return actionTable.getOrDefault(state, new HashMap<>()).get(token);
    }

    public void addGoto(int state, String nonTerminal, int nextState) {
        gotoTable.computeIfAbsent(state, k -> new HashMap<>()).put(nonTerminal, nextState);
    }

    public Integer getGoto(int state, String nonTerminal) {
        return gotoTable.getOrDefault(state, new HashMap<>()).get(nonTerminal);
    }

    public boolean hasShift(int state) {
        Map<String, Action> actions = actionTable.get(state);
        if (actions == null) return false;

        for (Action action : actions.values()) {
            if ("SHIFT".equals(action.actionType)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasReduce(int state) {
        Map<String, Action> actions = actionTable.get(state);
        if (actions == null) return false;

        for (Action action : actions.values()) {
            if ("REDUCE".equals(action.actionType)) {
                return true;
            }
        }
        return false;
    }
}
