package com.example.database_project_salesman.Order.Show_Order;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_project_salesman.Order.Entity.Orders;
import com.example.database_project_salesman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show_orders_activity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private DatabaseReference shops;
    private ArrayList<Orders> orderList;
    private FirebaseAuth auth;
    private FirebaseUser user;
    //search view
    private ActionBar actionBar;
    SearchManager searchManager;
    SearchView searchView;
    // Refresh menu item
    private MenuItem refreshMenuItem;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders_activity);
        actionBar = getActionBar();
        shops= FirebaseDatabase.getInstance().getReference("SHOP");
        recyclerView=findViewById(R.id.show_order_recycelrview);
        orderList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


    }
    //action bar this ahead
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_for_recylce_view, menu);
        // Associate searchable configuration with the SearchView
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.Action_Search)
                .getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                searchViewRecycleView (query );

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);

    }
    /**
     * On selecting action bar icons
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.Action_Search:
                // search action
                return true;

            case R.id.Action_Refresh:
                // refresh
                refreshMenuItem = item;
                // load the data from server
                new SyncData().execute();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Async task to load the data from server
     * **/
    private class SyncData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // set the progress bar view
            refreshMenuItem.setActionView(R.layout.action_progressbar);
            //.setActionView(R.drawable.progress_bar_ring);
            refreshMenuItem.expandActionView();
        }

        @Override
        protected String doInBackground(String... params) {
            // not making real request in this demo
            // for now we use a timer to wait for sometime
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            refreshMenuItem.collapseActionView();
            // remove the progress bar view
            refreshMenuItem.setActionView(null);
            searchViewRecycleView("");
        }
    };
    public void searchViewRecycleView(String search) {

        try{
            //Toast.makeText(delete_shop.this.toString(),Toast.LENGTH_LONG).show();
            ((show_order_rv_adaprter)recyclerView.getAdapter()).updateList(search,orderList);
        }
        catch (Exception e){}
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Query  query=FirebaseDatabase.getInstance().getReference("ORDERS").orderByChild("salesman").equalTo(user.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                orderList.clear();
                for(DataSnapshot shop:dataSnapshot.getChildren())
                {
                    orderList.add(shop.getValue(Orders.class));
                }
                show_order_rv_adaprter showOrderRvAdaprter=new show_order_rv_adaprter(orderList,show_orders_activity.this);
                recyclerView.setAdapter(showOrderRvAdaprter);
                showOrderRvAdaprter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(show_orders_activity.this, "Error in downloading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
