package com.example.fitnesstracker.history;

import com.example.fitnesstracker.RunItem;

import java.util.List;

public interface HistoryContract  {

     interface View{
         void showToast(String message);

         void setData();

    }

     interface Presenter{

         List<RunItem> getListOfRunItems();
         int getRunItemsSize();

         void setRunItems();

         RunItem getRunItem(int position);

    }
}
