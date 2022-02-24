package com.example.ordeepbot.symbol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ordeepbot.MainActivity;
import com.example.ordeepbot.R;
import com.example.ordeepbot.databinding.FragmentSymbolDetailsBinding;

/**
 * A fragment representing a list of Items.
 */
public class SymbolDetailsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private FragmentSymbolDetailsBinding binding;
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SymbolDetailsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SymbolDetailsFragment newInstance(int columnCount) {
        SymbolDetailsFragment fragment = new SymbolDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symbol_details, container, false);

        binding = FragmentSymbolDetailsBinding.inflate(inflater, container, false);
        SymbolInfoWithWs clickedSymbol = ((MainActivity) requireContext()).clickedSymbol;
        ((TextView) view.findViewById(R.id.price)).setText(String.valueOf(clickedSymbol.getWebSocketData().getOpen()));
        ((TextView) view.findViewById(R.id.symbol)).setText(clickedSymbol.getSymbolName());
        ((TextView) view.findViewById(R.id.difference24h)).setText(String.valueOf(clickedSymbol.getWebSocketData().getPercentDifference()));
        ImageView symbolIcon = view.findViewById(R.id.symbolIconPNG);
        ImageView assetIcon = view.findViewById(R.id.assetIconPNG);
        binding.symbol.setText(clickedSymbol.getSymbolName());
        binding.difference24h.setText(String.valueOf(clickedSymbol.getWebSocketData().getPercentDifference()));
        binding.price.setText(String.valueOf(clickedSymbol.getWebSocketData().getOpen()));
        Glide.with(symbolIcon.getContext()).load(clickedSymbol.getSymbolIconId()).into(symbolIcon);
        Glide.with(assetIcon.getContext()).load(clickedSymbol.getAssetIconId()).into(assetIcon);
        System.out.println(clickedSymbol.getSymbolName());


        return view;
    }
}