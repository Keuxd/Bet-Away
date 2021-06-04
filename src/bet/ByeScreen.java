package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.sun.javafx.scene.traversal.Direction;

import javafx.scene.text.Text;
import javafx.util.Duration;

public class ByeScreen {
	private static ArrayList<Entity> screenElements;
	
	protected static void init(int p1Coins,int p2Coins,int bet, Direction direction) {
		GameCore.currentScreen = 6;
		GameCore.money -= bet;
		screenElements = new ArrayList<Entity>();
		var text = new Text();
			text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(55));
		
		//better wins
		if((p1Coins > p2Coins && direction == Direction.LEFT) || (p2Coins > p1Coins && direction == Direction.RIGHT)) {
			text.setText("Você ganhou : " + bet*2 + " dinheiros");
			GameCore.money += bet*2;
		}//better draw
		else if(p2Coins == p1Coins) {
			GameCore.money += bet;
			text.setText("Empate, você ganhou 0 dinheiros");
			text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(55));
		}
		else { //better loses
			text.setText("Você perdeu -> " + bet + " dinheiros gastos");
			if(GameCore.money == 0) GameCore.money = 100;
		}

		
		screenElements.add(FXGL.entityBuilder().at(FXGL.getAppCenter().add(-380,-300)).view(text).buildAndAttach());
	}
	
	protected static void selfDestruction() {
		FXGL.runOnce(() -> {
			for(Entity entity : screenElements) {
				entity.removeFromWorld();
				BetArena.selfDestruction();
			}
			screenElements.clear();	
		}, Duration.seconds(1.5));
	}
	
	protected static void ok() {
		GameCore.procTransition();
		selfDestruction();
		
		
		FXGL.runOnce(() -> {
			Menu.initMenu();
		}, Duration.seconds(1.5));
	}
}
