package sp;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;


abstract public class AbstractSamplePlayer extends AbstractPlayer {

	
	
	public AbstractSamplePlayer(String name) {
		super(name);
	}

	public AbstractSamplePlayer(){
		super("");
		setPlayerName(getClass().getSimpleName());
	}

	@Override
	public void initialize() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void inputResult() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
