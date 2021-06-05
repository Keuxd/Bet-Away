package bet;

import java.util.Map;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.image.ImageView;
import javafx.util.Duration;

/* 	TODO
 * 
 * - English option(current Portuguese Only)
 * - Shop
 * - Game Over
 * - Update CharScreen(detailed descriptions)
 * 
 */

public class GameCore extends GameApplication{
	
	//-1 -> Transition |0 -> Menu | 1 -> InfoScreen | 2 -> CharScreen | 3 -> BetScreen | 4 -> VersusScreen
	protected static int currentScreen;
	protected static int money = 100;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(1366);
		settings.setHeight(768);
		
		settings.setTitle("Bet Away");
		settings.setVersion("");
		settings.setAppIcon("hat_0.png");

		settings.setGameMenuEnabled(false);
		settings.setPreserveResizeRatio(true);
		settings.setFullScreenAllowed(true);
		settings.setFullScreenFromStart(true);
		
		settings.setFileSystemWriteAllowed(false);
		settings.setApplicationMode(ApplicationMode.RELEASE);
	}
	
	@Override
	protected void initGameVars(Map<String, Object> vars) {
		FXGL.getGameScene().setCursorInvisible();
	}
	
	@Override
	protected void initUI() {		
		Menu.initMenu();
	}
	
	@Override
	protected void initInput() {
		InputsHandler.initInput();
	}
	
	@Override
	protected void initGame() {
		FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("assets_sounds_bg_music.mp3"));
	}

	protected static void procTransition() {
		FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_scratch_in.mp3"));
		int oldCurrentScreen = currentScreen;
		currentScreen = -1;
		
		ImageView iv = new ImageView();
		FXGL.getAssetLoader().clearCache();
		iv.setImage(FXGL.getAssetLoader().loadImage("scratch_in.gif"));
		var transition = FXGL.entityBuilder()
			.view(iv)
			.zIndex(2)
			.buildAndAttach();
		
		FXGL.runOnce(() -> {
			transition.removeFromWorld();
			FXGL.getAssetLoader().clearCache();
			iv.setImage(FXGL.getAssetLoader().loadImage("scratch_out.gif"));
			var outTransition = FXGL.entityBuilder().view(iv).zIndex(2).buildAndAttach();
			
			FXGL.runOnce(() -> {
				outTransition.removeFromWorld();
			}, Duration.seconds(1.3));
			currentScreen = oldCurrentScreen;
			FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_scratch_out.mp3"));
		}, Duration.seconds(1.5));
	}	

}
