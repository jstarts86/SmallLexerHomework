import java.util.HashMap;
import java.util.Map;

public class LRTable {

	Map<Integer, Map<String, Action>> table;

	public LRTable() {
		 table = new HashMap<>();
	}
	public void addAction(int state, String token, Action action) {
        table.computeIfAbsent(state, k -> new HashMap<>()).put(token, action);
    }

    public Action getAction(int state, String token) {
        return table.getOrDefault(state, new HashMap<>()).get(token);
    }

}
