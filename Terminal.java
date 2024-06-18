public class Terminal extends Symbol {
	Terminal(String name) {
		super(name);
	}
	public static final Terminal EOF = new Terminal("$");
	@Override
	public String toString() {
		return name;
	}
}
