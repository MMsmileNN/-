# -城市 索引选择器
 --简介
    使用的是高德地图的amp api  由于很多需求中要用到高德地图的城市码去进行数据的分配， 而根据城市名称是无法从高德地图中获取城市码，所以使用高德提供的 
    城市码表制作了一个db 文件，方便城市选择定位回调使用，布局采用的是自定义的view索引来点击页面定位到字母选择的城市栏，有定位功能，有热门城市的设置和
    选择
    
##内容
    * 高德定位
    * 根据制作的城市索引db文件显示城市
    * 热门城市的选择，可服务器获取或默认设置

##具体功能实现
   **数据获取
   
      主要是通过 sqlite对raw/china_city_name的数据提取
      
            // // 打开raw中得数据库文件，获得stream流
          InputStream stream = this.mContext.getResources().openRawResource(R.raw.china_city_name);

          try
          {

            // 将获取到的stream 流写入道data中
            FileOutputStream outputStream = new FileOutputStream(dbFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = stream.read(buffer)) > 0)
            {
              outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            stream.close();
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            return db;
           
           
  **界面设计
  
      ***布局文件
      
       <ListView
          android:id="@+id/city_list"
          android:layout_width="fill_parent"
          android:layout_height="wrap_content"
          android:layout_marginRight="30dip"
          android:background="#E8E8E8"
          android:cacheColorHint="#00000000"
          android:scrollbars="none" />
        //自定义一个索引控件
      <com.example.zhou.myapplication.view.MyLetterListView
          android:id="@+id/cityLetterListView"
          android:layout_width="30dip"
          android:layout_height="fill_parent"
          android:layout_alignParentRight="true"
          android:background="#ffffff"
          android:layout_below="@+id/rl_city_dangqiancity"

          />
          
          
      ***自定控件MyLetterListeView中，通过以下代码进行 索引栏的绘制
          
          
        String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
           protected void onDraw(Canvas canvas)
          {
            super.onDraw(canvas);
            if (showBkg)
            {
              canvas.drawColor(Color.parseColor("#40000000"));//设置画板颜色
            }

            int height = getHeight();
            int width = getWidth();
            int singleHeight = height / b.length;
            for (int i = 0; i < b.length; i++)
            {
              paint.setColor(Color.rgb(0x27, 0x8a, 0xfc));
              paint.setTypeface(Typeface.DEFAULT_BOLD);
              paint.setAntiAlias(true);
              paint.setTextSize(ToolUnit.spTopx(10));

              if (i == choose)
              {
        //				paint.setColor(Color.parseColor("#3399ff"));
                paint.setColor(Color.rgb(0x33, 0x99, 0xff));//设置选择的字母颜色
                paint.setFakeBoldText(true);
              }
              float xPos = width / 2 - paint.measureText(b[i]) / 2;
              float yPos = singleHeight * i + singleHeight;
              canvas.drawText(b[i], xPos, yPos, paint);
              paint.reset();
            }
            
        ***  控件暴露一个接口给界面数据绑定事件的时候回调
        
            public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener)
            {
              this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
            }

            public interface OnTouchingLetterChangedListener
            {
              public void onTouchingLetterChanged(String s);
            }
        
  **数据展示
      
     *** 首先是定位使用的是高德地图的amap api
            mLocationClient = new AMapLocationClient(CityAddressActivity.this);
                    AMapLocationClientOption option = new AMapLocationClientOption();
                    option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    option.setOnceLocation(true);
                    mLocationClient.setLocationOption(option);
                    mLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
      
      *** 热门城市布局文件是 以一个头布局添加到城市数据的 listview上，通过 HotCityGridAdapter  进行数据的加载
          
             public View getView(int position, View view, ViewGroup parent) {
        HotCityViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_hot_city_gridview, parent, false);
            holder = new HotCityViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_hot_city_name);
            view.setTag(holder);
        }else{
            holder = (HotCityViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position));
        return view;
          
      ***城市点击事件的绑定及回调
      
         private class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener
    {

        @Override
        public void onTouchingLetterChanged(final String s)
        {
            if (alphaIndexer.get(s) != null)
            {
                int position = alphaIndexer.get(s);
                mCityLit.setSelection(position);
                overlay.setText(sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1500);
            }
            
     
