package br.com.cozinheirodelivery.domotica_new.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import br.com.cozinheirodelivery.domotica_new.Fragments.EntradaFragment;
import br.com.cozinheirodelivery.domotica_new.Fragments.FragmentDetailFragment;
import br.com.cozinheirodelivery.domotica_new.Fragments.LavanderiaFragment;
import br.com.cozinheirodelivery.domotica_new.Fragments.PiscinaFragment;
import br.com.cozinheirodelivery.domotica_new.Fragments.SalaFragment;
import br.com.cozinheirodelivery.domotica_new.R;
import br.com.cozinheirodelivery.domotica_new.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * An activity representing a list of Fragments. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link FragmentDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class FragmentListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane;
    List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_list);
        initFireBase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        GoogleApiAvailability test = GoogleApiAvailability.getInstance();
        if(!(test.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)){
            test.getErrorDialog(this,test.isGooglePlayServicesAvailable(this),10);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(GONE);
        View recyclerView = findViewById(R.id.fragment_list);
        if(mList == null){
            mList = new ArrayList<>();
            mList.add(EntradaFragment.newInstance(1));
            mList.add(SalaFragment.newInstance(1));
            mList.add(LavanderiaFragment.newInstance(1));
            mList.add(PiscinaFragment.newInstance(1));
        }
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.fragment_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }


    private void initFireBase(){
        //FirebaseApp.initializeApp(this);
        //FirebaseApp.getInstance().initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("Domotica");
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mList));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Fragment> mValues;

        public SimpleItemRecyclerViewAdapter(List<Fragment> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).toString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        /*arguments.putString(FragmentDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        FragmentDetailFragment fragment = new FragmentDetailFragment();
                        fragment.setArguments(arguments);*/
                        List<Fragment> frag = getSupportFragmentManager().getFragments();
                        if(frag != null) {
                            for (Fragment aux : frag) {
                                try{
                                    aux.onDestroy();
                                }catch (Exception e){

                                }
                            }
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_detail_container, holder.mItem,holder.mItem.toString())
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, FragmentDetailActivity.class);
                        //intent.putExtra(FragmentDetailFragment.ARG_ITEM_ID, holder.mItem);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public Fragment mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability test = GoogleApiAvailability.getInstance();
        if(!(test.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)){
            test.getErrorDialog(this,test.isGooglePlayServicesAvailable(this),10);
        }
    }
}
