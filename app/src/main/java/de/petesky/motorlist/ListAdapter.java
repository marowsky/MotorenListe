package de.petesky.motorlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import de.petesky.motorlist.databinding.ItemWordBinding;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.Comparator;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class ListAdapter extends SortedListAdapter<ListModel> implements FastScrollRecyclerView.SectionedAdapter{

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.valueOf(getItem(position).getAkz().substring(5));
    }

    public interface Listener {
        void onExampleModelClicked(ListModel model);
    }

    private final Listener mListener;

    public ListAdapter(Context context, Comparator<ListModel> comparator, Listener listener) {
        super(context, ListModel.class, comparator);
        mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder<? extends ListModel>
        onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        final ItemWordBinding binding = ItemWordBinding.inflate(inflater, parent, false);
        return new AkzViewHolder(binding, mListener);
    }
}
