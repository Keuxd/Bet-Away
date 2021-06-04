package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.sun.javafx.scene.traversal.Direction;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CharScreen {
	
	protected static ArrayList<Entity> screenElements = new ArrayList<Entity>();
	protected static ArrayList<Character> characters = new ArrayList<Character>();
	
	protected static void initMenu() {
		GameCore.currentScreen = 2;
		screenElements.add(Menu.genButton("Voltar", FXGL.getAppCenter().add(316, 264), new Point2D(154,85.5)));
		screenElements.add(FXGL.entityBuilder()
							.at(screenElements.get(0).getPosition().add(58,2))
							.scale(0.6,0.6)
							.view("buttonEffect.png")
							.buildAndAttach());
		
		var text = new Text("Personagens");
		text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(50.0));
		
		screenElements.add(FXGL.entityBuilder()
							.at(FXGL.getAppCenter().subtract(150,FXGL.getAppHeight() / 2.4))
							.view(text)
							.buildAndAttach()
						  );
		
		
		
		characters.add(new Character(Personality.COOPERADOR, Direction.RIGHT));
		characters.get(0).setPosition(new Point2D(55,182));
			screenElements.add(getTextEntityByPersonality(Personality.COOPERADOR));

		characters.add(new Character(Personality.COPIADOR, Direction.RIGHT));
		characters.get(1).setPosition(new Point2D(55,369));
			screenElements.add(getTextEntityByPersonality(Personality.COPIADOR));
		
		characters.add(new Character(Personality.DETETIVE, Direction.RIGHT));
		characters.get(2).setPosition(new Point2D(55,571));
			screenElements.add(getTextEntityByPersonality(Personality.DETETIVE));
			
		characters.add(new Character(Personality.RANCOROSO, Direction.RIGHT));
		characters.get(3).setPosition(new Point2D(683,260));
			screenElements.add(getTextEntityByPersonality(Personality.RANCOROSO));
		
		characters.add(new Character(Personality.TRAPACEIRO, Direction.RIGHT));
		characters.get(4).setPosition(new Point2D(683,453));
			screenElements.add(getTextEntityByPersonality(Personality.TRAPACEIRO));
		
		for(Character ch : characters) {
			ch.attachToWorld();
		}
	}
	
	private static Entity getTextEntityByPersonality(Personality personality) {
		var tempEntity = getEntityByPersonality(personality);
		
		var upperText = new Label(personality.toString());
		upperText.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(22.0));
		upperText.setStyle("-fx-text-fill: " + personality.getColor());
		
		var description = new Label(personality.getDescription());
		description.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(25.0));
		description.setStyle("-fx-text-fill: " + personality.getColor());
		
		switch(personality) {
			case COOPERADOR: {
				upperText.setTranslateX(tempEntity.getX() - 35.0);
				upperText.setTranslateY(tempEntity.getY() - 70.0);
				
				description.setTranslateX(tempEntity.getX() + 80.0);
				description.setTranslateY(tempEntity.getY() + 10.0);
				break;
			}
			
			case COPIADOR: {
				upperText.setTranslateX(tempEntity.getX() - 30.0);
				upperText.setTranslateY(tempEntity.getY() - 65.0);
				
				description.setTranslateX(tempEntity.getX() + 80.0);
				description.setTranslateY(tempEntity.getY() - 5.0);
				break;			
			}
			
			case DETETIVE: {
				upperText.setTranslateX(tempEntity.getX() - 15.0);
				upperText.setTranslateY(tempEntity.getY() - 65.0);
				
				description.setTranslateX(tempEntity.getX() + 80.0);
				description.setTranslateY(tempEntity.getY() - 5.0);
				break;	
			}
			
			case RANCOROSO: {
				upperText.setTranslateX(tempEntity.getX() - 35.0);
				upperText.setTranslateY(tempEntity.getY() - 80.0);
				
				description.setTranslateX(tempEntity.getX() + 80.0);
				description.setTranslateY(tempEntity.getY() - 10.0);
				break;
			}
			
			case TRAPACEIRO: { 
				upperText.setTranslateX(tempEntity.getX() - 35.0);
				upperText.setTranslateY(tempEntity.getY() - 60.0);
				
				description.setTranslateX(tempEntity.getX() + 80.0);
				description.setTranslateY(tempEntity.getY() - 5.0);
				
				break;
			}
			
			default: break;
		}
		
		return FXGL.entityBuilder()
					.view(upperText)
					.view(description)
					.buildAndAttach();
	}
	
	private static Entity getEntityByPersonality(Personality personality) {
		for(Character chr : characters) {
			if(chr.getPersonality() == personality) {
				return chr.getEntities().get(0);
			}
		}
		return null;
	}
	
	protected static void buttonAction() {
		GameCore.procTransition();
		selfDestruction();
		
		FXGL.runOnce(() -> {
			InfoScreen.initInfoScreen();
		}, Duration.seconds(1.5));
	}
	
	private static void selfDestruction() {
		FXGL.runOnce(() -> {
			for(Entity entity : screenElements) {
				entity.removeFromWorld();
			}
			screenElements.clear();
			
			for(Character chr : characters) {
				chr.removeAllEntitiesFromWorld();
			}
			characters.clear();
		}, Duration.seconds(1.5));
	}
}
