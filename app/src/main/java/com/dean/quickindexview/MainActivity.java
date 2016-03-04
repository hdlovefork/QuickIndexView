package com.dean.quickindexview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private MyAdapter mMyAdapter;
    private ListView mLvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QuickIndexView mQiView = (QuickIndexView) findViewById(R.id.qi_view);
        mLvMain = (ListView) findViewById(R.id.lv_main);
        mQiView.setOnLetterSelectedListener(this);
        //初始化联系人数据
        mData = getPersons();
        //展示数据
        mMyAdapter = new MyAdapter(this, R.layout.item_list, mData);
        mLvMain.setAdapter(mMyAdapter);
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
                    .charAt(0) + "" : null;
            String prevChar = prevPerson != null && prevPerson.letter != null && prevPerson.letter
                    .length() > 0 ? prevPerson.letter
                    .charAt(0) + "" : null;
            //当前人物姓氏首字母和他前面(--postion得到前面人物，人物已经排好顺序)
            //那个人物姓氏首字母相同的情况下不再显示字母标签，否则显示字母标签
            viewHolder.tvLetter
                    .setVisibility(curChar.equals(prevChar) ? View.GONE : View.VISIBLE);
            viewHolder.tvLetter
                    .setText(curChar);
            viewHolder.tvName.setText(curPerson.name);
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
