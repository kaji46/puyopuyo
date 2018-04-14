package SamplePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoNumber;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;
import sp.ConnectionCounter;
import sp.puyoScore;

/**
 * かえる積みを行うのほほです。
 * 
 * @author Hara
 * 
 */
public class Kaji2 extends AbstractPlayer {
	public Kaji2(String playerName) {
		super(playerName);
	}

	@Override
	public Action doMyTurn() {
		int sort[] = { 2, 3, 1, 4, 0, 5 };
		boolean emergency = false;
		/**
		 * 現在のフィールドの状況
		 */
		Puyo puyoAlign[] = new Puyo[36];
		int a = 0;
		int num = 0;
		List<puyoScore> puyoscore = new ArrayList<puyoScore>();
		Map<PuyoNumber, PuyoType> mapb = new HashMap();
		for (PuyoType pt1 : PuyoType.values()) {
			mapb.put(PuyoNumber.FIRST, pt1);
			for (PuyoType pt2 : PuyoType.values()) {
				mapb.put(PuyoNumber.SECOND, pt2);
				Puyo puyo = new Puyo(mapb);
				puyoAlign[a] = puyo;
				a++;
			}
		}
		Field field = getMyBoard().getField();
		List<Integer> ojama = getMyBoard().getNumbersOfOjamaList();
		int[] enemyNum = enemyNum();
		Action action = new Action();

		int ojama0 = ojama.get(0);
		int ojama1 = ojama.get(1);
		int ojama2 = ojama.get(2);
		if (ojama0 > 0
				|| ojama1 > 0
				|| ojama2 > 0
				|| getPuyoNum(field) > field.getWidth() * field.getHeight()
						- 23) {
			emergency = true;

		}

		int maxScore = -100;
		for (int k0 = 0; k0 < field.getWidth(); k0++) {
			int i = sort[k0];
			for (PuyoDirection dir : PuyoDirection.values()) {
				if (!isEnable(dir, i, field)) {
					continue;
				}
				for (int k1 = 0; k1 < field.getWidth(); k1++) {
					int nextI = sort[k1];
					for (PuyoDirection nextDir : PuyoDirection.values()) {
						// 配置不能条件
						Puyo puyo = getMyBoard().getCurrentPuyo();

						puyo.setDirection(dir);

						Field nextField = field.getNextField(puyo, i);

						if (!isEnable(nextDir, nextI, nextField)) {
							continue;
						}

						for (int k2 = 0; k2 < field.getWidth(); k2++) {
							int nextNextI = sort[k2];
							for (PuyoDirection nextNextDir : PuyoDirection
									.values()) {

								/**
								 * 配置不可能，または負けてしまうような配置は最初から考慮しない
								 */
								// 配置不能条件

								Puyo nextPuyo = getMyBoard().getNextPuyo();
								nextPuyo.setDirection(nextDir);
								Field nextNextField = nextField.getNextField(
										nextPuyo, nextI);

								if (!isEnable(nextNextDir, nextNextI,
										nextNextField)) {
									continue;
								}

								int score = getScore(i, dir, nextI, nextDir,
										nextNextI, nextNextDir, enemyNum,
										puyoAlign, ojama);
								Puyo nextNextPuyo = getMyBoard()
										.getNextNextPuyo();
								nextNextPuyo.setDirection(nextNextDir);
								Field nextNextNextField = nextNextField
										.getNextField(nextNextPuyo, nextNextI);
								Action actionk = new Action(dir, i);
								puyoScore puyoscorek = new puyoScore(score,
										actionk, nextNextNextField);
								puyoscore.add(puyoscorek);

								//
								// if (score > maxScore) {
								// action = new Action(dir, i);
								// maxScore = score;
								// }
							}
						}
					}
				}
			}
		}
		Collections.sort(puyoscore);
		// System.out.println("ここまで");
		if (emergency) {
			action = puyoscore.get(0).getAction();
			maxScore = puyoscore.get(0).getScore();
		} else {

			// System.out.println("ここpeke");
			for (int b = 0; b < 100; b++) {

				int score = puyoscore.get(b).getScore()
						+ nextChainNum(puyoscore.get(b).getField(), puyoAlign)
						* 3;
				if (maxScore < score) {
					maxScore = score;
					action = puyoscore.get(b).getAction();
				}
			}
		}

		if (action == null) {
			System.out.println("Default");
			action = getDefaultAction();
		}
		// System.out.println("----------------------");
		// printField(field);
		System.out.printf("%d-%s(%d)\n", action.getColmNumber(),
				action.getDirection(), maxScore);
		// System.out.println("----------------------");
		// System.out.println(field.getPuyoType(2, 0));
		return action;

	}

	/**
	 * スコアリング
	 * 
	 * @param x
	 * @param dir
	 * @return
	 */

	private int getScore(int x, PuyoDirection dir, int nextX,
			PuyoDirection nextDir, int nextNextX, PuyoDirection nextNextDir,
			int[] enemyNum, Puyo[] puyoAlign, List<Integer> ojama) {
		// System.out.printf("%d,%s\n", x, dir);
		// この辺ももってこれる
		Field field = getMyBoard().getField();
		Puyo puyo = getMyBoard().getCurrentPuyo();
		Puyo nextPuyo = getMyBoard().getNextPuyo();
		Puyo nextNextPuyo = getMyBoard().getNextNextPuyo();
		puyo.setDirection(dir);
		nextPuyo.setDirection(nextDir);
		nextNextPuyo.setDirection(nextNextDir);
		Field nextField = field.getNextField(puyo, x);

		Field nextNextField = nextField.getNextField(nextPuyo, nextX);

		Field nextNextNextField = nextNextField.getNextField(nextNextPuyo,
				nextNextX);

		// 危機的状況かどうか
		// boolean emergency = false;

		int ojama0 = ojama.get(0);
		int ojama1 = ojama.get(1);
		int ojama2 = ojama.get(2);
		int score = 0;
		int totalPuyoNum = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			totalPuyoNum += field.getTop(i);
		}
		int chainNum = chainNum(puyoColorNum(field), puyoColorNum(nextField),
				puyo);
		int nextChainNum = chainNum(puyoColorNum(nextField),
				puyoColorNum(nextNextField), nextPuyo);
		int nextNextChainNum = chainNum(puyoColorNum(nextNextField),
				puyoColorNum(nextNextNextField), nextNextPuyo);
		// hyugy7

		if (ojama0 > 0
				|| totalPuyoNum > field.getWidth() * field.getHeight() - 19) {

			// 危機的状況の時は積極的に消しに行く
			score += (chainNum * 5 + getPuyoNum(field) - getPuyoNum(nextField)) * 10;
			// System.out.println("ojama1");
			ConnectionCounter cnt = new ConnectionCounter(nextField);
			int[][] countField = cnt.getConnectedPuyoNum();

			for (int i = 0; i < countField.length; i++) {
				for (int j = 0; j < countField[i].length; j++) {
					score += countField[i][j];
				}
			}
			return score;

		} else if (ojama1 > 0
				|| totalPuyoNum > field.getWidth() * field.getHeight() - 21) {

			// 危機的状況の時は積極的に消しに行く
			score += Math.max(nextChainNum, chainNum) * 5 + getPuyoNum(field)
					- getPuyoNum(nextNextField);
			// System.out.println("ojama1");
			return score;

		} else if (ojama2 > 0
				|| totalPuyoNum > field.getWidth() * field.getHeight() - 23) {

			score += Math.max(nextChainNum, nextNextChainNum) * 5
					+ getPuyoNum(field) - getPuyoNum(nextNextNextField);
			// System.out.println("ojama2");
			return score;

		}
		ConnectionCounter cnt = new ConnectionCounter(nextNextNextField);
		int[][] countField = cnt.getConnectedPuyoNum();

		for (int i = 0; i < countField.length; i++) {
			for (int j = 0; j < countField[i].length; j++) {
				score += countField[i][j];
			}
		}
		// つながりが強いほど高スコア

		//
		// // じゃまぷよの数に応じて
		// if (getPuyoNum(field) - getPuyoNum(nextField) == -2) {
		// int max = 0;
		// int min = field.getHeight();
		// for (int i = 0; i < nextField.getWidth(); i++) {
		// max = Math.max(max, nextField.getTop(i) + 1);
		// min = Math.min(min, nextField.getTop(i) + 1);
		// }
		//
		// // score += (field.getHeight() -max );
		//
		// // 3連鎖以上する場合は積極的に置く
		// if (getPuyoNum(nextField) < getPuyoNum(field) - 4 * 3) {
		// score += (getPuyoNum(field) - getPuyoNum(nextField));
		// score *= 3;
		// }
		// // List<Integer> ojama = getMyBoard().getNumbersOfOjamaList();
		// // int ojama0 = ojama.get(0);
		// if (getPuyoNum(nextNextField) < getPuyoNum(nextField) - 4 * 3
		// && ojama0 == 0) {
		// score += (getPuyoNum(field) - getPuyoNum(nextField));
		// score *= 2;
		// }
		// if (getPuyoNum(nextNextNextField) < getPuyoNum(nextNextField) - 4 * 3
		// && ojama0 == 0) {
		// score += (getPuyoNum(field) - getPuyoNum(nextField));
		// score *= 2;
		// }
		// /**
		// * みぎ２個には置かない
		// */
		// if (x == 5 || x == 4 || (x == 3 && dir == PuyoDirection.RIGHT)) {
		// score /= 1.2;
		// }
		//
		// if (getPuyoNum(nextField) - getPuyoNum(nextField) == -2) {
		// if (getPuyoNum(nextNextField) - getPuyoNum(nextField) == -2) {
		//
		// }
		//
		// }
		// }
		//
		// else {
		// // 危機的状況の時は積極的に消しに行く
		// score += field.getHeight() * field.getWidth()
		// - getPuyoNum(nextField);
		// score += (getPuyoNum(field) - getPuyoNum(nextField)) * 3;
		// }
		// /*
		// * int max = 0; for(int i = 0; i < nextField.getWidth(); i++){ max =
		// * Math.max(max, nextField.getTop(i)+1); } score +=
		// * field.getHeight()-max;
		// */
		//
		// 危機的状況でなければ，つながりを多くする
		//
		// int max = 0;
		// int min = field.getHeight();
		// for (int i = 0; i < nextField.getWidth(); i++) {
		// max = Math.max(max, nextField.getTop(i) + 1);
		// min = Math.min(min, nextField.getTop(i) + 1);
		// }
		// if (max > 8 && getPuyoNum(field) < 30) {
		// // System.out.println("min");
		// score -= 10;
		// }
		// score += nextChainNum(nextNextNextField,puyoAlign);

		int enemyNumMax = Math.max(enemyNum[0], enemyNum[1]);
		enemyNumMax = Math.max(enemyNumMax, enemyNum[2]);

		// 3連鎖以上する場合は積極的に置く
		if (chainNum >= 3
				&& enemyNumMax < getPuyoNum(field) - getPuyoNum(nextField)) {
			score += (getPuyoNum(field) - getPuyoNum(nextField));
			score *= 3;
		}
		// List<Integer> ojama = getMyBoard().getNumbersOfOjamaList();
		// int ojama0 = ojama.get(0);

		if (nextChainNum >= 3 && ojama0 == 0 && enemyNumMax < nextChainNum) {
			score += (getPuyoNum(field) - getPuyoNum(nextField));
			score *= 2;
		}
		if (nextNextChainNum >= 3 && ojama0 == 0
				&& enemyNumMax < nextNextChainNum) {
			score += (getPuyoNum(field) - getPuyoNum(nextField));
			score *= 2;
		}
		// for (int j = 0; j < nextField.getWidth()-1; j++) {
		// if (Math.abs(nextField.getTop(j) - nextField.getTop(j+1))>3) {
		// score-=1;
		// }
		// }

		// if (x ==5||x==4||(x==3&&dir==PuyoDirection.RIGHT) ){
		// score /= 1.2;
		// }

		// printField(nextField);
		// System.out.printf("[%d-%s]\t%d\t%d\n", x, dir.toString(), score,
		// getPuyoNum(nextField));
		return score;
	}

	/**
	 * 配置可能か，あるいは死んでしなわないかをチェックする
	 * 
	 * @param i
	 * @param dir
	 * @return 配置不能または死んでしまう場合はfalse
	 */
	private boolean isEnable(PuyoDirection dir, int i, Field field) {
		// 配置不能ならfalse
		if (!field.isEnable(dir, i)) {
			return false;
		}

		if (dir == PuyoDirection.DOWN || dir == PuyoDirection.UP) {
			if (field.getTop(i) >= field.getDeadLine() - 2) {
				return false;
			}
		} else if (dir == PuyoDirection.RIGHT) {
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
	 * 指定したフィールドのぷよ数を返す
	 * 
	 * @param field
	 * @return
	 */
	int getPuyoNum(Field field) {
		int num = 0;
		// ここでぷよの数を数える．
		// field.getTop(columnNum)で，ぷよが存在する場所を返すので，
		// それより1大きい数のぷよがその列には存在する
		// ぷよが一つもない列は-1が返ってくることに注意．

		for (int i = 0; i < field.getWidth(); i++) {
			num += field.getTop(i) + 1;
		}

		return num;
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

	public int[] enemyNum() {
		int num[] = { 100, 100, 100, 100 };
		int dec[] = { 0, 0, 0 };
		int chainNum[] = new int[3];
		Field field = getEnemyBoard().getField();
		num[0] = getPuyoNum(field);
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {
				if (!isEnable(dir, i, field)) {
					continue;
				}
				Puyo puyo = getEnemyBoard().getCurrentPuyo();
				puyo.setDirection(dir);

				Field nextField = field.getNextField(puyo, i);
				num[1] = Math.min(num[1], getPuyoNum(nextField));
				dec[0] = Math.max(dec[0], getPuyoNum(field)
						- getPuyoNum(nextField));
				chainNum[0] = Math.max(
						chainNum[0],
						chainNum(puyoColorNum(field), puyoColorNum(nextField),
								puyo));
				for (int nextI = 0; nextI < field.getWidth(); nextI++) {
					for (PuyoDirection nextDir : PuyoDirection.values()) {
						// 配置不能条件

						if (!isEnable(nextDir, nextI, nextField)) {
							continue;
						}
						Puyo nextPuyo = getEnemyBoard().getNextPuyo();
						nextPuyo.setDirection(nextDir);
						Field nextNextField = nextField.getNextField(nextPuyo,
								nextI);
						num[2] = Math.min(num[2], getPuyoNum(nextNextField));
						dec[1] = Math.max(dec[1], getPuyoNum(nextField)
								- getPuyoNum(nextNextField));
						chainNum[1] = Math.max(
								chainNum[1],
								chainNum(puyoColorNum(nextField),
										puyoColorNum(nextNextField), nextPuyo));
						for (int nextNextI = 0; nextNextI < field.getWidth(); nextNextI++) {
							for (PuyoDirection nextNextDir : PuyoDirection
									.values()) {

								/**
								 * 配置不可能，または負けてしまうような配置は最初から考慮しない
								 */
								// 配置不能条件

								if (!isEnable(nextNextDir, nextNextI,
										nextNextField)) {
									continue;
								}

								//
								Puyo nextNextPuyo = getEnemyBoard()
										.getNextNextPuyo();
								nextPuyo.setDirection(nextDir);
								Field nextNextNextField = nextNextField
										.getNextField(nextNextPuyo, nextNextI);
								num[3] = Math.min(num[3],
										getPuyoNum(nextNextNextField));
								dec[2] = Math
										.max(dec[2], getPuyoNum(nextNextField)
												- getPuyoNum(nextNextNextField));
								chainNum[2] = Math
										.max(chainNum[2],
												chainNum(
														puyoColorNum(nextNextField),
														puyoColorNum(nextNextNextField),
														nextNextPuyo));

							}
						}
					}
				}
			}
		}

		return chainNum;
	}

	public int[] puyoColorNum(Field field) {
		// RED,BLUE,GREEN,PURPLE,YELLOW
		int num[] = { 0, 0, 0, 0, 0 };
		for (int i = 0; i < field.getWidth(); i++) {
			for (int j = 0; j < field.getHeight(); j++) {
				if (field.getPuyoType(i, j) == PuyoType.RED_PUYO) {
					num[0] += 1;
				} else if (field.getPuyoType(i, j) == PuyoType.BLUE_PUYO) {
					num[1] += 1;

				} else if (field.getPuyoType(i, j) == PuyoType.GREEN_PUYO) {
					num[2] += 1;

				} else if (field.getPuyoType(i, j) == PuyoType.PURPLE_PUYO) {
					num[3] += 1;
				} else if (field.getPuyoType(i, j) == PuyoType.YELLOW_PUYO) {
					num[4] += 1;
				} else {
					continue;
				}
			}
		}
		return num;

	}

	public int chainNum(int[] puyoColorNum, int[] nextPuyoColorNum, Puyo puyo) {
		int chainNum = 0;
		if (puyo.getPuyoType(PuyoNumber.FIRST) == PuyoType.RED_PUYO) {
			nextPuyoColorNum[0] += 1;
		} else if (puyo.getPuyoType(PuyoNumber.FIRST) == PuyoType.BLUE_PUYO) {
			nextPuyoColorNum[1] += 1;

		} else if (puyo.getPuyoType(PuyoNumber.FIRST) == PuyoType.GREEN_PUYO) {
			nextPuyoColorNum[2] += 1;

		} else if (puyo.getPuyoType(PuyoNumber.FIRST) == PuyoType.PURPLE_PUYO) {
			nextPuyoColorNum[3] += 1;
		} else if (puyo.getPuyoType(PuyoNumber.FIRST) == PuyoType.YELLOW_PUYO) {
			nextPuyoColorNum[4] += 1;
		}
		if (puyo.getPuyoType(PuyoNumber.SECOND) == PuyoType.RED_PUYO) {
			nextPuyoColorNum[0] += 1;
		} else if (puyo.getPuyoType(PuyoNumber.SECOND) == PuyoType.BLUE_PUYO) {
			nextPuyoColorNum[1] += 1;

		} else if (puyo.getPuyoType(PuyoNumber.SECOND) == PuyoType.GREEN_PUYO) {
			nextPuyoColorNum[2] += 1;

		} else if (puyo.getPuyoType(PuyoNumber.SECOND) == PuyoType.PURPLE_PUYO) {
			nextPuyoColorNum[3] += 1;
		} else if (puyo.getPuyoType(PuyoNumber.SECOND) == PuyoType.YELLOW_PUYO) {
			nextPuyoColorNum[4] += 1;
		}
		int[] d = new int[5];
		for (int i = 0; i < 5; i++) {
			d[i] = (int) ((puyoColorNum[i] - nextPuyoColorNum[i]) / 4);
		}
		for (int i = 0; i < 5; i++) {
			chainNum += d[i];
		}
		return chainNum;
	}

	public void printField(Field field) {
		for (int y = field.getHeight(); y >= 0; y--) {
			for (int x = 0; x < field.getWidth(); x++) {
				if (field.getPuyoType(x, y) != null) {
					System.out.print(field.getPuyoType(x, y).toString()
							.substring(0, 1));
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
	}

	@Override
	public void initialize() {
		// TODO 自動生成されたメソッド・スタブ

	}

	public int nextChainNum(Field field, Puyo[] puyoAlign) {
		int nextChainNum = 0;
		int pcn[] = puyoColorNum(field);
		for (int a = 0; a < 36; a++) {

			Puyo puyo = puyoAlign[a];
			for (int i = 0; i < field.getWidth(); i++) {
				Field nextField = field.getNextField(puyo, i);
				nextChainNum = Math.max(nextChainNum,
						chainNum(pcn, puyoColorNum(nextField), puyo));
				// nextChainNum = Math.max(
				// nextChainNum,
				// (int)((getPuyoNum(field)-getPuyoNum(nextField))));

			}
		}

		return nextChainNum;
	}

	@Override
	public void inputResult() {
		// TODO 自動生成されたメソッド・スタブ

	}

}