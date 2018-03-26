package com.photozuri.photozuri.Views.V1.Stories;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.photozuri.photozuri.Adapter.StoryAlbumAdapter;
import com.photozuri.photozuri.Data.Models.StoryAlbum;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;

import java.util.ArrayList;

/**
 * Created by Eric on 2/21/2018.
 */

public class FragmentJourneys extends Fragment {
    private FloatingActionButton fab;
    private View view;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private StoryAlbumAdapter storyAlbumAdapter;
    //  private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private Fragment fragment = null;
    private ArrayList<StoryAlbum> storyAlbums = new ArrayList<>();
    private RelativeLayout relativeLayoutempty;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_journeys, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayoutempty = view.findViewById(R.id.relative_empty);
        relativeLayoutempty.setVisibility(View.GONE);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        initviews(getData());
    }

    private ArrayList<StoryAlbum> getData() {
        ArrayList<StoryAlbum> storyAlbums = new ArrayList<>();

        DbOperations dbOperations = new DbOperations(getContext());
        DbContentValues dbContentValues = new DbContentValues();
        Cursor cursor = dbOperations.select(DbConstants.TABLE_SA);
        if (cursor != null) {
            storyAlbums = dbContentValues.getSavedAlbums(cursor);
        }


        return storyAlbums;
    }

    private void initviews(ArrayList<StoryAlbum> storyAlbums) {


        storyAlbumAdapter = new StoryAlbumAdapter(getContext(), storyAlbums, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

                fragment = new FragmentJourneyDetails();
                Bundle bundle = new Bundle();
                bundle.putString("album", storyAlbums.get(position).getKEY_ID());
                bundle.putString("title", storyAlbums.get(position).getTitle());

                fragment.setArguments(bundle);
                popOutFragments();
                setUpView();


            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        storyAlbumAdapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storyAlbumAdapter);

        if (storyAlbumAdapter.getItemCount() < 0) {
            relativeLayoutempty.setVisibility(View.VISIBLE);
        }
    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private void startDialog() {


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_new_story, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("Create New Photo Story");

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid));
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;


        public CustomListener(Dialog dialog) {
            this.dialog = dialog;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText edtTitle, edtDescription;


            edtTitle = dialog.findViewById(R.id.edt_title);
            edtDescription = dialog.findViewById(R.id.edt_story);

            if (GeneralUtills.isFilledTextInputEditText(edtTitle)) {


                StoryAlbum storyAlbum = new StoryAlbum();
                storyAlbum.setDate(DateTimeUtils.getToday());
                storyAlbum.setCount("0");
                storyAlbum.setCover_image("null");
                String desc = "";
                if (edtDescription.getText().toString().isEmpty()) {

                } else {
                    desc = edtDescription.getText().toString();
                }
                storyAlbum.setDescription(desc);
                storyAlbum.setTitle(edtTitle.getText().toString());

                new DbContentValues.saveAlbum(storyAlbum, getContext(), new DbContentValues.MyInterfaceTitles() {
                    @Override
                    public void onComplete(int titleid) {
                        if (titleid != 9999) {
                            dialog.dismiss();

                            fragment = new FragmentJourneyDetails();
                            Bundle bundle = new Bundle();
                            bundle.putString("album", String.valueOf(titleid));

                            bundle.putString("title", edtTitle.getText().toString());
                            fragment.setArguments(bundle);
                            popOutFragments();
                            setUpView();
                        } else {

                        }
                    }
                }).execute();


            }


        }


    }

}
