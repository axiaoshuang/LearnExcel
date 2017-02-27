package com.win.myexcle.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.win.myexcle.R;
import com.win.myexcle.adapters.ExcelAdapter;
import com.win.myexcle.bean.Cell;
import com.win.myexcle.bean.ColTitle;
import com.win.myexcle.bean.RowTitle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.zhouchaoyuan.excelpanel.ExcelPanel;

public class MainActivity extends AppCompatActivity implements ExcelPanel.OnLoadMoreListener {


    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.content_container)
    ExcelPanel excelPanel;
    private Unbinder unbinder;

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String WEEK_FORMAT_PATTERN = "EEEE";
    public static final String[] CHANNEL = {"高数", "物理", "历史", "计算机网络", "组装原理"};
    public static final String[] NAME = {"张胜男", "李华", "王山", "雅君", "虎子", "飞机", "刘文"};
    public static final long ONE_DAY = 24 * 3600 * 1000;
    public static final int PAGE_SIZE = 14;
    public static final int ROW_SIZE = 20;

    private ExcelAdapter adapter;
    private List<RowTitle> rowTitles;
    private List<ColTitle> colTitles;
    private List<List<Cell>> cells;
    private SimpleDateFormat dateFormatPattern;
    private SimpleDateFormat weekFormatPattern;
    private boolean isLoading;
    private long historyStartTime;
    private long moreStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder=ButterKnife.bind(this);
        adapter = new ExcelAdapter(this, blockListener);
        excelPanel.setAdapter(adapter);
        excelPanel.setOnLoadMoreListener(this);
        initData();
    }

    private View.OnClickListener blockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Cell cell = (Cell) view.getTag();
            if (cell != null) {
                if (cell.getStatus() == 0) {
                    Toast.makeText(MainActivity.this, "空房", Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 1) {
                    Toast.makeText(MainActivity.this, "已离店，离店人：" + cell.getBookName(), Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 2) {
                    Toast.makeText(MainActivity.this, "入住中，离店人：" + cell.getBookName(), Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 3) {
                    Toast.makeText(MainActivity.this, "预定中，离店人：" + cell.getBookChannel(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onLoadMore() {
        if (!isLoading) {
            loadData(moreStartTime, false);
        }
    }

    @Override
    public void onLoadHistory() {
        if (!isLoading) {
            loadData(historyStartTime, true);
        }
    }

    private void initData() {
        moreStartTime = Calendar.getInstance().getTimeInMillis();
        historyStartTime = moreStartTime - ONE_DAY * PAGE_SIZE;
        dateFormatPattern = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
        rowTitles = new ArrayList<>();
        colTitles = new ArrayList<>();
        cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            cells.add(new ArrayList<Cell>());
        }
        loadData(moreStartTime, false);
    }

    private void loadData(long startTime, final boolean history) {
        isLoading = true;
        Message message = new Message();
        message.arg1 = history ? 1 : 2;
        message.obj = new Long(startTime);
        loadDataHandler.sendMessageDelayed(message, 1000);
    }

    private Handler loadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isLoading = false;
            long startTime = (Long) msg.obj;
            List<RowTitle> rowTitles1 = genRowData(startTime);
            List<List<Cell>> cells1 = genCellData();
            if (msg.arg1 == 1) {//history
                historyStartTime -= ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(0, rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(0, cells1.get(i));
                }

                if (excelPanel != null) {
                    excelPanel.addHistorySize(PAGE_SIZE);
                }
            } else {
                moreStartTime += ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(cells1.get(i));
                }
            }
            if (colTitles.size() == 0) {
                colTitles.addAll(genColData());
            }
            progress.setVisibility(View.GONE);
            adapter.setAllData(colTitles, rowTitles, cells);
            adapter.enableFooter();
            adapter.enableHeader();
        }
    };

    private List<RowTitle> genRowData(long startTime) {
        List<RowTitle> rowTitles = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < PAGE_SIZE; i++) {
            RowTitle rowTitle = new RowTitle();
            rowTitle.setAvailableRoomCount(random.nextInt(10) + 10);
            rowTitle.setDateString(dateFormatPattern.format(startTime + i * ONE_DAY));
            rowTitle.setWeekString(weekFormatPattern.format(startTime + i * ONE_DAY));
            rowTitles.add(rowTitle);
        }
        return rowTitles;
    }

    private List<ColTitle> genColData() {
        List<ColTitle> colTitles = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            ColTitle colTitle = new ColTitle();
            if (i < 10) {
                colTitle.setRoomNumber("10" + i);
            } else {
                colTitle.setRoomNumber("20" + (i - 10));
            }
            if (i % 3 == 0) {
                colTitle.setRoomTypeName("A" + i);
            } else if (i % 3 == 1) {
                colTitle.setRoomTypeName("B" + i);
            } else {
                colTitle.setRoomTypeName("C" + i);
            }
            colTitles.add(colTitle);
        }
        return colTitles;
    }

    private List<List<Cell>> genCellData() {
        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            cells.add(cellList);
            for (int j = 0; j < PAGE_SIZE; j++) {
                Cell cell = new Cell();
                Random random = new Random();
                int number = random.nextInt(6);
                if (number == 1 || number == 2 || number == 3) {
                    cell.setStatus(number);
                    cell.setBookChannel(CHANNEL[random.nextInt(CHANNEL.length)]);
                    cell.setBookName(NAME[random.nextInt(NAME.length)]);
                } else {
                    cell.setStatus(0);
                }
                cellList.add(cell);
            }
        }
        return cells;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
