package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.sun.javafx.scene.traversal.Direction;

import javafx.scene.control.Label;
import javafx.util.Duration;

public class BetArena {
	private static Character player1;
	private static Character player2;
	private static int bet;
	private static ArrayList<Entity> screenElements;
	private static FortuneMachine machine;
	
	protected static void init(Personality firstPlayer, Personality secondPlayer, int betVal, Direction direction){
		player1 = new Character(firstPlayer, Direction.RIGHT);
		player2 = new Character(secondPlayer, Direction.LEFT);
		screenElements = new ArrayList<Entity>();
		
		bet = betVal;

		screenElements.add(FXGL.entityBuilder().view("machine.png").scale(0.8,0.8).at(FXGL.getAppCenter().subtract(530,193.2)).buildAndAttach());
		updateScore();
		
		
		player1.setPosition(screenElements.get(0).getPosition().add(0,386.4-110.4));
			player1.attachToWorld();
			
		player2.setPosition(screenElements.get(0).getPosition().add(973,386.4-111.4));
			player2.attachToWorld();

		machine = new FortuneMachine(player1, player2);
		
		FXGL.run(() -> {
			machine.controller();
			FXGL.runOnce(() -> {
				updateScore();
			}, Duration.seconds(4.5));
			
		}, Duration.seconds(5),7);
		
		FXGL.runOnce(() -> {ByeScreen.init(player1.getCoins(),player2.getCoins(),bet,direction);}, Duration.seconds(40));
		
		
	}
	
	protected static void updateScore() {
		if(screenElements.size() == 2) {
			screenElements.get(1).removeFromWorld();
			screenElements.remove(1);
		}

		var label = new Label(String.valueOf(player1.getCoins() + " | " + player2.getCoins()));
			label.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(35.0));
			
		screenElements.add(FXGL.entityBuilder().view(label).at(screenElements.get(0).getPosition().add(-60,-140)).buildAndAttach());
	}
	
	protected static void selfDestruction() {
		for(Entity entity : screenElements)
			entity.removeFromWorld();
		
		screenElements.clear();
		
		player1.removeAllEntitiesFromWorld();
		player1 = null;
		
		player2.removeAllEntitiesFromWorld();
		player2 = null;
		
		machine = null;
	}
	protected static Character getPlayer(int index) {
		switch(index) {
			case 0 : return player1;
			case 1 : return player2;
			default : return null;
		}
	}
}


