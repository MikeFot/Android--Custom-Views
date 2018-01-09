package com.michaelfotiadis.androidcustomviewsdemos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelfotiadis.androidcustomviews.text.Typewriter;
import com.michaelfotiadis.androidcustomviewsdemos.R;

public class TypewriterFragment extends Fragment {
    private static final long DEFAULT_DELAY = 150;

    private Typewriter mWriter;
    private Spinner mModeSpinner;
    private Spinner mDelaySpinner;

    public static TypewriterFragment newInstance() {
        return new TypewriterFragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_type_writer, container, false);

        // Call the method to setup the mode spinner
        setupModeSpinner(view);

        // Call the method to setup the delay spinner
        setupDelaySpinner(view);

        mWriter = (Typewriter) view.findViewById(R.id.typewriter_view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mWriter != null)
            animateTypewriter();
    }

    private void setupModeSpinner(final View view) {
        // Initialise the spinner
        mModeSpinner = (Spinner) view.findViewById(R.id.mode_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.typewriter_mode_array, R.layout.demo_spinner);
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
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.typewriter_speed_array,
                R.layout.demo_spinner);
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

    private void animateTypewriter() {

        // pass it to the view
        mWriter.setInteractionMode(getModeFromSpinner());

        // set the delay
        mWriter.setCharacterDelay(getDelayFromSpinner());
        mWriter.animateText(getString(R.string.test_text_lorem));
        mWriter.invalidate();
    }

    /**
     * Gets the Interaction_Mode from the mode spinner position
     *
     * @return INTERACTION_MODE enum
     */
    private Typewriter.INTERACTION_MODE getModeFromSpinner() {
        // find the mode from the spinner
        switch (mModeSpinner.getSelectedItemPosition()) {
            case 0:
                return Typewriter.INTERACTION_MODE.NONE;
            case 1:
                return Typewriter.INTERACTION_MODE.DOWN_TO_SPEED_UP;
            case 2:
                return Typewriter.INTERACTION_MODE.DOWN_TO_SHOW_ALL;
            default:
                return Typewriter.INTERACTION_MODE.NONE;
        }
    }

    /**
     * Gets the long delay from the spinner position
     *
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
