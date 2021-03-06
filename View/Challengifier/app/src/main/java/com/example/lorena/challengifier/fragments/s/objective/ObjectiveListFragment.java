package com.example.lorena.challengifier.fragments.s.objective;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lorena.challengifier.R;
import com.example.lorena.challengifier.models.Objective;
import com.example.lorena.challengifier.services.external.services.retrofit.interfaces.ObjectiveService;
import com.example.lorena.challengifier.services.external.services.services.ApiObjectiveService;
import com.example.lorena.challengifier.utils.adapters.ObjectiveListAdapter;
import com.example.lorena.challengifier.utils.communication.FlowAids;
import com.example.lorena.challengifier.utils.constants.ObjStatus;
import com.example.lorena.challengifier.utils.session.SessionUser;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObjectiveListFragment extends Fragment {
    public static final String SHOW_SCREEN = "OBJECTIVE_LIST_TAG";

    ObjectiveListAdapter listAdapter;
    List<Objective> objectives = new ArrayList<>();
    static boolean mIsRestoredFromBackstack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_objective_list, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Objectives");
        FlowAids.BackUpTitle="Objectives";
        setHasOptionsMenu(true);

        mIsRestoredFromBackstack = false;

        listAdapter = new ObjectiveListAdapter(getActivity().getApplicationContext(), objectives);

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteSearch);

        loadObjectives();

        ListView list = (ListView) view.findViewById(R.id.objectiveList);

        list.setAdapter(listAdapter);
        registerForContextMenu(list);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        Objective selectedFromList = (Objective) (listAdapter.getItem(position));
                        FlowAids.ObjectiveToView = selectedFromList;
                        RxBus.get().post(ViewObjectiveFragment.SHOW_SCREEN, true);
                    }
                });

        searchTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsRestoredFromBackstack = true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "DELETE") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Objective obj = (Objective) listAdapter.getItem(info.position);

            ObjectiveService service = ApiObjectiveService.getService();
            Call<ResponseBody> call = service.deleteObjective(obj.getId());
            try {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getActivity().getApplicationContext(), "Objective deleted!", Toast.LENGTH_LONG).show();
                        RxBus.get().post(ObjectiveListFragment.SHOW_SCREEN, true);
                        // The network call was a success and we got a response
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Oops! :(", Toast.LENGTH_LONG).show();
                        // the network call was a failure
                        // TODO: handle error
                        t.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.objective_menu, menu);
    }

    private void loadObjectives() {
        objectives.clear();
        ObjectiveService service = ApiObjectiveService.getService();
        Call<List<Objective>> call = service.listObjectives(SessionUser.getLoggedInUser().getAspNetUserId());
        try {
            call.enqueue(new Callback<List<Objective>>() {
                @Override
                public void onResponse(Call<List<Objective>> call, Response<List<Objective>> response) {
                    objectives.addAll(response.body());
                    listAdapter.setObjectives(objectives);
                    listAdapter.notifyDataSetChanged();
                    FlowAids.ObjectivesBackup = objectives;
                    // The network call was a success and we got a response
                }

                @Override
                public void onFailure(Call<List<Objective>> call, Throwable t) {
                    // the network call was a failure
                    // TODO: handle error
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        listAdapter.notifyDataSetChanged();
    }

    public void showForReview(){
        List<Objective> objectives = FlowAids.ObjectivesBackup;
        List<Objective> sorted = new ArrayList<>();

        for (Objective objective:objectives) {
            if(objective.getStatus() == ObjStatus.For_Review.ordinal())
                sorted.add(objective);
        }
        listAdapter.setObjectives(sorted);
        listAdapter.notifyDataSetChanged();
    }

    public void showOngoing(){
        List<Objective> objectives = FlowAids.ObjectivesBackup;
        List<Objective> sorted = new ArrayList<>();

        for (Objective objective:objectives) {
            if(objective.getStatus() == ObjStatus.Ongoing.ordinal())
                sorted.add(objective);
        }
        listAdapter.setObjectives(sorted);
        listAdapter.notifyDataSetChanged();
    }

    public void showNotStarted(){
        List<Objective> objectives = FlowAids.ObjectivesBackup;
        List<Objective> sorted = new ArrayList<>();

        for (Objective objective:objectives) {
            if(objective.getStatus() == ObjStatus.Not_Active.ordinal())
                sorted.add(objective);
        }
        listAdapter.setObjectives(sorted);
        listAdapter.notifyDataSetChanged();
    }

    public void showDue(){
        List<Objective> objectives = FlowAids.ObjectivesBackup;
        List<Objective> sorted = new ArrayList<>();

        for (Objective objective:objectives) {
            if( DateUtils.isToday(objective.getDeadline().getTime())){
                sorted.add(objective);
            }
        }
        listAdapter.setObjectives(sorted);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadObjectives();
                return true;
            case R.id.action_review:
                showForReview();
                return true;
            case R.id.action_due:
                showDue();
                return true;
            case R.id.action_notstarted:
                showNotStarted();
                return true;
            case R.id.action_ongoing:
                showOngoing();
                return true;
            default:
                return false;
        }
    }
}
