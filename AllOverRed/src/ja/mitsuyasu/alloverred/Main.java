package ja.mitsuyasu.alloverred;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	// 変数
	public static Node start = new Node(); // スタートノード
	public static Node goal = new Node(); // ゴールノード
	public static ArrayList<Node> open = new ArrayList<Node>();  // オープンリスト
	public static ArrayList<Node> close = new ArrayList<Node>(); // クローズリスト
	public static ArrayList<Node> beside = new ArrayList<Node>();// 隣接ノードリスト
	public static HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();// ノードの種類別のハッシュ
	public static boolean isInit = false; // 初期化フラグ
	public static long startTime = 0; // 開始時間
	public static long endTime = 0; // 終了時間

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		initialize();

		// ゲームスタートできるまで待つ
		while (!isInit)
			;

		// 初期状態を表示
		// showResult(start.check(0, 1));

		// ここでごちゃごちゃいじる

		// 開始時間を記録
		startTime = System.currentTimeMillis();
		System.out.println("探索を開始します...");
		// A*アルゴリズム
		Node result = astar();
		// 終了時間を記録
		endTime = System.currentTimeMillis();
		double pastTime = (endTime - startTime) / 1000;
		System.out.println("探索が完了しました！(所要時間: " + pastTime + "秒)");

		// 結果を出力
		// 結果が得られなければ終了
		if (result == null) {
			System.out.println("回答なし（´・ω・） ｶﾜｲｿｽ");
			return;
		}

		while (result != null) {
			showResult(result);
			result = result.parent;
		}
	}

	public static void initialize() {
		// スタートの初期値
		// for (int i = 0; i < 4; i++) {
		// for (int j = 0; j < 4; j++) {
		// goal.set(i, j);
		// }
		// }

		start.set(0, 0);
		start.set(0, 1);
		start.set(0, 2);
		start.set(0, 3);

		start.set(1, 0);
		start.set(1, 1);
		start.set(1, 2);
		start.set(1, 3);

		start.set(2, 0);
		start.set(2, 1);
		start.set(2, 2);
		start.set(2, 3);

		start.set(3, 0);
		start.set(3, 1);
		start.set(3, 2);
		// start.set(3, 3);

		// ゴールの状態
		 for (int i = 0; i < 4; i++) {
             for (int j = 0; j < 4; j++) {
                 goal.set(i, j);
             }
         }

		// goal.set(0, 0);
		// goal.set(0, 1);
		// goal.set(0, 2);
		// goal.set(0, 3);

		// goal.set(1, 0);
		// goal.set(1, 1);
		// goal.set(1, 2);
		// goal.set(1, 3);

		// goal.set(2, 0);
		// goal.set(2, 1);
		// goal.set(2, 2);
		// goal.set(2, 3);

		// goal.set(3, 0);
		// goal.set(3, 1);
		// goal.set(3, 2);
		// goal.set(3, 3);

		// 初期化完了
		isInit = true;
	}

	public static Node astar() {
		open.add(start); // オープンリストにスタートノードを追加
		close.clear(); // クローズリストを初期化

		while (!open.isEmpty()) {
			// Openリストに格納されているノードの内，最小の f*(n) を持つノード n を取り出す．
			Node min = getMinNode(open);

			if (min.equals(goal)) { // 終了状態の場合
				return min;
			} else { // それ以外の場合は n を Closeリストに移す．
				open.remove(min);
				close.add(min);
			}

			// 隣接ノードを求める
			// 隣接ノードを格納するための配列をクリア
			beside.clear();
			// 今回，すべての場合を当たる必要があるのでループ回す
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					beside.add(min.check(i, j));
				}
			}

			// minの親は検索しなくても良いので除く
			if (min.parent != null) {
				beside.remove(min.parent);
			}

			// 隣接リストについて
			for (Node d : beside) {
				// 取り敢えずOpenノードとCloseノードに検索をかける
				int openId = isIncludeNode(open, d);
				int closeId = isIncludeNode(close, d);

				// m が Openリストにも Closeリストにも含まれていない場合 f*(m) = f '(m) とし m を
				// Openリストに追加する。
				// このとき m の親を n として記録する。
				if (openId == -1 && closeId == -1) {
					d.parent = min;
					open.add(d);
				} else if (openId != -1 && closeId == -1) {
					// m が Openリストにある場合、f '(m) < f*(m) であるなら f*(m) = f '(m)
					// に置き換える。
					// このとき記録してある m の親を n に置き換える。
					if (open.get(openId).cost > d.cost) {
						d.parent = min;
						open.set(openId, d);
					}
				} else {
					// m が Closeリストにある場合、f '(m) < f*(m) であるなら f*(m) = f '(m)
					// として m を Openリストに移動する。
					// また記録してある m の親を n に置き換える。
					if (close.get(closeId).cost > d.cost) {
						d.parent = min;
						close.remove(openId);
						open.add(d);
					}
				}
			}
		}
		return null; // 探索失敗
	}

	/**
	 * 最小コストのノードを返す
	 * 
	 * @param list
	 * @return minNode
	 */
	public static Node getMinNode(ArrayList<Node> list) {
		Node min = list.get(0);
		for (Node d : list) {
			if (min.cost > d.cost) {
				min = d;
			}
		}
		return min;
	}

	/**
	 * 指定したノードが配列に含まれるか検索 存在している場合，そのidを返す 存在していない場合，-1を返す
	 * 
	 * @param list
	 * @param node
	 * @return id
	 */
	public static int isIncludeNode(ArrayList<Node> list, Node node) {
		for (int i = 0; i < list.size(); i++) {
			if (node.equals(list.get(i))) {
				return i;
			}
		}
		return -1;
	}

	public static void showResult(Node d) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// クリックされた場所を見やすくする
				if (d.x != -1 && d.y != -1) {
					if (d.x == i && d.y == j) {
						System.out.print("[!]");
						continue;
					}
				}
				if (d.field[i][j]) {
					System.out.print("[*]");
				} else {
					System.out.print("[ ]");
				}
			}
			System.out.println("");
		}

		System.out.println("Click (" + d.x + ", " + d.y + ")");
		System.out.println("Cost: " + d.cost);
	}
}
