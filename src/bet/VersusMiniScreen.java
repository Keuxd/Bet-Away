package bet;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.sun.javafx.scene.traversal.Direction;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class VersusMiniScreen {
	// 0 -> textBox | 1 -> value | 2 -> top arrow | 3 -> bottom arrow
	private static Entity[] miniScreenElements;
	private static Direction direction;
	private static int value = 0;
	
	protected static void popIn(Direction side, Point2D buttonReferencePosition) {
		miniScreenElements = new Entity[4];
		direction = side;
		
		var box = FXGL.getAssetLoader().loadTexture("caixinha.png");
		Point2D upArrowAdjust = null, downArrowAdjust =  null;
	
		if(side == Direction.LEFT) {
			buttonReferencePosition = buttonReferencePosition.add(-200,-32);
			box.setScaleX(-1);
			upArrowAdjust = new Point2D(51.5,30);
			downArrowAdjust = new Point2D(95.5,160);
		}
		else {
			buttonReferencePosition = buttonReferencePosition.add(465,-32);
			upArrowAdjust = new Point2D(56.5,30);
			downArrowAdjust = new Point2D(100.5,160);
		}
		
		
		miniScreenElements[0] = FXGL.entityBuilder().view(box).at(buttonReferencePosition).buildAndAttach();
		updateButton();
		
		miniScreenElements[2] = FXGL.entityBuilder().view("arrow.png").at(miniScreenElements[0].getPosition().add(upArrowAdjust)).buildAndAttach();
		miniScreenElements[3] = FXGL.entityBuilder().view("arrow.png").at(miniScreenElements[0].getPosition().add(downArrowAdjust)).rotate(180).buildAndAttach();
	}
	
	protected static void selfDestruction() {
		for(Entity entity : miniScreenElements) {
			entity.removeFromWorld();
		}
		
		miniScreenElements = null;
	}
	
	protected static void upValue(int extraValue) {
		if(value + extraValue > GameCore.money) value = 0;
		else value += extraValue;
		miniScreenElements[2].translateY(-5);
		updateButton();
		
		FXGL.runOnce(() -> {
			if(GameCore.currentScreen == -1) return;
			miniScreenElements[2].translateY(5);
		}, Duration.seconds(0.1));
	}
	
	protected static void downValue(int extraValue) {
		if(value - extraValue < 0) value = GameCore.money;
		else value -= extraValue;
		miniScreenElements[3].translateY(5);
		updateButton();
		
		
		FXGL.runOnce(() -> {
			if(GameCore.currentScreen == -1) return;
			miniScreenElements[3].translateY(-5);
		}, Duration.seconds(0.1));
	}
	
	protected static void startBet() {
		if(value == 0) return;
		
		var perso = BetScreen.getCurrentBetButton().getPersonalities();
		GameCore.procTransition();
		BetScreen.selfDestruction();
		selfDestruction();
		
		FXGL.runOnce(() -> {
			BetArena.init(perso[0], perso[1], value, direction);
			value = 0;
			GameCore.currentScreen = -1;
		}, Duration.seconds(1.5));
	}
	
	private static void updateButton() {
		if(miniScreenElements[1] != null) miniScreenElements[1].removeFromWorld();
		
		var label = new Label(String.valueOf(value));
		label.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(30.0));
		
		miniScreenElements[1] = FXGL.entityBuilder().view(label).at(miniScreenElements[0].getPosition().add(calcPointByMoney())).buildAndAttach();
	}
	
	private static Point2D calcPointByMoney() {
		if(direction == Direction.LEFT) {
			if(value < 10) return new Point2D(65,75);
			if(value < 100) return new Point2D(55,75);
			if(value < 1000) return new Point2D(45,75);
			if(value < 10000) return new Point2D(35,75);
			if(value < 100000) return new Point2D(30,75);
		}
		else {
			if(value < 10) return new Point2D(70,70);
			if(value < 100) return new Point2D(60,70);
			if(value < 1000) return new Point2D(50,70);
			if(value < 10000) return new Point2D(40,70);
			if(value < 100000) return new Point2D(35,70);
		}
		return new Point2D(0,0);
	}
	
	protected static Direction getDirection() {
		return direction;
	}
}
