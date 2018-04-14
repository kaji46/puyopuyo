package player;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoNumber;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.FieldPoint;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;
import sp.AbstractSamplePlayer;

/*
 今降ってきているぷよと同じ色が一番上にあれば，そこに重ねるように配置する．
 ただし，このままだと配置したときに死んでしまうことがあるので，死なないように改良しよう．
 */

/**
 * 今降ってきているぷよと同じ色が一番上にあれば，そこに重ねるように配置する<br>
 * 
 * @author tori
 */
public class SamplePlayer06 extends AbstractSamplePlayer {

	@Override
	public Action doMyTurn() {

		/**
		 * 現在のフィールドの状況
		 */
		Field field = getMyBoard().getField();
		/**
		 * 今降ってきているぷよ
		 */
		Puyo puyo = getMyBoard().getCurrentPuyo();

		/**
		 * 最初actionは空っぽ
		 */
		Action action = null;

		/*
		 * 　全列を操作して今のぷよと同じ色を探す
		 */
		for (int i = 0; i < field.getWidth(); i++) {
			/**
			 * i列目の一番上の点を取得．<br>
			 * FieldPointはフィールド内の一点を示すクラス．<br>
			 * field.getTopPoint(列番号)で，指定列で一番上にあるぷよの座標を返す
			 */
			FieldPoint point = field.getTopPoint(i);
			if (point != null) {
				/**
				 * 一番上の点(point)にあるぷよの色を種類
				 */
				PuyoType puyoType = field.getPuyoType(point);
				/*
				 * puyo.getPuyoType(PuyoNumber.FIRST)でメインぷよの色(PuyoType)を返す
				 * puyo.getPuyoType(PuyoNumber.SECOND)でサブぷよの色(PuyoType)を返す
				 */
				if (puyoType == puyo.getPuyoType(PuyoNumber.FIRST)) {
					// 最初のぷよと同じ色だったら最初のぷよを下にする

					// TODO
					// このままだと高く積み過ぎて負ける可能性があるので，
					// 積んだときに負けてしまう場合は，置かないようにする．
					// System.out.println("height:"+field.getHeight());
					if (field.getTop(i) < 10) {

						action = new Action(PuyoDirection.UP, i);
						// for文を強制終了する
						break;
					}
				} else if (puyoType == puyo.getPuyoType(PuyoNumber.SECOND)) {
					// 最初のぷよと同じ色だったら最初のぷよを上にする

					// TODO
					// ただし，このままだと高く積み過ぎて負ける可能性があるので，
					// 積んだときに負けてしまう場合は，置かないようにする．
					// if(field.getTop(i) > ...)
					if (field.getTop(i) < 10) {
						action = new Action(PuyoDirection.DOWN, i);
						// for文を強制終了する
						break;
					}
				}
			}
		}

		if (action != null) {
			return action;
		} else {
			// 消せるところがなければ，DefaultのActionを返す
			action = getDefaultAction();
			return action;
		}
	}

	/**
	 * 特に配置する場所がなかった場合の基本行動
	 * 
	 * @return
	 */
	Action getDefaultAction() {
		// TODO 一番低い場所にぷよを配置するようにする
		// Sample04参照．

		Field field = getMyBoard().getField();
		int columnNum = 0;
		int minPuyoNum = field.getTop(0) + 1;
		for (int i = 0; i < field.getWidth(); i++) {
			// ここで，各列の高さを調べ，これまで一番低かった列よりも低ければ，columnNumをiにする．
			/**
			 * puyoNum=i列目のぷよの数としている．<br>
			 * field.getTop(列番号)で，指定列の一番上にあるぷよのy座標が返ってくる．
			 */
			int puyoNum = field.getTop(i) + 1;
			if (puyoNum < minPuyoNum) {
				// TODO
				// これまで一番ぷよが少なかった数より，i番目の列のぷよが少なかったら
				// minPuyoNumにi番目のぷよの数を入れて
				// 配置する列(columnNum)にiを指定する
				minPuyoNum = puyoNum;
				columnNum = i;
			}

		}
		Action action = new Action(PuyoDirection.DOWN, columnNum);
		return action;
	}

	public static void main(String args[]) {
		AbstractPlayer player1 = new SamplePlayer06();
		PuyoPuyo puyopuyo = new PuyoPuyo(player1);
		puyopuyo.puyoPuyo();
	}
}
