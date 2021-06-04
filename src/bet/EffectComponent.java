package bet;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.TimerAction;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class EffectComponent extends Component{
	
	TimerAction a;
	Point2D thisPosition;
	
	@Override
	public void onAdded() {
		thisPosition = this.getEntity().getPosition();
		
		a = FXGL.run(() -> {
			this.getEntity().setPosition(thisPosition.add(-20,10));
			FXGL.run(() -> {
				this.getEntity().setPosition(thisPosition);
			}, Duration.seconds(0.5));
		}, Duration.seconds(1));
	}
	
	@Override
	public void onRemoved() {
		a.expire();
	}
}
