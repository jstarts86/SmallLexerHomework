public class NonTerminal extends Symbol {
	NonTerminal(String name) {
		super(name);
	}
	@Override
	public String toString() {
		return name;
	}
}
