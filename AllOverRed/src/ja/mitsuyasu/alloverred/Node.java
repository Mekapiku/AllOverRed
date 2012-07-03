package ja.mitsuyasu.alloverred;

public class Node {
	// ゲームの舞台となるフィールド
    public boolean[][] field = new boolean[4][4];
    // 親ノード
    public Node parent;
    // 現在のコスト
    public int cost;
    // チェックされた座標
    public int x = -1;
    public int y = -1;
    // 初期化フラグ
    public boolean isInit = false;
    
    Node() {
        initialize();
    }
    
    Node(Node d) {
        // ゲームのフィールドのコピー
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.field[i][j] = d.field[i][j];
            }
        }
        // 親ノードの継承
        this.parent = d.parent;
        // コストの継承
        this.cost = d.cost;      
        // 初期化完了
        isInit = true;
    }
    
    void initialize() {
    	// ゲームのフィールドを初期化
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                field[i][j] = false;
            }
        }
        // 親ノードはnull
        parent = null;
        // コストを初期化
        cost = 0;
        // 初期化フラグ
        isInit = true;
    }
    
    /**
     * 初期値をセットするためのメソッド
     * 副作用は一切なし．
     * 
     * @param x
     * @param y
     */
    void set(int x, int y) {
    	field[x][y] = true;
    }
    
    /**
     * 指定したマスにチェックをつける
     * 副作用としての反転・コスト追加は自動的に行う
     * 
     * @param x
     * @param y
     * @return 
     */
    Node check(int x, int y) {
    	Node d = new Node(this);
        if (x < 4 && y < 4) {
        	// 指定されたxy座標について反転
            for (int i = 0; i < 4; i++) {
            	d.field[x][i] = !this.field[x][i]; // 反転
            	d.field[i][y] = !this.field[i][y]; // 反転
            }
            
            // 最後に押した座標を反転させる
            d.field[x][y] = !this.field[x][y];
            // コストを増やす
            d.cost = this.cost+1;
            // チェックされた座標を保存
            d.x = x;
            d.y = y;
        }
        return d;
    }
    
    boolean equals(Node d) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (this.field[i][j] != d.field[i][j]) {
                	return false;
                }
            }
        }
        
        return true;
    }
}
