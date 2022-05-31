package com.kitcod.android.fragments;

import androidx.fragment.app.Fragment;

abstract class BaseFragment extends PermissionFragment {
    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

}
