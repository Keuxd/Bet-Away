package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.sun.javafx.scene.traversal.Direction;

import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BetScreen {
	private static int currentButton = 0;
	private static ArrayList<Entity> screenElements = new ArrayList<Entity>();
	private static ArrayList<BetButton> buttons = new ArrayList<BetButton>();
	
	protected static void init() {
		GameCore.currentScreen = 3;
		var text = new Label("Bets");
		text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(50.0));
		
		//text
		screenElements.add(FXGL.entityBuilder()
							.at(632.5,10)
							.anchorFromCenter()
							.viewWithBBox(text)
							.buildAndAttach()
						  );
		//BetButtons
		for(int i = 180; i <= 720; i += 150) {
			buttons.add(new BetButton(FXGL.getAppCenter().add(0,i-360),i));
		}
		
		//selectEffect
		screenElements.add(FXGL.entityBuilder()
							.scale(0.65,0.65)
							.view("button_long_effect.png")
							.buildAndAttach()
						  );
		
		var money = new Text(String.valueOf(GameCore.money));
			money.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(45));
		screenElements.add(FXGL.entityBuilder().at(30,60).view(money).buildAndAttach());
		
		updateButton();
	}
	
	protected static void up() {
		if(currentButton - 1 < 0) return;
		currentButton--;
		updateButton();
	}
	
	protected static void down() {
		if(currentButton + 1 > 3) return;
		currentButton++;
		updateButton();
	}
	
	protected static void left() {
		if(GameCore.currentScreen == 3) {
			VersusMiniScreen.popIn(Direction.LEFT, buttons.get(currentButton).getButtonElements()[0].getPosition());
			GameCore.currentScreen = 4;
		}
		else if(GameCore.currentScreen == 4 && VersusMiniScreen.getDirection() == Direction.RIGHT) {
			VersusMiniScreen.selfDestruction();
			GameCore.currentScreen = 3;
		}
	}
	
	protected static void right() {
		if(GameCore.currentScreen == 3) {
			VersusMiniScreen.popIn(Direction.RIGHT, buttons.get(currentButton).getButtonElements()[0].getPosition());
			GameCore.currentScreen = 4;
		}
		else if(GameCore.currentScreen == 4 && VersusMiniScreen.getDirection() == Direction.LEFT) {
			VersusMiniScreen.selfDestruction();
			GameCore.currentScreen = 3;
		}
	}
	
	private static void updateButton() {
		screenElements.get(1).setPosition(buttons.get(currentButton).getButtonElements()[0].getPosition().subtract(50,20));
		var a = (Text)screenElements.get(2).getViewComponent().getChildren().get(0);
			a.setText(String.valueOf(GameCore.money));
	}
	
	protected static void goBack() {
		GameCore.procTransition();
		selfDestruction();
		
		FXGL.runOnce(() -> {
			Menu.initMenu();
		}, Duration.seconds(1.5));
	}
	
	protected static void selfDestruction() {
		FXGL.runOnce(() -> {
			for(Entity entity : screenElements) {
				entity.removeFromWorld();
			}
			screenElements.clear();
			
			for(BetButton button : buttons) {
				button.selfDestruction();
			}
			buttons.clear();	
		}, Duration.seconds(1.5));
	}
	
	protected static BetButton getCurrentBetButton() {
		return buttons.get(currentButton);
	}
}
