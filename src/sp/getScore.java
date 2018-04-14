package sp;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;

public class getScore {
	Field field;
	int score;
	
	
	/**
	 * 数えたいフィールドを指定して作成
	 * @param field
	 */
	public getScore(Field field,int score){
		this.field = field;
		this.score=score;
	}
	
	/**
	 * 接続したぷよ数を記録した行列を返す．<br>
	 * 隣接しているとは限らない．
	 * @return
	 */
	public int[][] getConnectedPuyoNum(){
		if(connectField == null){
			//まず隣接ぷよ数を取得する
			
			int[][] neighborField = new int[field.getHeight()][field.getWidth()];
			for(int x = 0; x < field.getWidth(); x++){
				for(int y = 0; y < field.getHeight(); y++){
					if(field.getPuyoType(x, y) != null){
						neighborField[y][x] = getNeighborPuyoNum(x, y);
					}
				}
			}

			connectField = new int[field.getHeight()][field.getWidth()];
			for(int x = 0; x < field.getWidth(); x++){
				for(int y = 0; y < field.getHeight(); y++){
					if(field.getPuyoType(x, y) == null){
						continue;
					}
					
					int max = neighborField[y][x];
					if(field.isOnField(x+1, y)){
						if(field.getPuyoType(x+1, y) == field.getPuyoType(x, y) && neighborField[y][x+1] > max){
							max = neighborField[y][x+1];
						}
					}
					if(field.isOnField(x-1, y)){
						if(field.getPuyoType(x-1, y) == field.getPuyoType(x, y) && neighborField[y][x-1] > max){
							max = neighborField[y][x-1];
						}
					}
					if(field.isOnField(x, y+1)){
						if(field.getPuyoType(x, y+1) == field.getPuyoType(x, y) && neighborField[y+1][x] > max){
							max = neighborField[y+1][x];
						}
					}
					if(field.isOnField(x, y-1)){
						if(field.getPuyoType(x, y-1) == field.getPuyoType(x, y) && neighborField[y-1][x] > max){
							max = neighborField[y-1][x];
						}
					}
					connectField[y][x] = max;
				}
			}
		}
		return connectField;
	}

	
	
	
	
}
