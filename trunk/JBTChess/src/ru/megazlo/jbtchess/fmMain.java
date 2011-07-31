package ru.megazlo.jbtchess;

import ru.megazlo.jbtchess.board.BoardAdapter;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.GridView;

public class fmMain extends Activity {
	private GridView mBoard;
	private BoardAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mAdapter = new BoardAdapter(this);

		mBoard = (GridView) findViewById(R.id.life_grid);
		mBoard.setAdapter(mAdapter);

		// Display display = ((WindowManager)
		// this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		// int width = display.getWidth();
		// int height = display.getHeight();
		//
		// width = width < height ? width : height;

		// mBoard.setColumnWidth(width / 8);
		// mBoard.setEnabled(false);
		// mBoard.setStretchMode(0);

	}
}
