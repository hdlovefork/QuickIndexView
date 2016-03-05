#仿联系人快速索引
##在XML文件中使用
     <com.dean.quickindexview.view.QuickIndexView
            android:id="@+id/qi_view"
            android:layout_width="30dp"
            android:layout_height="match_parent"
            android:layout_alignParentRight="true"
            android:background="@android:color/black"/>
            
##在代码中使用
    QuickIndexView qiView=new QuickIndexView(this);
           
##监听字母的变化
    final QuickIndexView qiView = (QuickIndexView) findViewById(R.id.qi_view);
    qiView.setOnLetterSelectedListener(this);
    //字母索引表被选中
    @Override
    public void onLetterSelected(View view, char letter, int index) {
      Log.d(TAG,"被选中的字母是:"+letter);
    }