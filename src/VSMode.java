
import hira.player.lib5ultimate.HiraLib5Ultimate;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.u_tokyo.torilab.usui.UsuiPlayerLv2.UsuiPlayerLv2;
import jp.ac.u_tokyo.torilab.usui.UsuiPlayerLv2.UsuiPlayerLv3;

import maou2014.Maou;
import moc.liamtoh900ognek.KajiGodGod;
import player.SamplePlayer08;
import SamplePlayer.Kaji;
import SamplePlayer.Kaji1;
import SamplePlayer.Kaji2;
import SamplePlayer.Kaji3;
import SamplePlayer.Kaji4;
import SamplePlayer.Kaji5;
import SamplePlayer.Kaji6;
import SamplePlayer.Kaji7;
import SamplePlayer.Kaji8;
import SamplePlayer.Kaji9;
import SamplePlayer.Kaji10;
import SamplePlayer.KajiEmperor;

import SamplePlayer.ScoringPlayer;
import sp.AbstractSamplePlayer;
import SamplePlayer.Nohoho;
import UsuiPlayer.UsuiPlayer;

/**
 * 任意の二体のエージェント同士を対戦させるためのクラス
 */
public class VSMode {

	public static void main(String args[]) {

		AbstractPlayer Scoring = new ScoringPlayer("Scoring");
		AbstractPlayer Kaji = new Kaji("kaji");
		AbstractPlayer Kaji1 = new Kaji1("kaji1");
		AbstractPlayer Kaji2 = new Kaji2("kaji2");
		AbstractPlayer Kaji3 = new Kaji3("kaji3");
		AbstractPlayer Kaji4 = new Kaji4("kaji4");
		AbstractPlayer Kaji5 = new Kaji5("kaji5");
		AbstractPlayer Kaji6 = new Kaji6("kaji6");
		AbstractPlayer Kaji7 = new Kaji7("kaji7");
		AbstractPlayer Kaji8 = new Kaji8("kaji8");
		AbstractPlayer Kaji9 = new Kaji9("kaji9");
		AbstractPlayer Kaji10 = new Kaji9("kaji10");
		AbstractPlayer KajiEmperor = new KajiEmperor("kajiEmperor");

		AbstractPlayer Nohoho = new Nohoho("Nohoho");	//カエル積み
		AbstractPlayer TA1 = new UsuiPlayer("UsuiLV1");		//TA1
		AbstractPlayer TA2 = new UsuiPlayerLv2("UsuiLV3");		//TA2
		AbstractPlayer TA3 = new UsuiPlayerLv3("UsuiLV3");		//TA3
		AbstractPlayer TA4 = new KajiGodGod("KajiGod");	//TA4
		AbstractPlayer maou = new Maou("Maou");			//かつての一位

		AbstractPlayer hiraUltimate = new HiraLib5Ultimate("Ultimate");	//2015年度優勝
		AbstractPlayer sample =new SamplePlayer08("sample");
		//AbstractPlayer player = 自分の作ったプレイヤー;

		/**
		 * 任意の二つのエージェントを対戦させる．<br>
		 */
		PuyoPuyo puyopuyo = new PuyoPuyo(TA4, Kaji9);
		puyopuyo.puyoPuyo();


	}
}
