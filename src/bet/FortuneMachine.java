package bet;

import com.almasb.fxgl.dsl.FXGL;
import com.sun.javafx.scene.traversal.Direction;

public class FortuneMachine {
	//0 -> P1 | 1 -> P2
	private int round;
	private Character players[];
	private int carma[];
	private BetAction lastBetAction[];
	private Personality volatilePersonality[];
	private boolean wasThereCoin[];
	private boolean isThereCoin[];
	
	
	public FortuneMachine(Character p1, Character p2) {
		round = 1;
		
		players = new Character[2];
		players[0] = p1;
		players[1] = p2;
		
		carma = new int[2];
		carma[0] = genCarma(players[0]);
		carma[1] = genCarma(players[1]);
		
		lastBetAction = new BetAction[2];
		
		volatilePersonality = new Personality[2];
			volatilePersonality[0] = p1.getPersonality();
			volatilePersonality[1] = p2.getPersonality();
			
		wasThereCoin = new boolean[2];
		isThereCoin = new boolean[2];
			
	}
	
	private int genCarma(Character player) {
		switch(player.getPersonality()) {
			case COOPERADOR: return 5;
			case COPIADOR: return 50;
			case DETETIVE: return 25;
			case RANCOROSO: return 80;
			case TRAPACEIRO: return 60;
		}
		return 0;
	}
	
	protected void controller() {
		wasThereCoin[0] = isThereCoin[0];
		wasThereCoin[1] = isThereCoin[1];
		switch(ifIf(players[0])) {
			case COOPERAR: {
				isThereCoin[0] = true;
				lastBetAction[0] = BetAction.COOPERAR;
				break;
			}
			
			case FALHAR: {
				isThereCoin[0] = false;
				lastBetAction[0] = BetAction.FALHAR;
				break;
			}
			
			case TRAPACEAR: {
				isThereCoin[0] = false;
				lastBetAction[0] = BetAction.FALHAR;
				break;
			}
		}
		
		switch(ifIf(players[1])) {
			case COOPERAR: {
				isThereCoin[1] = true;
				lastBetAction[1] = BetAction.COOPERAR;
				break;
			}
			
			case FALHAR: {
				isThereCoin[1] = false;
				lastBetAction[1] = BetAction.FALHAR;
				break;
			}
			
			case TRAPACEAR: {
				isThereCoin[1] = false;
				lastBetAction[1] = BetAction.FALHAR;
				break;
			}
		}
		round++;
	}
	
	protected int getDecision() {
		controller();
		if(isThereCoin[0] && isThereCoin[1]) return 3;
		if(!isThereCoin[0] && isThereCoin[1]) return 1;
		if(isThereCoin[0] && !isThereCoin[1]) return 2;
		if(!isThereCoin[0] && !isThereCoin[1]) return 0;
		return -1;
	}
	
	protected BetAction ifCooperador(Character player) {
		int carma;
		if(player.getDirection() == Direction.RIGHT) carma = this.carma[0];
			else carma = this.carma[1];
		
		if(FXGL.random(1,100) > carma) return BetAction.COOPERAR;
		else return BetAction.FALHAR;
	}
	
	protected BetAction ifCopiador(Character player) {
		if(round == 1) return BetAction.COOPERAR;
		
		//its player 1
		if(player.getDirection() == Direction.RIGHT) return lastBetAction[1];
		else return lastBetAction[0];
	}
	
	protected BetAction ifDetetive(Character player) {
		switch(round) {
			case 1: return BetAction.COOPERAR;
			case 2: return BetAction.TRAPACEAR;
			case 3: {
				if(player.getDirection() == Direction.RIGHT) {
					if(wasThereCoin[1]) {
						volatilePersonality[0] = Personality.TRAPACEIRO;
						return ifTrapaceiro(player);
					}
					else {
						volatilePersonality[0] = Personality.COPIADOR;
						return ifCopiador(player);
					}
				}
				else {
					if(wasThereCoin[0]) {
						volatilePersonality[1] = Personality.TRAPACEIRO;
						return ifTrapaceiro(player);
					}
					else {
						volatilePersonality[1] = Personality.COPIADOR;
						return ifCopiador(player);
					}
				}
			}
		}
		return BetAction.FALHAR;
	}
	
	protected BetAction ifRancoroso(Character player) {
		if(round == 1) return BetAction.COOPERAR;
		
		if(player.getDirection() == Direction.RIGHT) {
			if(wasThereCoin[1]) return BetAction.COOPERAR;
			else {
				volatilePersonality[0] = Personality.TRAPACEIRO;
				return BetAction.TRAPACEAR;
			}
		}
		else {
			if(wasThereCoin[0]) return BetAction.COOPERAR;
			else {
				volatilePersonality[0] = Personality.TRAPACEIRO;
				return BetAction.TRAPACEAR;
			}
		}
	}
	
	protected BetAction ifTrapaceiro(Character player) {
		int carma;
		
		if(player.getDirection() == Direction.RIGHT) carma = this.carma[0];
			else carma = this.carma[1];
		
		if(FXGL.random(1,100) < carma) return BetAction.FALHAR;
			else return BetAction.TRAPACEAR;
	}
	
	protected BetAction ifIf(Character player) {
		if(player.getDirection() == Direction.RIGHT) {
			switch(volatilePersonality[0]) {
				case COOPERADOR: return ifCooperador(player);
				case COPIADOR:	return ifCopiador(player);
				case DETETIVE:	return ifDetetive(player);
				case RANCOROSO:	return ifRancoroso(player);
				case TRAPACEIRO: return ifTrapaceiro(player);
			}
		}
		else {
			switch(volatilePersonality[1]) {
				case COOPERADOR: return ifCooperador(player);
				case COPIADOR:	return ifCopiador(player);
				case DETETIVE:	return ifDetetive(player);
				case RANCOROSO:	return ifRancoroso(player);
				case TRAPACEIRO: return ifTrapaceiro(player);
			}
		}
		return BetAction.FALHAR;
	}
	
	protected BetAction[] getLastBetAction() {
		return this.lastBetAction;
	}

}
