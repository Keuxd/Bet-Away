package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Menu {
	protected static Entity topText;
	private static ArrayList<Entity> buttons;
	private static int currentButton = 0;
	
	protected static void initMenu() {
		var text = new Text("Bet Away");
		text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(100.0));
		buttons = new ArrayList<Entity>();
		
		topText = FXGL.entityBuilder()
				.at(FXGL.getAppCenter().subtract(222,280))
				.view(text)
				.buildAndAttach();
		
		//Apostar
		buttons.add(genButton("Apostar", FXGL.getAppCenter().add(-224,-232), new Point2D(140,85.5)));
		
		//Info
		buttons.add(genButton("Info", buttons.get(0).getPosition().add(0,175), new Point2D(175,87)));
		
		//Sair
		buttons.add(genButton("Sair", buttons.get(1).getPosition().add(0,175), new Point2D(175,87)));
		
		//Selection effect
		buttons.add(FXGL.entityBuilder()
						.view("buttonEffect.png")
						.scale(0.65,0.65)
						.buildAndAttach()
				   );
		
		GameCore.currentScreen = 0;
		updateButtonAction();
	}
	
	protected static Entity genButton(String text, Point2D position, Point2D textPosition) {
		var textFont = new Text(text);
		textFont.setTranslateX(textPosition.getX());
		textFont.setTranslateY(textPosition.getY());
		textFont.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(40.0));

		var buttonImage = FXGL.getAssetLoader().loadTexture("button1.png");
		buttonImage.setScaleX(0.6);
		buttonImage.setScaleY(0.6);
		
		return FXGL.entityBuilder()
				   .at(position)
				   .view(buttonImage)
				   .view(textFont)
				   .buildAndAttach();
	}
	
	private static void updateButtonAction() {
		buttons.get(3).setPosition(buttons.get(currentButton).getPosition().add(50,2));
	}
	
	protected static void up() {
		if(currentButton - 1 < 0) return;
		currentButton--;
		updateButtonAction();
	}
	
	protected static void down() {
		if(currentButton + 1 > 2) return;
		currentButton++;
		updateButtonAction();
	}
	
	protected static void select() {
		switch(currentButton) {
			case 0 : {
				//Iniciar apostas
				GameCore.procTransition();
				selfDestruction();
				
				FXGL.runOnce(() -> {
					BetScreen.init();
				}, Duration.seconds(1.5));
				break;
			}
			
			case 1 : {
				//Tela de Info
				GameCore.procTransition();
				selfDestruction();
				
				FXGL.runOnce(() -> {
					InfoScreen.initInfoScreen();
				}, Duration.seconds(1.5));
				break;
			}
			
			case 2 : {
				GameCore.procTransition();
				selfDestruction();
				FXGL.runOnce(() -> {
					FXGL.getGameController().exit();
				}, Duration.seconds(1.5));
				break;
			}
		}
	}
	
	protected static void selfDestruction() {
		FXGL.runOnce(() -> {
			for(Entity entity : buttons) {
				entity.removeFromWorld();
			}
			topText.removeFromWorld();
			
			topText = null;
			buttons.clear();
		}, Duration.seconds(1.5));
		
	}
}