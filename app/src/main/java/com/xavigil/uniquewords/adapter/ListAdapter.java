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
import java.util.HashMap;
import java.util.LinkedList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
        implements IReadFileTask{

    public enum SortOrder{
        DEFAULT,
        ALPHABETICALLY,
        APPEARANCES
    }

    private SortOrder mSortOrder;

    private HashMap<String, UniqueWord> mHashMap;
    private ArrayList<String> mArrayList; // SortOrderDefault

    public ListAdapter(){

        mSortOrder = SortOrder.DEFAULT;
        mHashMap = new HashMap<>();
        mArrayList = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String word = mArrayList.get(position);
        holder.bindUniqueWord(mHashMap.get(word));
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
            mCount.setText(String.valueOf(uniqueWord.appearances));
        }
    }

    public void setSortOrder(SortOrder sortOrder){
        int ordinal = sortOrder.ordinal();
        if(ordinal<0 || ordinal>SortOrder.values().length) return;
        if(ordinal == mSortOrder.ordinal()) return;
        mSortOrder = sortOrder;
        notifyDataSetChanged();
    }

    private void addWord(String word)
    {
        UniqueWord uw = mHashMap.get(word);
        if(uw == null){
            mHashMap.put(word, new UniqueWord(word, 1));
            mArrayList.add(word);
        }
        else{
            uw.appearances++;
            mHashMap.put(word,uw);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onNextWord(String word) {
        addWord(word.toLowerCase());
    }
}
