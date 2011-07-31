package ru.megazlo.jbtchess.board;

import ru.megazlo.jbtchess.R;
import ru.megazlo.jbtchess.board.pieces.Type;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class BoardAdapter extends BaseAdapter {
	Context mContext;
	BoardModel mModel;

	public BoardAdapter(Context c) {
		mContext = c;
		mModel = new BoardModel();
	}

	@Override
	public int getCount() {
		return mModel.getCount();
	}

	@Override
	public Object getItem(int position) {
		return ((BoardModel) mModel).itemAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView view = (convertView == null) ? new ImageView(mContext) : (ImageView) convertView;
		CheckModel cm = mModel.itemAt(position);
		view.setImageResource(getRes(cm.getType(), cm.getColor()));
		view.setBackgroundColor(cm.getCheckColor() ? Color.WHITE : Color.BLACK);
		return view;
	}

	private int getRes(int type, int color) {
		if (type == Type.None)
			return R.drawable.none;
		if (color == Color.BLACK)
			return getBlack(type);
		else
			return getWhite(type);
	}

	private int getWhite(int type) {
		switch (type) {
		case Type.Bishop:
			return R.drawable.whitebishop;
		case Type.King:
			return R.drawable.whiteking;
		case Type.Knight:
			return R.drawable.whiteknight;
		case Type.Pawn:
			return R.drawable.whitepawn;
		case Type.Queen:
			return R.drawable.whitequeen;
		case Type.Rook:
			return R.drawable.whiterook;
		default:
			return R.drawable.none;
		}
	}

	private int getBlack(int type) {
		switch (type) {
		case Type.Bishop:
			return R.drawable.blackbishop;
		case Type.King:
			return R.drawable.blackking;
		case Type.Knight:
			return R.drawable.blackknight;
		case Type.Pawn:
			return R.drawable.blackpawn;
		case Type.Queen:
			return R.drawable.blackqueen;
		case Type.Rook:
			return R.drawable.blackrook;
		default:
			return R.drawable.none;
		}
	}
}
