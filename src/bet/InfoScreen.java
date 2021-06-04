package bet;

import java.util.ArrayList;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class InfoScreen {
	
	protected static ArrayList<Entity> screenElements = new ArrayList<Entity>();
	private static int currentButton = 1;
	
	protected static void initInfoScreen() {
		GameCore.currentScreen = 1;
		screenElements.add(Menu.genButton("Voltar", FXGL.getAppCenter().add(316, 264), new Point2D(154,85.5)));		
		screenElements.add(Menu.genButton("Avançar", screenElements.get(0).getPosition().add(0,-110), new Point2D(145,85.5)));
		
		//selection effect
		screenElements.add(FXGL.entityBuilder()
				   .scale(0.6,0.6)
				   .view("buttonEffect.png")
				   .buildAndAttach()
			  );
		
		
		var leftText = new Text(getInfo());
		leftText.setFont(FXGL.getAssetLoader().loadFont("FuturaHandWritten.ttf").newFont(25.0));
		
		screenElements.add(FXGL.entityBuilder()
							   .view(leftText)
							   .at(28,32)
							   .buildAndAttach()
						  );
		
		updateButtonAction();
	}
	
	protected static void up() {
		if(currentButton + 1 > 1) return;
		currentButton++;
		updateButtonAction();
	}
	
	protected static void down() {
		if(currentButton - 1 < 0) return;
		currentButton--;
		updateButtonAction();
	}
	
	protected static void select() {
		switch(currentButton) {
			case 0 : {
				GameCore.procTransition();
				selfDestruction();
				
				FXGL.runOnce(() -> {
					Menu.initMenu();
				}, Duration.seconds(1.5));
				break;
			}
			case 1 : {
				GameCore.procTransition();
				selfDestruction();
				
				FXGL.runOnce(() -> {
					CharScreen.initMenu();
				}, Duration.seconds(1.5));
				
				break;
			}
		}
		
	}
	
	protected static void updateButtonAction() {
		screenElements.get(2).setPosition((screenElements.get(currentButton).getPosition().add(58,2)));
	}
	
	private static void selfDestruction() {
		FXGL.runOnce(() -> {
			for(Entity entity : screenElements) {
				entity.removeFromWorld();
			}
			screenElements.clear();
		}, Duration.seconds(1.5));
	}
	
	private static String getInfo() {
		return 		"Bem vindo ao 'Bet Away', um simples jogo de apostas de troca de moedas"
				+ "\nExatamente, você vai apostar em personagens trocando moedas!"
				+ "\nNesse mundo existe a máquina da fortuna, ela funciona de maneira bem simples"
				+ "\nSe duas pessoas colocam uma moeda cada, então a máquina vai ejetar 2 moedas pra cada um!"
				+ "\nEntão.. todos saem ganhando certo ?"
				+ "\nBem.. Sim! Porém estamos falando de 'pessoas', pessoas essas que tem suas personalidades e atitudes."
				+ "\nSe um lado da máquina estiver com dinheiro e o outro não, o lado que não tem dinheiro ganhará 3 moedas."
				+ "\nOu seja... Os espertinhos podem querer passar a perna nos outros!"
				+ "\nCada personagem tem uma personalidade, então analise bem suas apostas..."
				+ "\nPois um personagem mal intencionado pode ganhar de um personagem inocente! Faz sentido não ?"
				+ "\nMas não será sempre, afinal, erros podem acontecer não é mesmo ? Somos todos humanos !"
				+ "\nSendo assim, o cachorro do personagem pode pegar fogo, ou o seu peixinho Nemo pode engolir a moeda."
				+ "\nOu um botão misterioso pode punir vontades maldosas, Não importa !"
				+ "\nImprevistos acontecem! E eles podem acontecer na pior hora possivel..."
				+ "\nMas o que é a vida senão um grande jogo de probabilidades não é mesmo ?";
	}
	
}
