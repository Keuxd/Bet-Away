package bet;

public enum Personality {
	
	COPIADOR("hat_1.png",
			 "Olá! Eu começo sempre tentando cooperar\ne então vou ficar apenas copiando\na sua ultima jogada.",
			 "#4089DD;"),
	
	TRAPACEIRO("hat_2.png",
			"Tentarei sempre te passar a perna!\nAfinal os fortes comem os fracos!\n              \"Nhoc..Nhoc..\"",
			"#52537f;"),
		
	COOPERADOR("hat_3.png",
			"Sempre confiarei na sua indole!\nQue tal sermos melhores amigos ? <3",
			"#FF75FF;"),
	
	RANCOROSO("hat_4.png",
			"Mermão, escuta ai, vou começar cooperando..\ne continuar nessa, mas se me passarem a perna..\nEU VOU TE TRAPACEAR ATÉ O FIM.",
			"#efc701;"),
	
	DETETIVE("hat_5.png",
			"Analisarei adaptativamente!, vou cooperar e trapacear\nSe tu trapacear de volta vou agir como o 'Copiador'..\nSe tu não me trapacear agirei como o 'Trapaceiro'.\n              \"Elementar, meu caro watson.\"",
			"#f6b24c;");
	
	private String hatPath, description, color;
	
	Personality(String hatPath, String description, String color){
		this.hatPath = hatPath;
		this.description = description;
		this.color = color;
	}
	
	protected String getHatPath() {
		return hatPath;
	}
	
	protected String getDescription() {
		return description;
	}
	
	protected String getColor() {
		return color;
	}
}
