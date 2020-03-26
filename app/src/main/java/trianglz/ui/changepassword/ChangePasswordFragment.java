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

import java.util.Objects;


public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel viewModel;
    private ChangePasswordFragmentBinding binding;
    private ChangePasswordActivity changePasswordActivity;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changePasswordActivity = ((ChangePasswordActivity) Objects.requireNonNull(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        binding = ChangePasswordFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        initViewModelObservers();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.showHideNewPassword.setOnClickListener(v -> onShowHideNewPassword());
        binding.showHideOldPassword.setOnClickListener(v -> onShowHideOldPassword());
        binding.btnChangePassword.setOnClickListener(view -> validate());
    }

    private void validate() {
        boolean isValid = true;
        // validate old password
        String oldPass = viewModel.oldPassword.getValue();
        if (oldPass == null || oldPass.isEmpty()) {
            binding.oldPasswordErrorTv.setVisibility(View.VISIBLE);
            binding.oldPasswordErrorTv.setText(getString(R.string.password_is_empty));
            isValid = false;
            binding.oldProgressBar.setProgressColor(getResources().getColor(R.color.pale_red));
            binding.oldProgressBar.setProgress(100);
        } else if (oldPass.length() < 7) {
            binding.oldPasswordErrorTv.setVisibility(View.VISIBLE);
            binding.oldPasswordErrorTv.setText(getString(R.string.password_length_error));
            binding.oldProgressBar.setProgressColor(getResources().getColor(R.color.pale_red));
            binding.oldProgressBar.setProgress(100);
            isValid = false;
        }
        // validate new password
        String newPass = viewModel.newPassword.getValue();
        if (newPass == null || newPass.isEmpty()) {
            binding.newPasswordErrorTv.setVisibility(View.VISIBLE);
            binding.newPasswordErrorTv.setVisibility(View.VISIBLE);
            binding.newPasswordErrorTv.setText(getString(R.string.password_is_empty));
            binding.newProgressBar.setProgressColor(getResources().getColor(R.color.pale_red));
            binding.newProgressBar.setProgress(100);
            isValid = false;
        } else if (newPass.length() < 7) {
            binding.newPasswordErrorTv.setVisibility(View.VISIBLE);
            binding.newPasswordErrorTv.setText(getString(R.string.password_length_error));
            binding.newProgressBar.setProgressColor(getResources().getColor(R.color.pale_red));
            binding.newProgressBar.setProgress(100);
            isValid = false;
        }

        if (isValid) {
            changePasswordActivity.progress.show();
            viewModel.changePassword(viewModel.oldPassword.getValue(),viewModel.newPassword.getValue());
        }
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
            binding.oldPasswordErrorTv.setVisibility(View.INVISIBLE);
            binding.oldProgressBar.setProgressColor(getResources().getColor(R.color.jade_green));
            if (s.trim().length() > 6) {
                binding.oldProgressBar.setProgress(100);
            } else {
                binding.oldProgressBar.setProgress((10 * (float) s.length()) / 6);
            }
        });
        viewModel.newPassword.observe(getViewLifecycleOwner(), s -> {
            binding.newPasswordErrorTv.setVisibility(View.INVISIBLE);
            binding.newProgressBar.setProgressColor(getResources().getColor(R.color.jade_green));
            if (s.trim().length() > 6) {
                binding.newProgressBar.setProgress(100);
            } else {
                binding.newProgressBar.setProgress((10 * (float) s.length()) / 6);
            }
        });

        viewModel.hideDialogEvent.observe(getViewLifecycleOwner(), hideLoading -> {
            if (hideLoading) {
                changePasswordActivity.progress.hide();
                viewModel.hideDialogHandled();
            }
        });
        viewModel.finishEvent.observe(getViewLifecycleOwner(), finish -> {
            if (finish) {
                changePasswordActivity.finish();
                viewModel.finishEventHandled();
            }
        });
        viewModel.showErrorDialog.observe(getViewLifecycleOwner(), stringIntegerPair -> {
            if (stringIntegerPair != null) {
                changePasswordActivity.showErrorDialog(getContext(), stringIntegerPair.second, stringIntegerPair.first);
                viewModel.showErrorDialogHandled();
            }
        });
    }
}
