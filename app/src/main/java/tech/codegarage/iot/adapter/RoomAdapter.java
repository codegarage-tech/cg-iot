package tech.codegarage.iot.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.security.InvalidParameterException;

import tech.codegarage.iot.model.Room;
import tech.codegarage.iot.viewholder.RoomViewHolder;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RoomAdapter extends RecyclerArrayAdapter<Room> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public RoomAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new RoomViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }
}