package com.xavigil.uniquewords.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xavigil.uniquewords.asynctask.IReadFileTask;
import com.xavigil.uniquewords.R;
import com.xavigil.uniquewords.model.UniqueWord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
        implements IReadFileTask{

    public enum SortOrder{
        DEFAULT,
        ALPHABETICALLY,
        COUNT
    }

    private SortOrder mSortOrder;

    private HashMap<String, UniqueWord> mHashMap;
    private List<String> mArrayList;                    // SortOrder.DEFAULT
    private List<UniqueWord> mAlphabeticallySortedList; // SortOrder.ALPHABETICALLY
    private List<UniqueWord> mCountSortedList;          // SortOrder.COUNT

    public ListAdapter(){

        mSortOrder = SortOrder.DEFAULT;
        mHashMap = new HashMap<>();
        mArrayList = new ArrayList<>();
        mAlphabeticallySortedList = null;
        mCountSortedList = null;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UniqueWord uw;
        if(mSortOrder == SortOrder.ALPHABETICALLY){
            uw = mAlphabeticallySortedList.get(position);
        }
        else if(mSortOrder == SortOrder.COUNT){
            uw = mCountSortedList.get(position);
        }
        else {
            String word = mArrayList.get(position);
            uw = mHashMap.get(word);
        }
        holder.bindUniqueWord(uw);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView mWord;
        public final TextView mCount;

        public ViewHolder(View view){
            super(view);
            mWord = (TextView)view.findViewById(R.id.txtWord);
            mCount = (TextView)view.findViewById(R.id.txtCount);
        }

        public void bindUniqueWord(UniqueWord uniqueWord){
            mWord.setText(uniqueWord.word);
            mCount.setText(String.valueOf(uniqueWord.count));
        }
    }

    public void setSortOrder(SortOrder sortOrder){
        int ordinal = sortOrder.ordinal();
        if(ordinal<0 || ordinal>SortOrder.values().length) return;
        if(ordinal == mSortOrder.ordinal()) return;
        updateSortOrder(sortOrder);
    }

    private void updateSortOrder(SortOrder newOrder){
        mSortOrder = newOrder;
        if(mSortOrder == SortOrder.ALPHABETICALLY && mAlphabeticallySortedList == null){
            mAlphabeticallySortedList = sortCollection(mHashMap.values(),newOrder);
        }
        else if(mSortOrder == SortOrder.COUNT && mCountSortedList == null){
            mCountSortedList = sortCollection(mHashMap.values(),newOrder);
        }
        notifyDataSetChanged();
    }

    private List<UniqueWord> sortCollection(Collection<UniqueWord> c, SortOrder order){
        List<UniqueWord> list = new ArrayList<>(c);
        if(order == SortOrder.ALPHABETICALLY){
            Collections.sort(list, UniqueWord.getWordComparator());
        }
        else if(order == SortOrder.COUNT){
            Collections.sort(list, UniqueWord.getCountComparator());
        }
        return list;
    }

    private void addWord(String word)
    {
        UniqueWord uw = mHashMap.get(word);
        if(uw == null){
            mHashMap.put(word, new UniqueWord(word, 1));
            mArrayList.add(word);
        }
        else{
            uw.count++;
            mHashMap.put(word,uw);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onNextWord(String word) {
        addWord(word.toLowerCase());
    }
}
