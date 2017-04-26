package Tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by ali on 26/04/17.
 */

public class FragmentUtils {

    public static void createFragment(FragmentManager fragmentManager,Fragment gradesFragment, int id ) {
        FragmentManager fm = fragmentManager;
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        Fragment f = gradesFragment;
        ft.add(id, f);
        ft.commit();
    }
}
