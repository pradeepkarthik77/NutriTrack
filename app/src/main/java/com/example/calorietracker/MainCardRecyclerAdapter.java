package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class MainCardRecyclerAdapter extends RecyclerView.Adapter<MainCardRecyclerAdapter.ViewHolder>
{
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String[] cardview_titles;
    private int cardview_count;
    private Context context;
    public ExcelClass excelClass;
    public LoadTheDatabase loadTheDatabase;
    //private MainActivity mainActivity;
    //private MainCardRecyclerAdapter mainCardRecyclerAdapter;


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView card_title;
        public RecyclerView inner_card_recycler;
        public MaterialButton see_more;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.card_title = (TextView) itemView.findViewById(R.id.main_card_title);
            this.inner_card_recycler = (RecyclerView) itemView.findViewById(R.id.main_content_recycler);
            this.see_more = (MaterialButton) itemView.findViewById(R.id.see_all_btn);
        }
    }

    public MainCardRecyclerAdapter(String[] cardview_titles,int cardview_count,Context context,LoadTheDatabase loadTheDatabase,ExcelClass excelClass,ActivityResultLauncher<Intent> activityResultLauncher)//,MainActivity mainActivity)
    {
        this.cardview_titles = cardview_titles;
        this.cardview_count = cardview_count;
        this.context = context;
        this.loadTheDatabase = loadTheDatabase;
        this.excelClass = excelClass;
        this.activityResultLauncher = activityResultLauncher;
        //this.mainActivity = mainActivity;
        //this.mainCardRecyclerAdapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.mainactivty_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.card_title.setText(this.cardview_titles[position]);

        MainInnerRecycler mainInnerRecycler = new MainInnerRecycler(this.cardview_titles[position],this.context,this.loadTheDatabase,this.excelClass);

        mainInnerRecycler.setAdapter(holder.inner_card_recycler);


        holder.see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent newIntent = new Intent(context,ListActivity.class);
                newIntent.putExtra("cardview_title",holder.card_title.getText().toString());
//                //mainActivity.set_card_title(holder.card_title.getText().toString(),mainCardRecyclerAdapter);
//               MainActivity activity = (MainActivity) context;
//                context.startActivity(newIntent);
                activityResultLauncher.launch(newIntent);
            }
        });
    }

    @Override//
    public int getItemCount() {
        return this.cardview_count;
    }
}
