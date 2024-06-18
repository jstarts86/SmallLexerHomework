public class TokenTerminal {
	private String token;
	private String terminal;

	public TokenTerminal(String token, String terminal) {
		this.token = token;
		this.terminal = terminal;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	@Override
	public String toString() {
		return getTerminal();
	}
}
