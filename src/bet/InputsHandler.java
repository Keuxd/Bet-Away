package bet;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.TriggerListener;

import javafx.scene.input.KeyCode;

public class InputsHandler {
	protected static TriggerListener currentTrigger;
	
	protected static void initInput() {
		FXGL.onKeyDown(KeyCode.W, () -> {
			if(GameCore.currentScreen != -1) FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_button2.mp3"));
			switch(GameCore.currentScreen) {
				case 0 : Menu.up(); break;
				case 1 : InfoScreen.up(); break;
				case 3 : BetScreen.up(); break;
				case 4 : VersusMiniScreen.upValue(10); break;
			}
		});
		
		FXGL.onKey(KeyCode.UP, () -> {
			switch(GameCore.currentScreen) {
				case 4 : VersusMiniScreen.upValue(100); break;
			}
		});
		
		FXGL.onKey(KeyCode.DOWN, () -> {
			switch(GameCore.currentScreen) {
				case 4 : VersusMiniScreen.downValue(100); break;
			}
		});
		
		FXGL.onKeyDown(KeyCode.S, () -> {
			if(GameCore.currentScreen != -1) FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_button2.mp3"));
			switch(GameCore.currentScreen) {
				case 0 : Menu.down(); break;
				case 1 : InfoScreen.down(); break;				
				case 3 : BetScreen.down(); break;				
				case 4 : VersusMiniScreen.downValue(10); break;
			}
		});
		
		FXGL.onKeyDown(KeyCode.SPACE, () -> {
			if(GameCore.currentScreen != -1) FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_button3.mp3"));
			switch(GameCore.currentScreen) {
				case 0 : Menu.select(); break;
				case 1 : InfoScreen.select(); break;
				case 2 : CharScreen.buttonAction(); break;
				case 4 : VersusMiniScreen.startBet(); break;
				case 6 : ByeScreen.ok(); break;
			}
		});
		
		FXGL.onKeyDown(KeyCode.A, () -> {
			if(GameCore.currentScreen != -1) FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_button2.mp3"));
			switch(GameCore.currentScreen) {
				case 3 : 
				case 4 : BetScreen.left(); break;
			}
		});
		
		FXGL.onKeyDown(KeyCode.D, () -> {
			if(GameCore.currentScreen != -1) FXGL.getAudioPlayer().playSound(FXGL.getAssetLoader().loadSound("assets_sounds_button2.mp3"));
			switch(GameCore.currentScreen) {
				case 3 :
				case 4 : BetScreen.right(); break;
			}
		});
		
		FXGL.onKeyDown(KeyCode.ESCAPE, () -> {
			switch(GameCore.currentScreen) {
				case 3 : BetScreen.goBack(); break;
			}
		});
		
	}
}
