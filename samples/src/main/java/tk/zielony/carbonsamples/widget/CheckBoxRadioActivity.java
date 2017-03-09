package tk.zielony.carbonsamples.widget;

import tk.zielony.carbonsamples.SamplesActivity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;

import carbon.drawable.CheckableDrawable;
import carbon.widget.CheckBox;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.databinding.ActivityCheckboxRadioBinding;

/**
 * Created by Marcin on 2015-03-06.
 */
public class CheckBoxRadioActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_checkbox_radio, null, false);
        setContentView(viewDataBinding.getRoot());
        ActivityCheckboxRadioBinding binding = (ActivityCheckboxRadioBinding) viewDataBinding;

        Samples.initToolbar(this, getString(R.string.checkBoxRadioActivity_title));

        binding.check.setOnClickListener(view -> binding.checkBox.setChecked(true));

        binding.uncheck.setOnClickListener(view -> binding.checkBox.setChecked(false));

        binding.checkBoxGroup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.checkBoxChild1.setChecked(isChecked);
            binding.checkBoxChild2.setChecked(isChecked);
        });

        CheckBox.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (binding.checkBoxChild1.isChecked() != binding.checkBoxChild2.isChecked()) {
                binding.checkBoxGroup.setChecked(CheckableDrawable.CheckedState.INDETERMINATE);
            } else {
                binding.checkBoxGroup.setChecked(binding.checkBoxChild1.isChecked());
            }
        };
        binding.checkBoxChild1.setOnCheckedChangeListener(listener);
        binding.checkBoxChild2.setOnCheckedChangeListener(listener);
    }
}
