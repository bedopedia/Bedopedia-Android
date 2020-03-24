package trianglz.ui.changepassword;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChangePasswordViewModel extends ViewModel {
    public MutableLiveData<String> oldPassword, newPassword;
}
