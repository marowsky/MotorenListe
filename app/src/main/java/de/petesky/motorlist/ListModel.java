package de.petesky.motorlist;

import android.support.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created with Android Studio
 * User: PeteSky
 * Date: 08/11/17
 */
public class ListModel implements SortedListAdapter.ViewModel {

    private final long mId;
    private final int mRank;
    private final String mAkz;
    private final String mDetail;

    public ListModel(long id, int rank, String akz, String detail) {
        mId = id;
        mRank = rank;
        mAkz = akz;
        mDetail = detail;
    }

    public long getId() {
        return mId;
    }

    public int getRank() {
        return mRank;
    }

    public String getAkz() {
        return mAkz;
    }

    public String getDetail(){
        return mDetail;
    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof ListModel) {
            final ListModel wordModel = (ListModel) item;
            return wordModel.mId == mId;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof ListModel) {
            final ListModel other = (ListModel) item;
            if (mRank != other.mRank) {
                return false;
            }
            return mAkz != null ? mAkz.equals(other.mAkz) : other.mAkz == null;
        }
        return false;
    }
}
