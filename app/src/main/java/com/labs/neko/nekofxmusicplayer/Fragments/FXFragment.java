package com.labs.neko.nekofxmusicplayer.Fragments;

import android.graphics.Typeface;
import android.media.PlaybackParams;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.labs.neko.nekofxmusicplayer.Globals;
import com.labs.neko.nekofxmusicplayer.R;

public class FXFragment extends Fragment {

    int textColorAccent, textColorBlack;
    private Globals globals = Globals.getInstance();

    private Switch switchEqualizer;
    private Switch switchBass;
    private Switch switchVirtualizer;
    private Switch switchPitch;
    private Switch switchSpeed;
    private Switch switchVolumeG;
    private Switch switchBPM;
    private Switch switchLoudness;
    private Switch switchEnvironmental;

    private LinearLayout layoutFX;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fx, container, false);
        layoutFX = view.findViewById(R.id.layoutFX);

        final Spinner spinner = view.findViewById(R.id.spinner_FX);
        String spinnerList[] = {"Equalizer","Reverberation","Effects","Playback"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_1,spinnerList);
        spinner.setAdapter(adapter);

        textColorAccent = ContextCompat.getColor(view.getContext(), R.color.colorAccent);
        textColorBlack = ContextCompat.getColor(view.getContext(), R.color.colorBlack);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layoutFX.removeAllViews();
                switch (position){
                    case 0:
                        setLayoutEqualizer();
                        break;
                    case 1:
                        //setLayoutReverb();
                        setLayoutEnvironmentalReverb();
                        break;
                    case 2:
                        //setLayoutLoudness();
                        setLayoutBass();
                        setLayoutVirtualizer();
                        break;
                    case 3:
                        setLayoutVolumeG();
                        setLayoutPitch();
                        setLayoutSpeed();
                        setLayoutBPM();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public LinearLayout setOptionHeaderLayout(String text, Switch sw) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lpList = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lpList.bottomMargin = 20;
        lpList.topMargin = 20;
        linearLayout.setLayoutParams(lpList);

        RelativeLayout relativeLayout = new RelativeLayout(getActivity());

        RelativeLayout.LayoutParams lpText = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lpText.addRule(RelativeLayout.ALIGN_PARENT_START);
        lpText.setMargins(0, 20, 0, 20);

        RelativeLayout.LayoutParams lpSwitch = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lpSwitch.addRule(RelativeLayout.ALIGN_PARENT_END);
        lpSwitch.setMargins(0, 20, 0, 20);

        TextView textFXOption = new TextView(getActivity());

        textFXOption.setText(text);
        textFXOption.setTextSize(15);
        textFXOption.setTypeface(null, Typeface.BOLD);
        textFXOption.setTextColor(textColorBlack);
        textFXOption.setAlpha(0.7f);
        textFXOption.setId(View.generateViewId());

        textFXOption.setLayoutParams(lpText);
        sw.setLayoutParams(lpSwitch);

        relativeLayout.addView(textFXOption);
        relativeLayout.addView(sw);

        linearLayout.addView(relativeLayout);

        return linearLayout;
    }

    public void setLayoutEqualizer(){
        switchEqualizer = new Switch(getActivity());
        switchEqualizer.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Equalizer",switchEqualizer);
        layoutFX.addView(linearLayout);
        globals.getEqualizer().setEnabled(true);
        // Show frequency text views
        final LinearLayout layoutEQ = drawEqualizer();
        switchEqualizer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    linearLayout.addView(layoutEQ);
                }else{
                    linearLayout.removeView(layoutEQ);
                }
            }
        });
        if(switchEqualizer.isChecked()){
            linearLayout.addView(layoutEQ);
        }
    }

    public LinearLayout drawEqualizer(){
        final Equalizer equalizer = globals.getEqualizer();
        LinearLayout layoutEQ = new LinearLayout(getActivity());
        layoutEQ.setOrientation(LinearLayout.VERTICAL);

        short numFreqBands = equalizer.getNumberOfBands();
        final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = equalizer.getBandLevelRange()[1];
        for(short i=0; i<numFreqBands; i++){
            final short eqBandIndex = i;
            TextView tvFreqHeader = new TextView(getActivity());
            tvFreqHeader.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tvFreqHeader.setGravity(Gravity.CENTER_HORIZONTAL);
            tvFreqHeader.setText((equalizer.getCenterFreq(i)/1000)+"Hz");
            tvFreqHeader.setTextColor(textColorBlack);
            layoutEQ.addView(tvFreqHeader);
            // Set text seekbar and layout
            LinearLayout seekBarRowLayout = new LinearLayout(getActivity());
            seekBarRowLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView tvLowerEqBandLevel = new TextView(getActivity());
            tvLowerEqBandLevel.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tvLowerEqBandLevel.setText((lowerEqualizerBandLevel/100)+"dB");
            tvLowerEqBandLevel.setTextColor(textColorBlack);
            TextView tvUpperEqBandLevel = new TextView(getActivity());
            tvUpperEqBandLevel.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tvUpperEqBandLevel.setText((upperEqualizerBandLevel/100)+"dB");
            tvUpperEqBandLevel.setTextColor(textColorBlack);
            // Create seek bars
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;
            layoutParams.bottomMargin = 60;
            SeekBar seekBar = new SeekBar(getActivity());
            seekBar.setId(i);
            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel-lowerEqualizerBandLevel);
            seekBar.setProgress(equalizer.getBandLevel(i));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    equalizer.setBandLevel(eqBandIndex,(short)(i+lowerEqualizerBandLevel));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            seekBarRowLayout.addView(tvLowerEqBandLevel);
            seekBarRowLayout.addView(seekBar);
            seekBarRowLayout.addView(tvUpperEqBandLevel);
            layoutEQ.addView(seekBarRowLayout);
        }
        return layoutEQ;
    }

    public void setLayoutBass() {
        final BassBoost bassBoost = globals.getBassBoost();
        switchBass = new Switch(getActivity());
        switchBass.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Bass Booster", switchBass);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        if (bassBoost.getStrengthSupported()) {
            bassBoost.setEnabled(true);
            bassBoost.setStrength(bassBoost.getRoundedStrength());
            seekBar.setMax(1000);
            seekBar.setProgress(0);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    short strength = (short)progress;
                    if (globals.getMediaPlayer().isPlaying()) {
                        bassBoost.setStrength(strength);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            switchBass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.addView(seekBar);
                    } else {
                        linearLayout.removeView(seekBar);
                        bassBoost.setStrength((short)0);
                    }
                }
            });
            if (switchBass.isChecked()) {
                linearLayout.addView(seekBar);
            }
        }
    }

    public void setLayoutVirtualizer() {
        final Virtualizer virtualizer = globals.getVirtualizer();
        switchVirtualizer = new Switch(getActivity());
        switchVirtualizer.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Virtualizer", switchVirtualizer);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        if (virtualizer.getStrengthSupported()) {
            virtualizer.setStrength(virtualizer.getRoundedStrength());
            seekBar.setMax(1000);
            seekBar.setProgress(0);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    short strength = (short)progress;
                    if (globals.getMediaPlayer().isPlaying()) {
                        virtualizer.setStrength(strength);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            switchVirtualizer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.addView(seekBar);
                        virtualizer.setEnabled(true);
                    } else {
                        linearLayout.removeView(seekBar);
                        virtualizer.setEnabled(false);
                    }
                }
            });
            if (switchVirtualizer.isChecked()) {
                virtualizer.setEnabled(true);
                linearLayout.addView(seekBar);
            }
        }
    }

    public void setLayoutSpeed() {
        switchSpeed = new Switch(getActivity());
        switchSpeed.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Speed", switchSpeed);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            final PlaybackParams playbackParams = new PlaybackParams();
            seekBar.setMax(200);
            seekBar.setProgress(100);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    float current = (float) progress / 100;
                    if (current > 0.1 && globals.getMediaPlayer().isPlaying()) {
                        playbackParams.setSpeed(current);
                        globals.getMediaPlayer().setPlaybackParams(playbackParams);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            switchSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.addView(seekBar);
                    } else {
                        linearLayout.removeView(seekBar);
                        if (globals.getMediaPlayer().isPlaying()) {
                            globals.getMediaPlayer().setPlaybackParams(playbackParams.setSpeed(1));
                        }
                    }
                }
            });
            if (switchSpeed.isChecked()) {
                linearLayout.addView(seekBar);
            }
        }
    }

    public void setLayoutPitch() {
        switchPitch = new Switch(getActivity());
        switchPitch.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Pitch", switchPitch);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            globals.getBassBoost().setEnabled(true);
            final PlaybackParams playbackParams = new PlaybackParams();
            seekBar.setMax(200);
            seekBar.setProgress(100);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    float current = (float) progress / 100;
                    if (current > 0.1 && globals.getMediaPlayer().isPlaying()) {
                        playbackParams.setPitch(current);
                        globals.getMediaPlayer().setPlaybackParams(playbackParams);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            switchPitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.addView(seekBar);
                    } else {
                        linearLayout.removeView(seekBar);
                        if (globals.getMediaPlayer().isPlaying()) {
                            globals.getMediaPlayer().setPlaybackParams(playbackParams.setPitch(1));
                        }
                    }
                }
            });
            if (switchPitch.isChecked()) {
                linearLayout.addView(seekBar);
            }
        }
    }

    public void setLayoutVolumeG() {
        switchVolumeG = new Switch(getActivity());
        switchVolumeG.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("General Volume", switchVolumeG);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        final SeekBar seekBarVolume = new SeekBar(getActivity());
        seekBarVolume.setMax(100);
        seekBar.setProgress(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                float current = (float) progress / 100;
                if (globals.getMediaPlayer().isPlaying()) {
                    globals.getMediaPlayer().setVolume(current, current);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        switchVolumeG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    linearLayout.addView(seekBar);
                } else {
                    linearLayout.removeView(seekBar);
                    if (globals.getMediaPlayer().isPlaying()) {
                        globals.getMediaPlayer().setVolume(1, 1);
                    }
                }
            }
        });
        if (switchVolumeG.isChecked()) {
            linearLayout.addView(seekBar);
        }
    }


    public void setLayoutBPM() {
        switchBPM= new Switch(getActivity());
        switchBPM.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("BPM", switchBPM);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            final PlaybackParams playbackParams = new PlaybackParams();
            seekBar.setMax(200);
            seekBar.setProgress(100);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    float current = (float) progress / 100;
                    if (current > 0.1 && globals.getMediaPlayer().isPlaying()) {
                        playbackParams.setSpeed(current);
                        playbackParams.setPitch(current);
                        globals.getMediaPlayer().setPlaybackParams(playbackParams);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            switchBPM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.addView(seekBar);
                    } else {
                        linearLayout.removeView(seekBar);
                        if (globals.getMediaPlayer().isPlaying()) {
                            playbackParams.setPitch(1);
                            playbackParams.setSpeed(1);
                            globals.getMediaPlayer().setPlaybackParams(playbackParams);
                        }
                    }
                }
            });
            if (switchBPM.isChecked()) {
                linearLayout.addView(seekBar);
            }
        }
    }

    /*
    public void setLayoutReverb(){

        final PresetReverb presetReverb = globals.getPresetReverb();
        presetReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
        presetReverb.setEnabled(true);
        globals.getMediaPlayer().attachAuxEffect(presetReverb.getId());
        switchReverb = new Switch(getActivity());
        switchReverb.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Presets", switchReverb);
        layoutFX.addView(linearLayout);

        Spinner spinner = new Spinner(getActivity());
        String options[] = {"None","Small room","Medium room","Large room", "Medium hall","Large hall","Plate"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,options);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        presetReverb.setPreset(PresetReverb.PRESET_NONE);
                        break;
                    case 1:
                        presetReverb.setPreset(PresetReverb.PRESET_SMALLROOM);
                        break;
                    case 2:
                        presetReverb.setPreset(PresetReverb.PRESET_MEDIUMROOM);
                        break;
                    case 3:
                        presetReverb.setPreset(PresetReverb.PRESET_LARGEROOM);
                        break;
                    case 4:
                        presetReverb.setPreset(PresetReverb.PRESET_MEDIUMHALL);
                        break;
                    case 5:
                        presetReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
                        break;
                    case 6:
                        presetReverb.setPreset(PresetReverb.PRESET_PLATE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchReverb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    presetReverb.setEnabled(true);
                } else {
                    presetReverb.setEnabled(false);
                }
            }
        });

        layoutFX.addView(spinner);

    }
    */

    public void setLayoutEnvironmentalReverb(){
        final EnvironmentalReverb environmentalReverb = globals.getEnvironmentalReverb();
        environmentalReverb.setEnabled(true);
        globals.getMediaPlayer().attachAuxEffect(environmentalReverb.getId());
        globals.getMediaPlayer().setAuxEffectSendLevel(1.0f);
        switchEnvironmental = new Switch(getActivity());
        switchEnvironmental.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Reverberation", switchEnvironmental);
        layoutFX.addView(linearLayout);

        String reverbs[] = {
                "Decay time",
                "Reflection delay",
                "Reverb delay",
                "Decay ratio HF",
                "Density",
                "Difussion",
                "Reflection level",
                "Reverb level",
                "Room level HF",
                "Room level"};

        RelativeLayout.LayoutParams lpText = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lpText.setMargins(0, 20, 0, 20);

        for(int i=0; i<reverbs.length; i++){
            final int position = i;
            TextView textView = new TextView(getActivity());
            textView.setText(reverbs[i]);
            textView.setTextSize(15);
            textView.setTextColor(textColorBlack);
            textView.setAlpha(0.7f);
            textView.setTypeface(null,Typeface.BOLD);
            textView.setLayoutParams(lpText);
            layoutFX.addView(textView);

            SeekBar seekBar = new SeekBar(getActivity());
            seekBar.setMax(30000);
            seekBar.setProgress(15000);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setEnvironmentalLevel(environmentalReverb,position,progress-15000);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            layoutFX.addView(seekBar);
        }
    }

    public void setEnvironmentalLevel(EnvironmentalReverb e, int i, int pro) {
        short progress = (short) pro;
        switch (i) {
            case 0:
                e.setDecayTime(pro);
                break;
            case 1:
                e.setReflectionsDelay(pro);
                break;
            case 2:
                e.setReverbDelay(pro);
                break;
            case 3:
                e.setDecayHFRatio(progress);
                break;
            case 4:
                e.setDensity(progress);
                break;
            case 5:
                e.setDiffusion(progress);
                break;
            case 6:
                e.setReflectionsLevel(progress);
                break;
            case 7:
                e.setReverbLevel(progress);
                break;
            case 8:
                e.setRoomHFLevel(progress);
                break;
            case 9:
                e.setRoomLevel(progress);
                break;
        }
    }

    /*
    public void setLayoutLoudness() {
        switchLoudness= new Switch(getActivity());
        switchLoudness.setChecked(true);
        final LinearLayout linearLayout = setOptionHeaderLayout("Loudness", switchLoudness);
        layoutFX.addView(linearLayout);
        final SeekBar seekBar = new SeekBar(getActivity());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            final LoudnessEnhancer loudnessEnhancer = globals.getLoudnessEnhancer();
            loudnessEnhancer.setEnabled(true);

            seekBar.setMax(1000);
            seekBar.setProgress(0);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    loudnessEnhancer.setTargetGain(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            switchLoudness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        linearLayout.addView(seekBar);
                        loudnessEnhancer.setEnabled(true);
                    } else {
                        linearLayout.removeView(seekBar);
                        loudnessEnhancer.setEnabled(false);
                    }
                }
            });
            if (switchLoudness.isChecked()) {
                linearLayout.addView(seekBar);
            }
        }
    }
    */





}
