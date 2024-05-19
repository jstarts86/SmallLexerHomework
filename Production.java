import java.util.List;

public class Production {
	NonTerminal leftHand;
	List<Symbol> rightHand;

	public Production(NonTerminal leftHand, List<Symbol> rightHand) {
		this.leftHand = leftHand;
		this.rightHand = rightHand;
	}
}
