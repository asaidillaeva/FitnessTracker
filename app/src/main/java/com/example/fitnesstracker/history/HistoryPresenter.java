package com.example.fitnesstracker.history;

import android.content.Context;
import android.os.AsyncTask;

import com.example.fitnesstracker.RunItem;
import com.example.fitnesstracker.data.RunDao;
import com.example.fitnesstracker.data.RunDatabase;

import java.util.List;

public class HistoryPresenter implements HistoryContract.Presenter {

    public List<RunItem> listOfRunItems;
    public RunDao runDao;
    public RunItemsAsyncTask runItemsAsyncTask;
    private Context applicationContext;
    public HistoryContract.View view;


    public HistoryPresenter(Context applicationContext, HistoryContract.View view) {
        this.applicationContext = applicationContext;
        this.view = view;
    }

    @Override
    public int getRunItemsSize() {
        setRunItems();
        return listOfRunItems.size();
    }

    @Override
    public List<RunItem> getListOfRunItems() {
        setRunItems();
        return listOfRunItems;
    }

    @Override
    public void setRunItems() {
        if (runItemsAsyncTask == null || runItemsAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
            runItemsAsyncTask = new RunItemsAsyncTask();
            runItemsAsyncTask.execute();
        }
    }

    @Override
    public RunItem getRunItem(int position) {
        return listOfRunItems.get(position);
    }

    private class RunItemsAsyncTask extends AsyncTask<Void, Void, List<RunItem>> {

        //        List<RunItem> listOfRunItems;
        @Override
        protected List<RunItem> doInBackground(Void... voids) {

            RunDatabase database = RunDatabase.getDatabase(applicationContext);
            runDao = database.runDao();
            listOfRunItems = runDao.getAllRunItems();
            return listOfRunItems;
        }

        @Override
        protected void onPostExecute(List<RunItem> products) {
            super.onPostExecute(products);
            view.setData();
        }
    }
}
