package com.forcavenda.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.forcavenda.Entidades.Produto;
import com.forcavenda.R;

import java.util.ArrayList;

/**
 * Created by Leo on 11/02/2017.
 */

public class TextViewProdutoAdapter extends ArrayAdapter<Produto> {
    private ArrayList<Produto> items;
    private ArrayList<Produto> itemsAll;
    private ArrayList<Produto> suggestions;
    private int viewResourceId;

    public ArrayList<Produto> getItems() {
        return items;
    }

    public TextViewProdutoAdapter(Context context, int viewResourceId, ArrayList<Produto> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<Produto>) items.clone();
        this.suggestions = new ArrayList<Produto>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Produto customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.label);
            if (customerNameLabel != null) {
                customerNameLabel.setText(customer.getNome());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Produto) (resultValue)).getNome();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                if (itemsAll.size() == 0) {
                    itemsAll = (ArrayList<Produto>) items.clone();
                }
                for (Produto customer : itemsAll) {
                    if (customer.getNome().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Produto> filteredList = (ArrayList<Produto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Produto c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
