package ru.megazlo.crazytest.components;

import java.util.Date;

import ru.megazlo.crazytest.R;
import ru.megazlo.crazytest.utils.NoteData;
import ru.megazlo.crazytest.utils.Parcer;
import ru.megazlo.crazytest.utils.TimeSpan;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class NotifyList extends ListView {

	public NotifyList(Context context) {
		super(context);
	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		NoteData datn = ((NotifyRow) view).getData();

		Date now = new Date();
		if (now.getTime() < datn.From.getTime()) {
			Toast.makeText(getContext(), R.string.hahaha, Toast.LENGTH_SHORT).show();
		} else {

			TimeSpan rze = Parcer.parce(datn.From);

			String msg = Parcer.createMessage(rze, getContext());
			// String msg = "y:" + rze.year + " m:" + rze.month + " d:\n" + rze.day +
			// " h:" + rze.hour + " m:" + rze.minutes;

			Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

			// Date tst = new Date(); какая-то шляпа
			// Date post = new Date(tst.getTime() - datn.From.getTime());
			// Toast.makeText(
			// getContext(),
			// "y:" + (post.getYear() - 70) + " m:" + post.getMonth() + " d:" +
			// post.getDate() + " h:" + post.getHours()
			// + " m:" + post.getMinutes(), Toast.LENGTH_SHORT).show();
		}
		return super.performItemClick(view, position, id);
	}

}
