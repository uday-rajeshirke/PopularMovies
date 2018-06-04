package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

public class SortingOptionListDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private CheckedTextView mSortingOptionPopular, mSortingOptionTopRated;
    private SortingOptionListener mOptionListener;
    private PreferencesHelper mPreferencesHelper;

    public static SortingOptionListDialogFragment newInstance() {
        return new SortingOptionListDialogFragment();
    }

    public interface SortingOptionListener {
        void onSortingOptionClicked(int option);
    }

    public void setOptionListener(SortingOptionListener optionListener) {
        this.mOptionListener = optionListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getContext()!=null)
            mPreferencesHelper = new PreferencesHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sortingoption_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mSortingOptionPopular = view.findViewById(R.id.ctv_sorting_option_popular);
        mSortingOptionTopRated = view.findViewById(R.id.ctv_sorting_option_top_rated);

        switch (mPreferencesHelper.getSortingOptionPreferences("SelectedSortingOption")) {

            case 1:
                selectedOptionPopular();
                break;
            case 2:
                selectedOptionTopRated();
                break;
        }

        mSortingOptionPopular.setOnClickListener(this);
        mSortingOptionTopRated.setOnClickListener(this);

    }

    private void selectedOptionPopular() {
        mSortingOptionPopular.setChecked(true);
        mSortingOptionPopular.setCheckMarkDrawable(R.drawable.ic_check_24dp);
        mSortingOptionTopRated.setChecked(false);
        mSortingOptionTopRated.setCheckMarkDrawable(null);
    }

    private void selectedOptionTopRated() {
        mSortingOptionPopular.setChecked(false);
        mSortingOptionPopular.setCheckMarkDrawable(null);
        mSortingOptionTopRated.setChecked(true);
        mSortingOptionTopRated.setCheckMarkDrawable(R.drawable.ic_check_24dp);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ctv_sorting_option_popular:
                selectedOptionPopular();
                mOptionListener.onSortingOptionClicked(1);
                break;
            case R.id.ctv_sorting_option_top_rated:
                selectedOptionTopRated();
                mOptionListener.onSortingOptionClicked(2);
                break;
        }

    }

}
