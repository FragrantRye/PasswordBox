package cn.xfz.passwordbox;

import android.content.Context;
import android.view.*;
import cn.xfz.passwordbox.sql.RecodeItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cn.xfz.passwordbox.R;

public class ListItemAdapter extends ArrayAdapter<RecodeItem> {
    private int newResourceId;
    ListItemAdapter(Context context, int resourceId, RecodeItem[] cityList){
        super(context, resourceId, cityList);
        newResourceId = resourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        RecodeItem recodes = getItem(position);
        View view = LayoutInflater.from (getContext ()).inflate (newResourceId, parent, false);

        TextView application_view = view.findViewById (R.id.application_item);
        TextView username_view = view.findViewById (R.id.username_item);

        application_view.setText (recodes.getApplication());
        username_view.setText (recodes.getUsername());
        return view;
    }
}
