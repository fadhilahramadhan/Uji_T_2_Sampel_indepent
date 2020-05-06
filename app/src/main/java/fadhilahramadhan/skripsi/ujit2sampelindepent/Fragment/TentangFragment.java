package fadhilahramadhan.skripsi.ujit2sampelindepent.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fadhilahramadhan.skripsi.ujit2sampelindepent.R;


public class TentangFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tentang, container, false);
        getActivity().setTitle("About");

        return view;
    }
}
