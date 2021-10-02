package com.example.stockrecheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockrecheck.CheckVersion;
import com.example.stockrecheck.Login;
import com.example.stockrecheck.R;
import com.example.stockrecheck.dao.PlanItems;
import com.example.stockrecheck.dao.Results;

import java.util.List;

import static java.util.Objects.isNull;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyviewHolder>  {

    Context context;
    List<PlanItems> planList;

    public RecyclerAdapter(Context context, List<PlanItems> movieList) {
        this.context = context;
        this.planList = movieList;
    }

    public void setMovieList(List<PlanItems> planList) {
        this.planList = planList;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter,parent,false);


        return new MyviewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerAdapter.MyviewHolder holder, int position) {

        holder.matcode.setText(planList.get(position).getMatcode());
        holder.matdesc.setText(planList.get(position).getMatdesc());
        holder.lgort.setText(planList.get(position).getLocation());
        holder.batch.setText(planList.get(position).getBatch());
        holder.sap_qty.setText(planList.get(position).getSapQty()+"/"+iNull(planList.get(position).getActQty()));
        if(holder.getAdapterPosition()%2==1){
            holder.vertLay.setBackgroundColor(Color.parseColor("#ECECEC"));
        }


//        Glide.with(context).load(movieList.get(position).getImageUrl()).apply(RequestOptions.centerCropTransform()).into(holder.image);
    }

    public String iNull(Integer i) {
        return (i==null ? "0" : i.toString());
    }

    @Override
    public int getItemCount() {
        if(planList != null){
            return planList.size();
        }
        return 0;

    }



    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView matcode,lgort,matdesc,batch,sap_qty;
        LinearLayout vertLay;
        ProgressBar mProgressBar;


        public MyviewHolder(View itemView) {
            super(itemView);
            matcode = (TextView)itemView.findViewById(R.id.matcode);
            lgort = (TextView)itemView.findViewById(R.id.lgort);
            matdesc = (TextView)itemView.findViewById(R.id.matdesc);
            batch = (TextView)itemView.findViewById(R.id.batch);
            sap_qty = (TextView)itemView.findViewById(R.id.sap_qty);
            vertLay =(LinearLayout)itemView.findViewById(R.id.vertLay);



//            itemView.setOnClickListener(this);
          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("XXR", "Element " + planList.get(getAdapterPosition()).getPlan_doc().toString() + " clicked.");
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            Log.d("XXR", "Element " + planList.get(getAdapterPosition()).getPlanDoc().toString() + " clicked.");
            /*Intent i = new Intent(context, Login.class);
            context.startActivity(i);*/

        }
    }
}