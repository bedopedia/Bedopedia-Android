package Tools;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

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
