package com.example.baseproject;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends BaseActivity {
    public static final int DEFAULT_ID = 1;
    public static final String ARG_ID = "SCHEDULE_ID_ARG";
    public static final String ARG_NAME = "SCHEDULE_NAME_ARG";
    public static final String ARG_TYPE = "SCHEDULE_TYPE_ARG";
    public static final String ARG_MODE = "SCHEDULE_MODE_ARG";
    ScheduleType type;
    ScheduleMode mode;
    int id;
    String name;
    RecyclerView recyclerView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        name = getIntent().getStringExtra(ARG_NAME);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);

        TextView title = findViewById(R.id.titleText);
        title.setText(name);

        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);
        recyclerView.setAdapter(adapter);

        initTime();
        initData();
    }

    private void initData() {
        List<ScheduleItem> list = new ArrayList<>();

        ScheduleItemHeader header = new ScheduleItemHeader();
        header.setTitle("ПН, 28 ЯНВ");
        list.add(header);

        ScheduleItem item = new ScheduleItem();
        item.setStart("10:00");
        item.setEnd("11:20");
        item.setType("Практическое занятие");
        item.setName("Анализ данных");
        item.setPlace("Ауд. 503, корп. 1");
        item.setTeacher("Гущим Михаил Иванович");
        list.add(item);

        item = new ScheduleItem();
        item.setStart("11:50");
        item.setEnd("13:10");
        item.setType("Лекция");
        item.setName("Анализ данных");
        item.setPlace("Ауд. 503, корп. 1");
        item.setTeacher("Гущим Михаил Иванович");
        list.add(item);
        adapter.setDataList(list);
    }

    private void onScheduleItemClick(ScheduleItem scheduleItem) {

    }

    public final static class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_ITEM = 0;
        private final static int TYPE_HEADER = 1;

        private List<ScheduleItem> dataList = new ArrayList<>();
        private OnItemClick onItemClick;

        public ItemAdapter(OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        public void setDataList(List<ScheduleItem> list) {
            dataList = list;
        }

        public List<ScheduleItem> getDataList() {
            return dataList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            if (viewType == TYPE_ITEM) {
                View contactView = inflater.inflate(R.layout.item_schedule, parent, false);
                return new ViewHolder(contactView, context ,onItemClick);
            } else if (viewType == TYPE_HEADER) {
                View contactView = inflater.inflate(R.layout.item_schedule_header, parent, false);
                return new ViewHolderHeader(contactView, context, onItemClick);
            }

            throw new IllegalArgumentException("Invalid view type");
        }

        public int getItemViewType(int position) {
            ScheduleItem data = dataList.get(position);
            if (data instanceof ScheduleItemHeader) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ScheduleItem data = dataList.get(position);
            if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).bind(data);
            } else if (holder instanceof ViewHolderHeader) {
                ((ViewHolderHeader) holder).bind((ScheduleItemHeader) data);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private OnItemClick onItemClick;
        private TextView start;
        private TextView end;
        private TextView type;
        private TextView name;
        private TextView place;
        private TextView teacher;

        public ViewHolder(@NonNull View itemView, Context context, OnItemClick onItemClick) {
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            start = itemView.findViewById(R.id.textStart);
            end = itemView.findViewById(R.id.textEnd);
            type = itemView.findViewById(R.id.textType);
            name = itemView.findViewById(R.id.textName);
            place = itemView.findViewById(R.id.textPlace);
            teacher = itemView.findViewById(R.id.textTeacher);
        }

        public void bind(final ScheduleItem data) {
            start.setText(data.getStart());
            end.setText(data.getEnd());
            type.setText(data.getType());
            name.setText(data.getName());
            place.setText(data.getPlace());
            teacher.setText(data.getTeacher());
        }
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        private Context context;
        private OnItemClick onItemClick;
        private TextView title;

        public ViewHolderHeader(@NonNull View itemView, Context context, OnItemClick onItemClick) {
            super(itemView);
            this.context = context;
            this.onItemClick = onItemClick;
            title = itemView.findViewById(R.id.titleHeader);
        }

        public void bind(final ScheduleItemHeader data) {
            title.setText(data.getTitle());
        }
    }
}