package Tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by ali on 26/04/17.
 */

public class FragmentUtils {

    public static void createFragment(FragmentManager fragmentManager,Fragment gradesFragment, int id ) {
        FragmentManager fm = fragmentManager;
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f = gradesFragment;
        ft.add(id, f);
        ft.commit();
    }
}
