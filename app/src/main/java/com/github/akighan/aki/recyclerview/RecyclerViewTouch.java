package com.github.akighan.aki.recyclerview;

public interface RecyclerViewTouch {
    void onItemMove (int fromPosition, int toPosition);
    void onItemDismiss (int position);
}
