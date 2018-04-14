package player;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoNumber;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;
import sp.AbstractSamplePlayer;

/*
 配置したときに同じ色のぷよと接する数が多い場所に落とすようにする．
 ただし，二番目のぷよについての計算が未完成なので，完成させよう
 */
/**
 * ぷよが同じ色と隣接するように配置する
 * 
 * @author tori
 */
public class SamplePlayer08 extends AbstractPlayer {

	public SamplePlayer08(String playerName) {
		super(playerName);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public Action doMyTurn() {

		/**
		 * 現在のフィールドの状況
		 */
		Field field = getMyBoard().getField();
		Field enemyField = getEnemyBoard().getField();
		Puyo puyo = getMyBoard().getCurrentPuyo();
		Puyo enemyPuyo = getEnemyBoard().getCurrentPuyo();
		/**
		 * 今降ってきているぷよ
		 */

		int PuyoNum = getPuyoNum(field);
		int maxDif = PuyoNum - getMinNextPuyoNum(field, puyo);
		PuyoDirection minDir = getMinNextPuyoDir(field, puyo);
		int minColumn = getMinNextPuyoColumn(field, puyo);

		/**
		 * 最初actionは空っぽ
		 */
		Action action = null;
		int enemyDif = getPuyoNum(enemyField)
				- getMinNextPuyoNum(enemyField, enemyPuyo);

		// TODO isEnableとgetNeighborPuyoNumを作る

		// if (getEnemyBoard().getTotalNumberOfOjama() > 0) {
		// if (minDir != null) {
		// action = new Action(minDir, minColumn);
		// System.out.println("発火");
		// }
		// }
		if (minDir == null) {
			action = getAction(field, puyo, PuyoNum);
		} else {
			if (maxDif > enemyDif) {

				action = new Action(minDir, minColumn);
				System.out.println("発火");

			} else if (PuyoNum > 40) {

				action = new Action(minDir, minColumn);
				System.out.println("まじやば");

			} else {
				action = getAction(field, puyo, PuyoNum);
			}
		}
		if (action != null) {
			System.out.printf("%d\t%s\n", action.getColmNumber(),
					action.getDirection());
			int k = PuyoNum
					- getNextPuyoNum(field, puyo, action.getColmNumber(),
							action.getDirection());

			System.out.println(k);
			return action;
		} else {
			// 消せるところがなければ，DefaultのActionを返す
			System.out.println("Deafault Action!");
			return getDefaultAction();
		}
	}

	private int getMinNextPuyoNum(Field field, Puyo puyo) {
		// TODO 自動生成されたメソッド・スタブ
		int minNum = 1000;
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				/**
				 * 配置不可能，または負けてしまうような配置は最初から考慮しない
				 */
				if (!isEnable(dir, i)) {
					continue;
				}

				int num = getNextPuyoNum(field, puyo, i, dir);
				if (minNum > num) {
					minNum = num;
				}
			}
		}
		return minNum;
	}

	private PuyoDirection getMinNextPuyoDir(Field field, Puyo puyo) {
		// TODO 自動生成されたメソッド・スタブ
		int minNum = 1000;
		PuyoDirection minDir = null;
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				/**
				 * 配置不可能，または負けてしまうような配置は最初から考慮しない
				 */
				if (!isEnable(dir, i)) {
					continue;
				}

				int num = getNextPuyoNum(field, puyo, i, dir);
				if (minNum > num) {
					minNum = num;
					minDir = dir;
				}
			}
		}
		if (minNum - getPuyoNum(field) == 2) {
			return null;
		} else {
			return minDir;
		}
	}

	private Action getAction(Field field, Puyo puyo, int PuyoNum) {
		Action action = null;
		double maxScore = -20;
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				/**
				 * 配置不可能，または負けてしまうような配置は最初から考慮しない
				 */
				if (!isEnable(dir, i)) {
					continue;
				}

				PuyoType firstPuyo = puyo.getPuyoType(PuyoNumber.FIRST);
				PuyoType secondPuyo = puyo.getPuyoType(PuyoNumber.SECOND);

				int firstNeighbor = 0;
				int secondNeighbor = 0;

				// 最初のぷよの周りに存在する同色ぷよ数を数える
				if (dir == PuyoDirection.DOWN) {
					// 二番目のぷよが下にある場合は，topの二つ上がy座標
					int y = field.getTop(i) + 2;
					firstNeighbor = getNeighborPuyoNum(i, y, firstPuyo);
				} else {
					// 二番目のぷよが下にある場合以外は，topの1つ上がy座標
					int y = field.getTop(i) + 1;
					firstNeighbor = getNeighborPuyoNum(i, y, firstPuyo);
				}

				// 二番目のぷよの周りに存在する同色ぷよを数える
				if (dir == PuyoDirection.DOWN) {
					// 二番目のぷよが下にある場合
					int y = field.getTop(i) + 1;
					secondNeighbor = getNeighborPuyoNum(i, y, secondPuyo);
				} else if (dir == PuyoDirection.UP) {
					// 二番目のぷよが上にある場合
					int y = field.getTop(i) + 2;
					secondNeighbor = getNeighborPuyoNum(i, y, secondPuyo);
				} else if (dir == PuyoDirection.RIGHT) {
					// 二番目のぷよが右にある場合
					int y = field.getTop(i) + 1;
					secondNeighbor = getNeighborPuyoNum(i + 1, y, secondPuyo);
				} else if (dir == PuyoDirection.LEFT) {
					// 二番目のぷよが左にある場合
					int y = field.getTop(i) + 1;
					secondNeighbor = getNeighborPuyoNum(i - 1, y, secondPuyo);
				}

				// 一番目のぷよの周りの同色ぷよ数と二番目のぷよの周りの同色ぷよ数が最大の場所に落とすようにしたい．
				// TODO
				int nextHeight = getNextHeight(i, dir);
				double score = firstNeighbor + secondNeighbor
						+ Math.abs(i - 2.5) / 1.5 - nextHeight / 4;
				int difNum = PuyoNum - getNextPuyoNum(field, puyo, i, dir);

				if (maxScore < score) {
					if (PuyoNum > 30) {
						action = new Action(dir, i);
						maxScore = score;
						System.out.println("ちょいやば");
					} else if (difNum == -2) {
						action = new Action(dir, i);
						maxScore = score;
					}
				}
			}
		}
		System.out.println(maxScore);
		return action;
	}

	private int getMinNextPuyoColumn(Field field, Puyo puyo) {
		// TODO 自動生成されたメソッド・スタブ
		int minNum = 1000;
		int minColumn = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				/**
				 * 配置不可能，または負けてしまうような配置は最初から考慮しない
				 */
				if (!isEnable(dir, i)) {
					continue;
				}

				int num = getNextPuyoNum(field, puyo, i, dir);
				if (minNum > num) {
					minNum = num;
					minColumn = i;
				}
			}
		}
		return minColumn;
	}

	/**
	 * 配置可能か，あるいは死んでしなわないかをチェックする TODO 未完成なので完成させる
	 * 
	 * @param i
	 * @param dir
	 * @return 配置不能または死んでしまう場合はfalse，それ以外はtrue
	 */
	private boolean isEnable(PuyoDirection dir, int i) {
		Field field = getMyBoard().getField();

		// 配置不能ならfalse
		if (!field.isEnable(dir, i)) {
			return false;
		}

		if (dir == PuyoDirection.DOWN || dir == PuyoDirection.UP) {
			if (field.getTop(i) >= field.getDeadLine() - 2) {
				return false;
			}
		}

		else if (dir == PuyoDirection.RIGHT) {
			if (field.getTop(i) >= field.getDeadLine() - 2
					|| field.getTop(i + 1) >= field.getDeadLine() - 2) {
				return false;
			}
		} else if (dir == PuyoDirection.LEFT) {
			if (field.getTop(i) >= field.getDeadLine() - 2
					|| field.getTop(i - 1) >= field.getDeadLine() - 2) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 指定した場所の周りに同色のぷよがいくつあるか
	 * 
	 * @param x
	 * @param y
	 * @param puyoType
	 * @return
	 */
	private int getPuyoNum(Field field) {
		int totalPuyoNum = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			totalPuyoNum += field.getTop(i);
		}

		return totalPuyoNum;
	}

	private int getNextPuyoNum(Field field, Puyo puyo, int x, PuyoDirection dir) {

		puyo.setDirection(dir);
		Field nextField = field.getNextField(puyo, x);
		int totalPuyoNum = 0;
		for (int i = 0; i < nextField.getWidth(); i++) {
			totalPuyoNum += nextField.getTop(i);
		}

		return totalPuyoNum;
	}

	private int getNextHeight(int x, PuyoDirection dir) {
		Field field = getMyBoard().getField();
		Puyo puyo = getMyBoard().getCurrentPuyo();
		puyo.setDirection(dir);
		Field nextField = field.getNextField(puyo, x);
		int maxHeight = 0;
		for (int i = 0; i < nextField.getWidth(); i++) {

			if (maxHeight < nextField.getTop(i)) {
				maxHeight = nextField.getTop(i);
			}
		}

		return maxHeight;
	}

	private int getNeighborPuyoNum(int x, int y, PuyoType puyoType) {
		// 数を記録する変数
		int count = 0;
		Field field = getMyBoard().getField();

		// 上下左右のPuyoTypeを
		// field.getPuyoType(a, b)で取得して，
		// puyoTypeと等しいかを確認する
		// フィールドからはみ出すとエラーが起きることに注意
		// if(条件式){
		// count++;
		// }
		// if(条件式){
		// count++;
		// }
		// if(条件式){
		// count++;
		// }
		// if(条件式){
		// count++;
		// }
		if (x != 0) {
			if (field.getPuyoType(x - 1, y) == puyoType) {
				count++;
			}
		}
		if (x != 5) {
			if (field.getPuyoType(x + 1, y) == puyoType) {
				count++;
			}
		}
		if (y != -1) {
			if (field.getPuyoType(x, y - 1) == puyoType) {
				count++;
			}
		}
		if (y != 11) {
			if (field.getPuyoType(x, y + 1) == puyoType) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 特に配置する場所がなかった場合の基本行動
	 * 
	 * @return
	 */
	Action getDefaultAction() {
		Board board = getGameInfo().getBoard(getMyPlayerInfo());
		Field field = board.getField();
		int minColumn = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			if (field.getTop(i) < field.getTop(minColumn)) {
				minColumn = i;
			}
		}

		Action action = new Action(PuyoDirection.DOWN, minColumn);

		return action;
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
