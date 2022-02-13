package com.example.nkindustries.ui.mpin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nkindustries.databinding.FragmentMobilePinBinding;

public class MobilePinFragment extends Fragment {


    private MobilePinViewModel mobilePinViewModel;
    private FragmentMobilePinBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mobilePinViewModel =
                new ViewModelProvider(this).get(MobilePinViewModel.class);

        binding = FragmentMobilePinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textMobilePassword;
//        mobilePinViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
