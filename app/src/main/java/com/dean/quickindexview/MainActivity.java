package com.dean.quickindexview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dean.quickindexview.bean.Person;
import com.dean.quickindexview.data.Cheeses;
import com.dean.quickindexview.utils.PinYinUtil;
import com.dean.quickindexview.view.QuickIndexView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuickIndexView.OnLetterSelectedListener {

    private static final String TAG = "MainActivity";
    private List<Person> mData;
    private ListView mLvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final QuickIndexView qiView = (QuickIndexView) findViewById(R.id.qi_view);
        final TextView tvLetter = (TextView) findViewById(R.id.tv_letter);
        mLvMain = (ListView) findViewById(R.id.lv_main);
        //初始化联系人数据
        mData = getPersons();
        //展示数据
        mLvMain.setAdapter(new MyAdapter(this, R.layout.item_list, mData));
        mLvMain.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            //顶部字母的显示
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d(TAG, "firstVisibleItem:" + firstVisibleItem+" visibleItemCount:"+visibleItemCount);
                View groupView = getNextVisibleGroupView(firstVisibleItem, visibleItemCount);
                if (groupView != null) {
                    Log.d(TAG, "groupView.getY():" + groupView.getY());
                    Log.d(TAG, "groupView.getScrollY():" + groupView.getScrollY());
                    if (groupView.getY() < tvLetter.getHeight()) {
                        tvLetter.setY(groupView.getY()-tvLetter.getHeight());
                    } else {
                        tvLetter.setY(0);
                    }
                }else{
                    tvLetter.setY(0);
                }
                if (firstVisibleItem >= 0 && firstVisibleItem < mData.size()) {
                    Person person = mData.get(firstVisibleItem);
                    Log.d(TAG, "person:" + person + " firstVisibleItem:" + firstVisibleItem);
                    char ch = person.letter != null && person.letter.length() > 0 ? person.letter
                            .charAt(0) : 'A';
                    tvLetter.setText(ch + "");
                }
            }

            //获取可视区域的下一个可见的分组
            private View getNextVisibleGroupView(int firstVisibleItem, int visibleItemCount) {
                Log.d(TAG, "----------------------------------");
                for (int i = firstVisibleItem; i <firstVisibleItem+ visibleItemCount; i++) {
                    //在可视区域查找下一个分组，getChildAt()方法只能查找可视范围
                    View childView = mLvMain.getChildAt(i-firstVisibleItem);
                    ViewHolder viewHolder = (ViewHolder) childView.getTag();
                    Log.d(TAG, "i:" + i + " " + viewHolder.tvName
                            .getText() + " childView.getY():" + childView.getY());
                    //在可视区域查找，Y轴大于0并且可见意味着是一个可视分组
                    if (viewHolder.tvLetter.getVisibility() == View.VISIBLE && childView
                            .getY() > 0) {
                        Log.d(TAG, "下个分组是viewHolder.tvLetter.getText():" + viewHolder.tvLetter
                                .getText());
                        return childView;
                    }
                }
                return null;
            }
        });
        qiView.setOnLetterSelectedListener(this);
    }

    //初始化待显示的数据
    private List<Person> getPersons() {
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            String pinyin = PinYinUtil.getNameFirstLetter(Cheeses.NAMES[i]);
            Person person = new Person(Cheeses.NAMES[i], pinyin);
            list.add(person);
        }
        Collections.sort(list);
        return list;
    }

    //字母索引表被选中，滚动到相应姓氏的联系人
    @Override
    public void onLetterSelected(View view, char letter, int index) {
        int position = -1;
        for (int i = 0; i < mData.size(); i++) {
            Person person = mData.get(i);
            if (person.letter.charAt(0) == letter) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            mLvMain.setSelection(position);
        }
    }

    class MyAdapter extends ArrayAdapter<Person> {

        private final int mResId;

        public MyAdapter(Context context, int resource, List<Person> objects) {
            super(context, resource, objects);
            mResId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                                            .inflate(mResId, parent, false);
                viewHolder = ViewHolder.createInstance(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //字母分组处理
            Person curPerson = getItem(position);
            Person prevPerson = position == 0 ? null : getItem(--position);
            String curChar = curPerson != null && curPerson.letter != null && curPerson.letter
                    .length() > 0 ? curPerson.letter
                    .charAt(0) + "" : "";
            String prevChar = prevPerson != null && prevPerson.letter != null && prevPerson.letter
                    .length() > 0 ? prevPerson.letter
                    .charAt(0) + "" : "";
            //当前人物姓氏首字母和他前面(--postion得到前面人物，人物已经排好顺序)
            //那个人物姓氏首字母相同的情况下不再显示字母标签，否则显示字母标签
            viewHolder.tvLetter
                    .setVisibility(curChar.equals(prevChar) ? View.GONE : View.VISIBLE);
            viewHolder.tvLetter
                    .setText(curChar);
            viewHolder.tvName.setText(curPerson!=null?curPerson.name:"");
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvLetter;
        public TextView tvName;

        public static ViewHolder createInstance(View view) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_letter);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
            return viewHolder;
        }
    }
}
