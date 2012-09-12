package com.commonsensenet.realfarm.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsensenet.realfarm.R;
import com.commonsensenet.realfarm.model.aggregate.UserAggregateItem;

public class UserAggregateItemAdapter extends ArrayAdapter<UserAggregateItem> {
	/** Database provided that used to obtain the required data. */

	/**
	 * Creates a new UserAggregateItemAdapter instance.
	 */
	public UserAggregateItemAdapter(Context context,
			List<UserAggregateItem> userAggregates) {
		super(context, android.R.layout.simple_list_item_1, userAggregates);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.tpl_user_aggregate_item, null);
		}

		UserAggregateItem userAggregate = getItem(position);
		if (userAggregate != null) {
			TextView nameView = (TextView) v
					.findViewById(R.id.label_user_aggregate_user_name);
			TextView dateView = (TextView) v
					.findViewById(R.id.label_user_aggregate_date);
			ImageView avatarView = (ImageView) v
					.findViewById(R.id.icon_user_aggregate_user_avatar);
			ImageView differentiatorImageLeft = (ImageView) v
					.findViewById(R.id.label_user_aggregate_differentiator_left_image);
			ImageView differentiatorImageCenter = (ImageView) v
					.findViewById(R.id.label_user_aggregate_differentiator_center_image);
			ImageView differentiatorImageRight = (ImageView) v
					.findViewById(R.id.label_user_aggregate_differentiator_right_image);
			TextView differentiatorTextLeft = (TextView) v
					.findViewById(R.id.label_user_aggregate_differentiator_left_text);
			TextView differentiatorTextRight = (TextView) v
					.findViewById(R.id.label_user_aggregate_differentiator_right_text);

			nameView.setText(userAggregate.getName());
			dateView.setText(userAggregate.getDate());
			differentiatorTextLeft.setText(userAggregate.getLeftText());
			differentiatorTextRight.setText(userAggregate.getRightText());

			// adds the user image.
			int resId;
			if (userAggregate.getAvatar() != null) {
				resId = v.getResources().getIdentifier(
						userAggregate.getAvatar(), "drawable",
						"com.commonsensenet.realfarm");
			} else {
				resId = R.drawable.farmer_default;
			}

			avatarView.setImageResource(resId);

			if (userAggregate.getLeftImage() != -1)
				differentiatorImageLeft.setImageResource(userAggregate
						.getLeftImage());
			if (userAggregate.getCenterImage() != -1)
				differentiatorImageCenter.setImageResource(userAggregate
						.getCenterImage());
			if (userAggregate.getRightImage() != -1)
				differentiatorImageRight.setImageResource(userAggregate
						.getRightImage());
		}
		return v;

	}
}
