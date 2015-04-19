package com.michaelfotiadis.androidcustomviewsdemos.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelfotiadis.androidcustomviews.textview.MyTypewriter;
import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.androidcustomviewsdemos.activity.MainActivity;

public class MyTypewriterFragment extends Fragment {
    // fragment initialisation parameter
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mPosition;

    private final long DEFAULT_DELAY = 150;
    private final String TAG = "TypewriterFragment";


    private String mTestText;

    private MyTypewriter mWriter;
    private Spinner mModeSpinner;
    private Spinner mDelaySpinner;

    public MyTypewriterFragment() {
        // empty constructor
        super();
    }

    public static MyTypewriterFragment newInstance(final int sectionNumber) {
        final MyTypewriterFragment fragment = new MyTypewriterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        // initialise the test text
        mTestText = getActivity().getString(R.string.test_text_lorem);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_type_writer, container, false);

        // Call the method to setup the mode spinner
        setupModeSpinner(view);

        // Call the method to setup the delay spinner
        setupDelaySpinner(view);

        mWriter = (MyTypewriter) view.findViewById(R.id.typewriter_view);

        return view;
    }

    private void setupModeSpinner(final View view) {
        // Initialise the spinner
        mModeSpinner = (Spinner) view.findViewById(R.id.mode_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.typewriter_mode_array, R.layout.typewriter_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mModeSpinner.setAdapter(adapter);
        mModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                animateTypewriter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    private void setupDelaySpinner(final View view) {
        // Initialise the spinner
        mDelaySpinner = (Spinner) view.findViewById(R.id.speed_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.typewriter_speed_array, R.layout.typewriter_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mDelaySpinner.setAdapter(adapter);
        mDelaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                animateTypewriter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mWriter != null)
            animateTypewriter();
    }

    private void animateTypewriter() {

        // pass it to the view
        mWriter.setInteractionMode(getModeFromSpinner());

        // set the delay
        mWriter.setCharacterDelay(getDelayFromSpinner());
        mWriter.animateText(mTestText);
        mWriter.invalidate();
    }

    /**
     * Gets the Interaction_Mode from the mode spinner position
     * @return INTERACTION_MODE enum
     */
    private MyTypewriter.INTERACTION_MODE getModeFromSpinner() {
        // find the mode from the spinner
        switch(mModeSpinner.getSelectedItemPosition()) {
            case 0:
                return MyTypewriter.INTERACTION_MODE.NONE;
            case 1:
                return MyTypewriter.INTERACTION_MODE.DOWN_TO_SPEED_UP;
            case 2:
                return MyTypewriter.INTERACTION_MODE.DOWN_TO_SHOW_ALL;
            default:
                return MyTypewriter.INTERACTION_MODE.NONE;
        }
    }

    /**
     * Gets the long delay from the spinner position
     * @return long delay
     */
    private long getDelayFromSpinner() {
        switch (mDelaySpinner.getSelectedItemPosition()) {
            case 0:
                return 50;
            case 1:
                return 150;
            case 2:
                return 300;
            case 3:
                return 500;
            case 4:
                return 1000;
            default:
                return DEFAULT_DELAY;
        }
    }
}
