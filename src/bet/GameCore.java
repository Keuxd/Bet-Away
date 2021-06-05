package bet;

import java.util.Map;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.image.ImageView;
import javafx.util.Duration;

/* A FAZER
 * 
 * - Começar o sistema principal(bets)
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

		settings.setGameMenuEnabled(false);
		settings.setPreserveResizeRatio(true);
		settings.setFullScreenAllowed(true);
		settings.setFullScreenFromStart(true);
		
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
//		FXGL.getUIFactoryService().registerFontFactory(FontType.UI, FXGL.getAssetLoader().loadFont("FuturaHandwritten.ttf"));
	}

	protected static void procTransition() {
		FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_scratch_in.mp3"));
		int oldCurrentScreen = currentScreen;
		currentScreen = -1;
		ImageView iv = new ImageView();
		FXGL.getAssetLoader().clearCache();
		iv.setImage(FXGL.getAssetLoader().loadImage("indo.gif"));
		
		var transition = FXGL.entityBuilder()
			.view(iv)
			.zIndex(2)
			.buildAndAttach();
		
		FXGL.runOnce(() -> {
			transition.removeFromWorld();
			FXGL.getAssetLoader().clearCache();
			iv.setImage(FXGL.getAssetLoader().loadImage("indo1.gif"));
			var outTransition = FXGL.entityBuilder().view(iv).zIndex(2).buildAndAttach();
			
			FXGL.runOnce(() -> {
				outTransition.removeFromWorld();
			}, Duration.seconds(1.3));
			currentScreen = oldCurrentScreen;
			FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_scratch_out.mp3"));
		}, Duration.seconds(1.5));
	}	

}
