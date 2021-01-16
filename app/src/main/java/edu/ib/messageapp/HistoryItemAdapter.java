package edu.ib.messageapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    private List<HistoryItem> historyItemList;

    public HistoryItemAdapter(List<HistoryItem> historyItemList) {
        this.historyItemList = historyItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View contactView = layoutInflater.inflate(R.layout.recycle, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(HistoryItemAdapter.ViewHolder holder, int position)  {
        HistoryItem historyItem = historyItemList.get(position);

        TextView textView = holder.messageText;
        textView.setText(historyItem.toString());
        ImageButton imageButton = holder.deleteButton;
        imageButton.setOnClickListener((View.OnClickListener) view -> {
            Context context=view.getContext();
            HistoryItem item=historyItemList.get(position);
            String filename = item.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".txt";
            File file = new File(context.getExternalFilesDir(MainActivity.MESSAGES_FOLDER),filename);
            boolean success=file.delete();
            if(success){
                Toast.makeText(context,"File deleted",Toast.LENGTH_LONG).show();
                historyItemList.remove(position);
                notifyItemRemoved(position);
            }else{
                Toast.makeText(context,"Failed to delete a file",Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageText = (TextView) itemView.findViewById(R.id.txtListPosition);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.ibtnDelete);
        }

    }

}
