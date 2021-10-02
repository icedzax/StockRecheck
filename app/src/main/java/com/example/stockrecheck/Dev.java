package com.example.stockrecheck;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Dev extends AppCompatActivity {
    private ListView lvCountry;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lvCountry = (ListView) findViewById(R.id.lvCountry);

        ArrayList<Item> countryList = new ArrayList<Dev.Item>();
        // Header
        countryList.add(new SectionItem("Asia"));
        // State Name
        countryList.add(new EntryItem("India"));
        countryList.add(new EntryItem("China"));
        countryList.add(new EntryItem("Hong Kong"));
        countryList.add(new EntryItem("Nepal"));

        // Header
        countryList.add(new SectionItem("North Asia"));
        // State Name
        countryList.add(new EntryItem("Belarus"));
        countryList.add(new EntryItem("Moldova"));
        countryList.add(new EntryItem("Russian Federation"));
        countryList.add(new EntryItem("Ukraine"));

        // Header
        countryList.add(new SectionItem("North America"));
        // State Name
        countryList.add(new EntryItem("Canada"));
        countryList.add(new EntryItem("Saint Pierre and Miquelon"));
        countryList.add(new EntryItem("United States"));

        // Header
        countryList.add(new SectionItem("North & Central America"));
        // State Name
        countryList.add(new EntryItem("Caribbean Islands"));
        countryList.add(new EntryItem("Anguilla"));
        countryList.add(new EntryItem("Antigua and Barbuda"));
        countryList.add(new EntryItem("Aruba"));

        // set adapter
        final CountryAdapter adapter = new CountryAdapter(this, countryList);
        lvCountry.setAdapter(adapter);
        lvCountry.setTextFilterEnabled(true);

        // filter on text change
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter != null)
                {
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * row item
     */
    public interface Item {
        public boolean isSection();
        public String getTitle();
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item {
        private final String title;

        public SectionItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }
    }

    /**
     * Entry Item
     */
    public class EntryItem implements Item {
        public final String title;

        public EntryItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }

    /**
     * Adapter
     */
    public class CountryAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Item> item;
        private ArrayList<Item> originalItem;

        public CountryAdapter() {
            super();
        }

        public CountryAdapter(Context context, ArrayList<Item> item) {
            this.context = context;
            this.item = item;
            //this.originalItem = item;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()) {
                // if section header
                convertView = inflater.inflate(R.layout.layout_section, parent, false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
                tvSectionTitle.setText(((SectionItem) item.get(position)).getTitle());
            }
            else
            {
                // if item
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
                tvItemTitle.setText(((EntryItem) item.get(position)).getTitle());
            }

            return convertView;
        }

        /**
         * Filter
         */
        public Filter getFilter()
        {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    item = (ArrayList<Item>) results.values;
                    notifyDataSetChanged();
                }

                @SuppressWarnings("null")
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    ArrayList<Item> filteredArrayList = new ArrayList<Item>();


                    if(originalItem == null || originalItem.size() == 0)
                    {
                        originalItem = new ArrayList<Item>(item);
                    }

                    /*
                     * if constraint is null then return original value
                     * else return filtered value
                     */
                    if(constraint == null && constraint.length() == 0)
                    {
                        results.count = originalItem.size();
                        results.values = originalItem;
                    }
                    else
                    {
                        constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
                        for (int i = 0; i < originalItem.size(); i++)
                        {
                            String title = originalItem.get(i).getTitle().toLowerCase(Locale.ENGLISH);
                            if(title.startsWith(constraint.toString()))
                            {
                                filteredArrayList.add(originalItem.get(i));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }
            };

            return filter;
        }
    }


}
