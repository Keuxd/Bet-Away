package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.sun.javafx.scene.traversal.Direction;

import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Character {
	//entitiy -> n0 base | n1 -> hat | n2 -> eyes | n3 -> coin
	
	private ArrayList<Entity> entities;
	private Personality personality;
	private Direction direction;
	private int coins;
	
	public Character(Personality personality,Direction direction) {
		this.entities = new ArrayList<Entity>();
		this.personality = personality;
		this.direction = direction;
		
		coins = 0;
		
		genCharacter();
	}
	
	protected void genCharacter() {	
		
		//base
		var baseChar = FXGL.getAssetLoader().loadTexture("baseChar.png");
		if(direction == Direction.LEFT) baseChar.setScaleX(-1);
		
		entities.add(FXGL.entityBuilder()
						.scale(0.5,0.5)
						.view(baseChar)
						.build()
					  );
		
		//hat
		var hat = FXGL.getAssetLoader().loadTexture(personality.getHatPath());
		if(direction == Direction.LEFT) hat.setScaleX(-1);
		
		entities.add(FXGL.entityBuilder()
						.scale(0.5,0.5)
						.view(hat)
						.build()
					  );
		
		//eyes
		var eyes = FXGL.getAssetLoader().loadTexture("eyes_0.png");
		if(direction == Direction.LEFT) eyes.setScaleX(-1);
		
		entities.add(FXGL.entityBuilder()
						.scale(0.5,0.5)
						.view(eyes)
						.build()
					  );
	}
	
	protected void setPosition(Point2D position) {
		entities.get(0).setPosition(position);
		Point2D hatPosition = null;
		Point2D eyePosition = null;
		
		if(direction == Direction.RIGHT) {
			switch(personality) {
				case COPIADOR: hatPosition = new Point2D(-15,-32); break;
				case TRAPACEIRO: hatPosition = new Point2D(-9,-29); break;
				case DETETIVE: hatPosition = new Point2D(-11,-36); break;
				case RANCOROSO: hatPosition = new Point2D(-10,-54); break;
				case COOPERADOR: hatPosition = new Point2D(-22,-37); break;
			}
		
			eyePosition = new Point2D(27,33);
		}
		else {
			switch(personality) {
				case COPIADOR: hatPosition = new Point2D(24,-32); break;
				case TRAPACEIRO: hatPosition = new Point2D(11,-29); break;
				case DETETIVE: hatPosition = new Point2D(-3,-36); break;
				case RANCOROSO: hatPosition = new Point2D(3,-54); break;
				case COOPERADOR: hatPosition = new Point2D(-10,-37); break;
			}
	
			eyePosition = new Point2D(12,33);
		}
		entities.get(1).setPosition(entities.get(0).getPosition().add(hatPosition));
		entities.get(2).setPosition(entities.get(0).getPosition().add(eyePosition));
	}
	
	protected void removeAllEntitiesFromWorld() {
		for(Entity entity : this.entities) {
			entity.removeFromWorld();
		}
		entities = null;
	}
	
	protected void failAnimation() {
		genCoin();
		double x , y = 2;
		Point2D plateAdjust = null;
		
		if(direction == Direction.RIGHT) {
			x = 3.6;
			plateAdjust = new Point2D(-380,122);
		}
		else {
			x = -3.6;
			plateAdjust = new Point2D(250,122);
		}
		
		
		var plate = FXGL.entityBuilder().zIndex(-1).at(FXGL.getAppCenter().add(plateAdjust)).view("plate.png").buildAndAttach();
		
			FXGL.run(() -> {
				for(Entity entity : entities) {
					var stepIn = FXGL.run(() -> {entity.translate(x,-y);}, Duration.millis(10),6);

					//after the stepIn it steps out
					FXGL.runOnce(() -> {
						stepIn.expire();
						var stepOut = FXGL.run(() -> {entity.translate(x,y);}, Duration.millis(10), 6);
						FXGL.runOnce(() -> {stepOut.expire();}, Duration.millis(100));
					}, Duration.millis(100));
				}
			}, Duration.millis(200), 4);
			
			
			//eyes effect
			FXGL.runOnce(() -> {
				FXGL.run(() -> {
					entities.get(2).translateY(1);
				}, Duration.millis(20), 10);
			}, Duration.millis(800));
			
			//eyes effect
			FXGL.runOnce(() -> {
				FXGL.run(() -> {
					entities.get(2).translateY(-1);
				}, Duration.millis(20), 10);
			}, Duration.millis(900));
			
			
			FXGL.runOnce(() -> {
				entities.get(3).removeFromWorld();
				entities.remove(3);
				
				FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_button1.mp3"));
				if(direction == Direction.LEFT) setPosition(new Point2D(1155,455));
				else setPosition(new Point2D(124,457));
				plate.removeFromWorld();
				
				var text = new Text("?");
				text.setFont(FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf").newFont(55));
				var interroga = FXGL.entityBuilder().at(entities.get(0).getPosition().add(30,-45)).anchorFromCenter().view(text).buildAndAttach();
				FXGL.runOnce(()-> {interroga.removeFromWorld();}, Duration.seconds(1.5));
			}, Duration.seconds(0.9));
	}
	
	protected void drawAnimation() {
	baseWalk();
		
		//coin in
		FXGL.runOnce(() -> {
			//ida da coin
			FXGL.run(() -> {
				FXGL.run(() -> {
					if(direction == Direction.LEFT) entities.get(3).translate(-1,2);
					else entities.get(3).translate(1,2);
				}, Duration.millis(1),39);
			}, Duration.millis(200),1);
		}, Duration.seconds(1.25));
		
		//coin out
		FXGL.runOnce(() -> {
			FXGL.run(() -> {
				FXGL.run(() -> {
					if(direction == Direction.LEFT) entities.get(3).translate(2,-4);
					else entities.get(3).translate(-2,-4);
				}, Duration.millis(1),20);
			}, Duration.millis(200),2);
		}, Duration.seconds(1.65));
		
		//face
		FXGL.runOnce(() -> {
			entities.get(2).setVisible(false);
			var expression = FXGL.getAssetLoader().loadTexture("eyes_4.png");
			Point2D expressionAdjust = null;
			
			if(direction == Direction.LEFT) {
				expression.setScaleX(-1);
				expressionAdjust = new Point2D(12,31);
			}
			else expressionAdjust = new Point2D(20,31);
			
			entities.add(FXGL.entityBuilder().at(entities.get(0).getPosition().add(expressionAdjust)).zIndex(2).view(expression).scale(0.45,0.45).buildAndAttach());		
		}, Duration.seconds(2.15));
		
		FXGL.runOnce(() -> {baseMoonWalk();}, Duration.seconds(2.65));
		FXGL.runOnce(() -> {
			entities.get(3).removeFromWorld();
			entities.get(4).removeFromWorld();
				entities.remove(3);
				entities.remove(3);
			entities.get(2).setVisible(true);
		}, Duration.seconds(3.65));
	}
	
	protected void loseAnimation() {
		baseWalk();
		
		//coin in
		FXGL.runOnce(() -> {
			//ida da coin
			FXGL.run(() -> {
				FXGL.run(() -> {
					if(direction == Direction.LEFT) entities.get(3).translate(-1,2);
					else entities.get(3).translate(1,2);
				}, Duration.millis(1),39);
			}, Duration.millis(200),1);
		}, Duration.seconds(1.25));
		
		//badface
		FXGL.runOnce(() -> {
			entities.get(2).setVisible(false);
			var expression = FXGL.getAssetLoader().loadTexture("expression_1.png");
			Point2D expressionAdjust = null;
			
			if(direction == Direction.LEFT) {
				expression.setScaleX(-1);
				expressionAdjust = new Point2D(5,-3);
			}
			else expressionAdjust = new Point2D(5,-3);
			
			entities.add(FXGL.entityBuilder().at(entities.get(0).getPosition().add(expressionAdjust)).zIndex(2).view(expression).scale(0.45,0.45).buildAndAttach());	
			entities.get(3).removeFromWorld();
			entities.remove(3);
		}, Duration.seconds(2.15));
		
		FXGL.runOnce(() -> {baseMoonWalk();}, Duration.seconds(2.65));
		FXGL.runOnce(() -> {
			entities.get(3).setVisible(false);
			entities.get(3).removeFromWorld();
				entities.remove(3);
			entities.get(2).setVisible(true);
		}, Duration.seconds(3.65));
	}
	
	protected void goodWinAnimation() {
		baseWalk();
		
		//coin in
				FXGL.runOnce(() -> {
					//ida da coin
					FXGL.run(() -> {
						FXGL.run(() -> {
							if(direction == Direction.LEFT) entities.get(3).translate(-1,2);
							else entities.get(3).translate(1,2);
						}, Duration.millis(1),39);
					}, Duration.millis(200),1);
				}, Duration.seconds(1.25));		
		
				
				//badface
				FXGL.runOnce(() -> {
					entities.get(2).setVisible(false);
					var expression = FXGL.getAssetLoader().loadTexture("eyes_5.png");
					Point2D expressionAdjust = null;
					
					if(direction == Direction.LEFT) {
						expression.setScaleX(-1);
						expressionAdjust = new Point2D(11,18);
					}
					else expressionAdjust = new Point2D(15,22);
					
					entities.add(FXGL.entityBuilder().at(entities.get(0).getPosition().add(expressionAdjust)).zIndex(2).view(expression).scale(0.5,0.5).buildAndAttach());		
				}, Duration.seconds(2.15));
				
				FXGL.runOnce(() -> {
					baseMoonWalk();
					entities.get(3).removeFromWorld();
				}, Duration.seconds(2.65));
				FXGL.runOnce(() -> {
					entities.get(4).removeFromWorld();
					entities.remove(3);
					entities.remove(3);
					entities.get(2).setVisible(true);
				}, Duration.seconds(3.65));
	}
	
	protected void evilWinAnimation() {
		baseWalk();
		
		//coin in
		FXGL.runOnce(() -> {
			//ida da coin
			FXGL.run(() -> {
				FXGL.run(() -> {
					if(direction == Direction.LEFT) entities.get(3).translate(-1,2);
					else entities.get(3).translate(1,2);
				}, Duration.millis(1),39);
			}, Duration.millis(200),1);
		}, Duration.seconds(1.25));
		
		//coin out
		FXGL.runOnce(() -> {
			FXGL.run(() -> {
				FXGL.run(() -> {
					if(direction == Direction.LEFT) entities.get(3).translate(2,-4);
					else entities.get(3).translate(-2,-4);
				}, Duration.millis(1),20);
			}, Duration.millis(200),2);
		}, Duration.seconds(1.65));
		
		//badface
		FXGL.runOnce(() -> {
			entities.get(2).setVisible(false);
			var expression = FXGL.getAssetLoader().loadTexture("expression_2.png");
			Point2D expressionAdjust = null;
			
			if(direction == Direction.LEFT) {
				expression.setScaleX(-1);
				expressionAdjust = new Point2D(3,-10);
			}
			else expressionAdjust = new Point2D(3,-10);
			
			entities.add(FXGL.entityBuilder().at(entities.get(0).getPosition().add(expressionAdjust)).zIndex(2).view(expression).scale(0.45,0.45).buildAndAttach());
			FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_evil_laugh.mp3"));
		}, Duration.seconds(2.15));
		
		FXGL.runOnce(() -> {baseMoonWalk();}, Duration.seconds(2.65));
		FXGL.runOnce(() -> {
			entities.get(3).removeFromWorld();
			entities.get(4).removeFromWorld();
				entities.remove(3);
				entities.remove(3);
			entities.get(2).setVisible(true);
		}, Duration.seconds(3.65));
	}
	
	private void genCoin() {
		var coin = FXGL.getAssetLoader().loadTexture("coin.png");
		Point2D adjustPosition = new Point2D(55,-20);
		
		if(direction == Direction.LEFT) {
			 coin.setTranslateX(-1);
			 adjustPosition = new Point2D(-45,-20);
		}
		
		entities.add(FXGL.entityBuilder().at(entities.get(0).getPosition().add(adjustPosition)).scale(0.5,0.5).view(coin).zIndex(-1).buildAndAttach());
	}
	
	protected void baseMoonWalk() {
		double x , y = 2;
		
		if(direction == Direction.RIGHT) {x = 3.6;}
		else {x = -3.6;}
		
			var fullAnimation = FXGL.run(() -> {
				for(Entity entity : entities) {
					var stepIn = FXGL.run(() -> {entity.translate(-x,-y);}, Duration.millis(10),6);
	
					//after the stepIn it steps out
					FXGL.runOnce(() -> {
						var stepOut = FXGL.run(() -> {entity.translate(-x,y);}, Duration.millis(10),6);
					}, Duration.millis(100));
				}
			}, Duration.millis(200), 5);
	}
	
	protected void baseWalk() {
		genCoin();
		double x , y = 2;
		
		if(direction == Direction.RIGHT) {x = 3.6;}
		else {x = -3.6;}
		
			 FXGL.run(() -> {
				for(Entity entity : entities) {
					FXGL.run(() -> {entity.translate(x,-y);}, Duration.millis(10),6);

					//after the stepIn it steps out
					FXGL.runOnce(() -> {
						FXGL.run(() -> {entity.translate(x,y);}, Duration.millis(10), 6);
					}, Duration.millis(100));
				}
			}, Duration.millis(200), 5);
	}
	
	protected void attachToWorld() {
		for(Entity entity : entities) {
			FXGL.getGameWorld().addEntity(entity);
		}
	}
	
	protected ArrayList<Entity> getEntities() {
		return this.entities;
	}
	
	protected Direction getDirection() {
		return this.direction;
	}
	
	protected void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	protected Personality getPersonality() {
		return this.personality;
	}
	
	protected void setPersonality(Personality personality) {
		this.personality = personality;
	}
	
	protected void addCoins(int coins) {
		var moedas = this.coins + coins;
		this.coins = moedas;
	}
	
	protected void removeCoins(int coins) {
		var moedas = this.coins - coins;
		this.coins = moedas;
	}
	
	protected int getCoins() {
		return this.coins;
	}
}
