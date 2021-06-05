package bet;

import com.almasb.fxgl.dsl.FXGL;

public class FortuneMachine {
	private int round;
	private Character[] players;
	private Personality[] volatilePersonality;
	private BetAction[] thisAction;
	private BetAction[] pastAction;
	private int[] carma;
	
	public FortuneMachine(Character p1, Character p2) {
		round = 1;
		
		players = new Character[2];
			players[0] = p1;
			players[1] = p2;
			
		volatilePersonality = new Personality[2];
			volatilePersonality[0] = p1.getPersonality();
			volatilePersonality[1] = p2.getPersonality();
			
		thisAction = new BetAction[2];
		pastAction = new BetAction[2];
		
		carma = new int[2];
		genCarma();
	}

	protected void genCarma() {
		for(int i = 0; i < 2;i++) {
			switch(volatilePersonality[i]) {
				case COOPERADOR: carma[i] = 10; break;				
				case COPIADOR: carma[i] = 30; break;				
				case DETETIVE: carma[i] = 40; break;				
				case RANCOROSO: carma[i] = 25; break;				
				case TRAPACEIRO: carma[i] = 50; break;				
				default:break;
			}
		}
	}
	
	protected void controller() {
		genAction();
		
		//line p1 cooperar
		if(thisAction[0] == BetAction.COOPERAR) {
			//p2 cooperar
			if(thisAction[1] == BetAction.COOPERAR) {
				for(int i = 0; i < 2 ; i++) {
					players[i].goodWinAnimation();
					players[i].addCoins(2);
				}
			}
			//p2 trapacear
			else if(thisAction[1] == BetAction.TRAPACEAR) {
				players[0].loseAnimation();
				players[1].evilWinAnimation();
				players[0].removeCoins(1);
				players[1].addCoins(3);
			}
			//p2 falhar
			else if(thisAction[1] == BetAction.FALHAR) {
				players[0].goodWinAnimation();
				players[1].failAnimation();
				players[0].addCoins(1);
				players[1].removeCoins(1);
			}
		}
		
		//line p1 trapacear
		else if(thisAction[0] == BetAction.TRAPACEAR) { 
			//p2 cooperar
			if(thisAction[1] == BetAction.COOPERAR) {
				players[0].evilWinAnimation();
				players[1].loseAnimation();
				players[0].addCoins(3);
				players[1].removeCoins(1);
			}
			//p2 trapacear
			else if(thisAction[1] == BetAction.TRAPACEAR) {
				for(int i = 0; i < 2; i++)
					players[i].drawAnimation();
			}
			//p2 falhar
			else if(thisAction[1] == BetAction.FALHAR) {
				players[0].evilWinAnimation();
				players[1].failAnimation();
				players[0].addCoins(1);
				players[1].removeCoins(1);
			}
		}
		//line p1 falhar
		else if(thisAction[0] == BetAction.FALHAR) {
			//p2 cooperar
			if(thisAction[1] == BetAction.COOPERAR) {
				players[0].failAnimation();
				players[1].goodWinAnimation();
				players[0].removeCoins(1);
				players[1].addCoins(2);
			}
			//p2 trapacear
			else if(thisAction[1] == BetAction.TRAPACEAR) {
				players[0].failAnimation();
				players[1].evilWinAnimation();
				players[0].removeCoins(1);
				players[1].addCoins(1);
			}
			//p2 falhar
			else if(thisAction[1] == BetAction.FALHAR) {
				for(int i = 0; i < 2; i++) {
					players[i].failAnimation();
					players[i].removeCoins(1);
				}
			}
		}
		pastAction[0] = thisAction[0];
		pastAction[1] = thisAction[1];
		round++;
	}
	
	protected void genAction() {
		for(int i = 0; i < 2; i++){
			switch(volatilePersonality[i]) {
				case COOPERADOR: thisAction[i] = actionCooperador(i); break;					
				case COPIADOR: thisAction[i] = actionCopiador(i); break;					
				case DETETIVE: thisAction[i] = actionDetetive(i); break;					
				case RANCOROSO: thisAction[i] = actionRancoroso(i); break;					
				case TRAPACEIRO: thisAction[i] = actionTrapaceiro(i); break;					
				default:break;	
			}
		}

	}
	
	protected BetAction actionCooperador(int player) {
		if(FXGL.random(1,100) <= carma[player]) return BetAction.FALHAR;
		return BetAction.COOPERAR;
	}
	
	protected BetAction actionTrapaceiro(int player) {
		if(FXGL.random(1,100) <= carma[player]) return BetAction.FALHAR;
		return BetAction.TRAPACEAR;
	}
	
	protected BetAction actionRancoroso(int player) {
		int enemyIndex = 0;
		if(player == 0) enemyIndex = 1;

		if(pastAction[enemyIndex] == BetAction.TRAPACEAR || pastAction[enemyIndex] == BetAction.FALHAR) {
			volatilePersonality[player] = Personality.TRAPACEIRO;
			return BetAction.TRAPACEAR;
		}
		
		if(FXGL.random(1,100) <= carma[player]) 
			return BetAction.FALHAR;
		
		else return BetAction.COOPERAR;
	}
	
	protected BetAction actionCopiador(int player) {
		int enemyIndex = 0;
		if(player == 0) enemyIndex = 1;
		
		if(FXGL.random(1,100) <= carma[player]) return BetAction.FALHAR;
		else {
			if(round == 1) return BetAction.COOPERAR;
			else {
				if(pastAction[enemyIndex] == BetAction.FALHAR || pastAction[enemyIndex] == BetAction.TRAPACEAR)
					return BetAction.TRAPACEAR;
			}
		}
		return BetAction.COOPERAR;
	}
	
	protected BetAction actionDetetive(int player) {
		int enemyIndex = 0;
		if(player == 0) enemyIndex = 1;
		
		//round 1~2 / analysis
		switch(round) {
			case 1 :  {
				if(players[enemyIndex].getPersonality() == Personality.COOPERADOR || players[enemyIndex].getPersonality() == Personality.COPIADOR)
					return BetAction.TRAPACEAR;
				else
					return BetAction.COOPERAR;
			}
			case 2 : {
				if(pastAction[player] == BetAction.COOPERAR) return BetAction.TRAPACEAR;
				else return BetAction.COOPERAR;
			}
		}
		
		//round 3+ / personality change
		if(pastAction[enemyIndex] == BetAction.COOPERAR) {
			volatilePersonality[player] = Personality.TRAPACEIRO;
			carma[player] = 65;
			return BetAction.TRAPACEAR;
		}
		else {
			volatilePersonality[player] = Personality.COPIADOR;
			if(pastAction[enemyIndex] == BetAction.FALHAR || pastAction[enemyIndex] == BetAction.TRAPACEAR) 
				return BetAction.TRAPACEAR;
		}
		return BetAction.COOPERAR;
	}
}
