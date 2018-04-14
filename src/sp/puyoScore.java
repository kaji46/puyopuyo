package sp;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;


public class puyoScore implements Comparable<puyoScore>{
	int score;
	Action action;
	Field field;
	public puyoScore(int score,Action action,Field field){
		this.score=score;
		this.action=action;
		this.field=field;
	}
	public int getScore(){
		return score;
	}
	public Action getAction(){
		return action;
	}
	public Field getField(){
		return field;
	}
	@Override
	public int compareTo(puyoScore puyoscore) {
		// TODO 自動生成されたメソッド・スタブ
		puyoScore other = (puyoScore) puyoscore;
     
		return other.score-this.score;
	}
}