package bet;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;

public class BetButton {
	
	// 0 -> button_long | 1 -> text | 2 -> player1 | 3 -> player2
	private Entity[] buttonElements;
	private Personality[] players;
	
	public BetButton(Point2D position, int i) {
		this.buttonElements = new Entity[4];
		this.players = new Personality[2];
		
		generateRandomPlayers();
		
//		if(i == 180) {
//			players[0] = Personality.COPIADOR;
//			players[1] = Personality.TRAPACEIRO;
//		}
//		if(i == 480) {
//			players[0] = Personality.RANCOROSO;
//			players[1] = Personality.RANCOROSO;
//		}
		
		
		//button
		buttonElements[0] = FXGL.entityBuilder()
								.anchorFromCenter()
								.view("button_long.png")
								.zIndex(1)
								.scale(0.6,0.6)
								.buildAndAttach();
		
		//text
		var text = new Label("VS");
		text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(50.0));
		
		buttonElements[1] = FXGL.entityBuilder()
								.view(text)
								.buildAndAttach();
		
		//player1
		buttonElements[2] = FXGL.entityBuilder()
								.scale(0.3,0.3)
								.view(players[0].getHatPath())
								.buildAndAttach();
		
		//player2
		var idk = FXGL.getAssetLoader().loadTexture(players[1].getHatPath());
		idk.setScaleX(-1);
		buttonElements[3] = FXGL.entityBuilder()
								.scale(0.3,0.3)
								.view(idk)
								.buildAndAttach();

		
		setPosition(position);
	}
		
	private void setPosition(Point2D position) {
		buttonElements[0].setPosition(position.subtract(210,54));
		buttonElements[1].setPosition(buttonElements[0].getPosition().add(175,22));
		
		Point2D adjustPosition = null;
		
		switch(players[0]) {
			case COOPERADOR: adjustPosition = new Point2D(45,35); break;
			case COPIADOR: adjustPosition = new Point2D(45,42); break;
			case DETETIVE: adjustPosition = new Point2D(45,31); break;
			case RANCOROSO: adjustPosition = new Point2D(45,35); break;
			case TRAPACEIRO: adjustPosition = new Point2D(50,40); break;
			default: break;
		}
		
		buttonElements[2].setPosition(buttonElements[0].getPosition().add(adjustPosition));
		
		switch(players[1]) {
			case COOPERADOR: adjustPosition = new Point2D(310,35); break;
			case COPIADOR: adjustPosition = new Point2D(330,40); break;
			case DETETIVE: adjustPosition = new Point2D(320,30); break;
			case RANCOROSO: adjustPosition = new Point2D(310,33); break;
			case TRAPACEIRO: adjustPosition = new Point2D(315,40); break;
			default: break;
		}
		
		buttonElements[3].setPosition(buttonElements[0].getPosition().add(adjustPosition));
	}
	
	private void generateRandomPlayers() {
		int temp;
		
		for(int i = 0; i < 2; i++) {
			temp = FXGLMath.random(0,4);
			switch(temp) {
				case 0 : players[i] = Personality.COOPERADOR; break;
				case 1 : players[i] = Personality.COPIADOR; break;
				case 2 : players[i] = Personality.DETETIVE; break;
				case 3 : players[i] = Personality.RANCOROSO; break;
				case 4 : players[i] = Personality.TRAPACEIRO; break;
			}
		}
	}
	
	protected void selfDestruction() {
		for(Entity entity : buttonElements) {
			entity.removeFromWorld();
		}
		
		buttonElements = null;
		players = null;
	}
	
	protected Entity[] getButtonElements() {
		return this.buttonElements;
	}
	
	protected Personality[] getPersonalities() {
		return this.players;
	}
	
	protected void setPersonalities(Personality p1, Personality p2) {
		this.players = new Personality[2];
		players[0] = p1;
		players[1] = p2;
	}
}
