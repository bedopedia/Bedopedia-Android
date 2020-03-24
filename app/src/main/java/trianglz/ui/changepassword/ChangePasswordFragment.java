package trianglz.ui.changepassword;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.skolera.skolera_android.R;
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
        initViewModelObservers();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.showHideNewPassword.setOnClickListener(v -> onShowHideNewPassword());
        binding.showHideOldPassword.setOnClickListener(v -> onShowHideOldPassword());
    }

    private void onShowHideNewPassword() {
        if (binding.etNewPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
            binding.etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showHideNewPassword.setImageResource(R.drawable.ic_unshow_password);
        } else {
            binding.etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.showHideNewPassword.setImageResource(R.drawable.ic_show_password);
        }
    }
    private void onShowHideOldPassword () {
        if (binding.etOldPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
            binding.etOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.showHideOldPassword.setImageResource(R.drawable.ic_unshow_password);
        } else {
            binding.etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.showHideOldPassword.setImageResource(R.drawable.ic_show_password);
        }
    }

    private void initViewModelObservers() {
        viewModel.oldPassword.observe(getViewLifecycleOwner(), s -> {

        });
        viewModel.newPassword.observe(getViewLifecycleOwner(), s -> {

        });
    }
}
