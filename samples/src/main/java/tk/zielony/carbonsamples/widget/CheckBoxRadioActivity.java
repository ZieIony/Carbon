package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import carbon.drawable.CheckedState;
import carbon.widget.CheckBox;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.carbonsamples.databinding.ActivityCheckboxRadioBinding;

@ActivityAnnotation(
        title = R.string.checkBoxRadioActivity_title,
        icon = R.drawable.carbon_checkbox_checked
)
public class CheckBoxRadioActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_checkbox_radio, null, false);
        setContentView(viewDataBinding.getRoot());
        ActivityCheckboxRadioBinding binding = (ActivityCheckboxRadioBinding) viewDataBinding;

        initToolbar();

        binding.check.setOnClickListener(view -> binding.checkBox.setChecked(true));

        binding.uncheck.setOnClickListener(view -> binding.checkBox.setChecked(false));

        binding.checkBoxGroup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.checkBoxChild1.setChecked(isChecked);
            binding.checkBoxChild2.setChecked(isChecked);
        });

        CheckBox.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (binding.checkBoxChild1.isChecked() != binding.checkBoxChild2.isChecked()) {
                binding.checkBoxGroup.setChecked(CheckedState.INDETERMINATE);
            } else {
                binding.checkBoxGroup.setChecked(binding.checkBoxChild1.isChecked());
            }
        };
        binding.checkBoxChild1.setOnCheckedChangeListener(listener);
        binding.checkBoxChild2.setOnCheckedChangeListener(listener);
    }
}
