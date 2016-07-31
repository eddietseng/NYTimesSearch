package com.eddietseng.nytimessearch.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.eddietseng.nytimessearch.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by eddietseng on 7/30/16.
 */
public class ArticleFilterFragment extends DialogFragment
        implements SelectDateFragment.SelectDateDialogListener,
        View.OnClickListener {

    @BindView(R.id.etBeginDate) EditText etDate;
    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.radio_ND_group) RadioGroup ndGroup;
    @BindView(R.id.btnSave) Button btnSave;
    private Unbinder unbinder;

    public interface ArticleFilterDialogListener {
        void onFinishEditDialog(HashMap<String,String> params);
    }

    public ArticleFilterFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ArticleFilterFragment newInstance(String title) {
        ArticleFilterFragment frag = new ArticleFilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_filter, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
//        etDate = (EditText) view.findViewById(R.id.etBeginDate);
//        spSortOrder = (Spinner) view.findViewById(R.id.spSortOrder);
//        ndGroup = (RadioGroup) view.findViewById(R.id.radio_ND_group);
//        btnSave = (Button) view.findViewById(R.id.btnSave);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        btnSave.setOnClickListener(this);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Search Filter");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etDate.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    // Call this method to launch the edit dialog
    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        SelectDateFragment selectDateFragment = new SelectDateFragment();
        // SETS the target fragment for use later when sending results
        selectDateFragment.setTargetFragment(ArticleFilterFragment.this, 300);
        selectDateFragment.show(fm, "DatePicker");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        etDate.setText(inputText);
    }

    @Override
    public void onClick(View v) {
        HashMap<String,String> params = new HashMap<>();

        if( etDate.getText().length() > 0 ) {
            String date = etDate.getText().toString();
            date = date.replace(" ","").replace("/","");

            params.put("begin_date",date);
        }

        params.put("sort",spSortOrder.getSelectedItem().toString().toLowerCase());

        if(ndGroup.getCheckedRadioButtonId()==-1)
            Log.i("ArticleFilterFragment", "NEGATIVE 1" );
        else {
            switch (ndGroup.getCheckedRadioButtonId()) {
                case 2131558525:
                    params.put("fq","news_desk:(\"Arts\")");
                    break;

                case 2131558526:
                    params.put("fq","news_desk:(\"Fashion & Style\")");
                    break;

                case 2131558527:
                    params.put("fq","news_desk:(\"Sports\")");
                    break;

                default:
                    Log.i("ArticleFilterFragment", "can't handle news desk: " +
                            ndGroup.getCheckedRadioButtonId());
            }
        }

        ArticleFilterDialogListener listener = (ArticleFilterDialogListener)getActivity();
        listener.onFinishEditDialog(params);

        Log.i("ArticleFilterFragment", "filter string: " +
                params.toString());

        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
