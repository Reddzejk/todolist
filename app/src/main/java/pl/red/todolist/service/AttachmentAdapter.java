package pl.red.todolist.service;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.red.todolist.R;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {
    private final List<Uri> attachments;
    private boolean onlyReadable;

    public AttachmentAdapter(List<Uri> attachments, boolean show) {
        this.attachments = attachments;
        this.onlyReadable = show;
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.attachment, parent, false);
        return new AttachmentViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(AttachmentViewHolder attachmentViewHolder, int position) {
        attachmentViewHolder.image.setImageURI(attachments.get(position));
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    class AttachmentViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageButton delete;

        AttachmentViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete_attachment);
            handleDeleteStrategy(itemView);
        }

        private void handleDeleteStrategy(View itemView) {
            if (onlyReadable) {
                delete.setVisibility(View.INVISIBLE);
            } else {
                itemView.findViewById(R.id.delete_attachment).setOnClickListener(
                        v -> {
                            if (getAdapterPosition() > -1) {
                                attachments.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), attachments.size());
                            }
                        });
            }
        }
    }
}