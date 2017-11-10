package de.petesky.motorlist;

import android.support.annotation.NonNull;

import de.petesky.motorlist.databinding.ItemWordBinding;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

public class AkzViewHolder extends SortedListAdapter.ViewHolder<ListModel> {

    private final ItemWordBinding mBinding;

    public AkzViewHolder(ItemWordBinding binding, ListAdapter.Listener listener) {
        super(binding.getRoot());
        binding.setListener(listener);

        mBinding = binding;
    }

    @Override
    protected void performBind(@NonNull ListModel item) {
        mBinding.setModel(item);
    }
}
