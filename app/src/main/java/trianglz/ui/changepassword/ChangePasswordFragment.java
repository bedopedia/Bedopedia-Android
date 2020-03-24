package trianglz.ui.changepassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.skolera.skolera_android.databinding.ChangePasswordFragmentBinding;


public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel viewModel;
    private ChangePasswordFragmentBinding binding;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        binding = ChangePasswordFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
