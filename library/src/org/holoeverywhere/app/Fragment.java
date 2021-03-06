
package org.holoeverywhere.app;

import org.holoeverywhere.addon.IAddon;
import org.holoeverywhere.addon.IAddonAttacher;
import org.holoeverywhere.addon.IAddonBasicAttacher;
import org.holoeverywhere.addon.IAddonFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app._HoloFragment;
import android.view.View;

public class Fragment extends _HoloFragment {
    public static <T extends Fragment> T instantiate(Class<T> clazz) {
        return instantiate(clazz, null);
    }

    public static <T extends Fragment> T instantiate(Class<T> clazz, Bundle args) {
        try {
            T fragment = clazz.newInstance();
            if (args != null) {
                args.setClassLoader(clazz.getClassLoader());
                fragment.setArguments(args);
            }
            return fragment;
        } catch (Exception e) {
            throw new InstantiationException("Unable to instantiate fragment " + clazz
                    + ": make sure class name exists, is public, and has an"
                    + " empty constructor that is public", e);
        }
    }

    @Deprecated
    public static Fragment instantiate(Context context, String fname) {
        return instantiate(context, fname, null);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static Fragment instantiate(Context context, String fname, Bundle args) {
        try {
            return instantiate((Class<? extends Fragment>) Class.forName(fname, true,
                    context.getClassLoader()), args);
        } catch (Exception e) {
            throw new InstantiationException("Unable to instantiate fragment " + fname
                    + ": make sure class name exists, is public, and has an"
                    + " empty constructor that is public", e);
        }
    }

    private final IAddonAttacher<IAddonFragment> mAttacher =
            new IAddonBasicAttacher<IAddonFragment, Fragment>(this);

    @Override
    public <T extends IAddonFragment> T addon(Class<? extends IAddon> clazz) {
        return mAttacher.addon(clazz);
    }

    @Override
    public <T extends IAddonFragment> T addon(String classname) {
        return mAttacher.addon(classname);
    }

    @Override
    public boolean isAddonAttached(Class<? extends IAddon> clazz) {
        return mAttacher.isAddonAttached(clazz);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        forceInit(savedInstanceState);
        super.onCreate(savedInstanceState);
        performAddonAction(new AddonCallback<IAddonFragment>() {
            @Override
            public void justAction(IAddonFragment addon) {
                addon.onCreate(savedInstanceState);
            }
        });
    }

    @Override
    public void onViewCreated(final View view) {
        super.onViewCreated(view);
        performAddonAction(new AddonCallback<IAddonFragment>() {
            @Override
            public void justAction(IAddonFragment addon) {
                addon.onViewCreated(view);
            }
        });
    }

    @Override
    public boolean performAddonAction(AddonCallback<IAddonFragment> callback) {
        return mAttacher.performAddonAction(callback);
    }
}
