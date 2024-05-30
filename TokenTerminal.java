public class TokenTerminal {
	String token;
	String terminal;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenTerminal(String token, String terminal) {
		this.token = token;
		this.terminal = terminal;
	}

	public TokenTerminal(String terminal) {
		this.terminal = terminal;
		this.token = "";
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
}
