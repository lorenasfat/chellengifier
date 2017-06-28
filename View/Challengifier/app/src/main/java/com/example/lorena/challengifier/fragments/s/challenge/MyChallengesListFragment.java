package com.example.lorena.challengifier.fragments.s.challenge;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lorena.challengifier.R;
import com.example.lorena.challengifier.models.MyChallenge;
import com.example.lorena.challengifier.services.external.services.retrofit.interfaces.ChallengeService;
import com.example.lorena.challengifier.services.external.services.services.ApiChallengeService;
import com.example.lorena.challengifier.utils.adapters.MyChallengeListAdapter;
import com.example.lorena.challengifier.utils.communication.FlowAids;
import com.example.lorena.challengifier.utils.session.SessionUser;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChallengesListFragment extends Fragment {


    public MyChallengesListFragment() {
        // Required empty public constructor
    }

    public static final String SHOW_SCREEN = "MY_CHALLENGES_LIST_FRAGMENT_TAG";

    MyChallengeListAdapter listAdapter;
    List<MyChallenge> challenges = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_challenges_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Challenges");

        listAdapter = new MyChallengeListAdapter(getActivity().getBaseContext());
        listAdapter.setChallenges(challenges);

        ChallengeService service = ApiChallengeService.getService();
        Call<List<MyChallenge>> call = service.listMyChallenges(SessionUser.loggedInUser.getAspNetUserId());
        try {
            call.enqueue(new Callback<List<MyChallenge>>() {
                @Override
                public void onResponse(Call<List<MyChallenge>> call, Response<List<MyChallenge>> response) {
                    challenges.addAll(response.body());
                    listAdapter.notifyDataSetChanged();
                    FlowAids.MyChallengesBackup = challenges;
                    // The network call was a success and we got a response
                }

                @Override
                public void onFailure(Call<List<MyChallenge>> call, Throwable t) {
                    // the network call was a failure
                    // TODO: handle error
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListView list = (ListView) view.findViewById(R.id.challengeList);

        list.setAdapter(listAdapter);
        registerForContextMenu(list);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
                        MyChallenge selectedFromList = (MyChallenge) (listAdapter.getItem(position));
                        FlowAids.MyChallengeToView = selectedFromList;
                        RxBus.get().post(ViewMyChallengeFragment.SHOW_SCREEN, true);
                    }
                }
        );

        return view;
    }
}