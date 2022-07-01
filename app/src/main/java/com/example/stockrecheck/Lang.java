package com.example.stockrecheck;

import java.util.HashMap;
import java.util.Map;

public class Lang {
    public static  Map<String, String> map = new HashMap<String, String>();
   public void setMap(int param ){

      if(param==0){
          map.clear();
          map.put("lang", "ไทย");
          map.put("empcode", "รหัสพนักงาน");
          map.put("stock", "นับสต็อก");
          map.put("machine","โรง");
          map.put("channal","ช่อง");
          map.put("move", "ย้ายเสา");
          map.put("pole","เสา");
          map.put("cancel","ยกเลิก");
          map.put("confirm","ยืนยัน");
          map.put("save_location","ตำแหน่งเก็บ");
          map.put("edit","แก้ไข");
          map.put("total","รวม");
          map.put("bundle","มัด");
          map.put("line","เส้น");
          map.put("no_bundle","เลขมัด");
          map.put("order","รายการ");
          map.put("amount","จำนวน");
          map.put("select_area","เลือกฝั่งที่จะลง");
          map.put("select_pole","เลือกเสาแล้วตกลง");
          map.put("product","สินค้า");
          map.put("productgroup","กลุ่มสินค้า");
          map.put("left","ซ้าย");
          map.put("right","ขวา");

      }else{
          map.clear();
          map.put("lang", "မြန်မာ");
          map.put("empcode", "ဝန်ထမ်းကုဒ်");
          map.put("stock", "စတော့");
          map.put("machine","ရုံ");
          map.put("channal","လိုင်း");
          map.put("move", "ရွှေ့");
          map.put("pole","နေရာတိုင်");
          map.put("cancel","ဖျတ်သိမ်း");
          map.put("confirm","လက်ခံ");
          map.put("save_location","သိမ်တဲ့ရုံ");
          map.put("edit","ပြုပြင်");
          map.put("total","အပေါင်း");
          map.put("bundle","အစီး");
          map.put("line","အချောင်းရေ");
          map.put("no_bundle","အမျိုးစားနံပါတ်");
          map.put("order","အမျိုးစား");
          map.put("amount","ဘယ်လောက်ရ");
          map.put("select_area","သံရှိထားလိုင်းရွေးပါ");
          map.put("select_pole","တိုင်နံဘက်ကိုနှပ်ပါပြီးရင်okနှပ်ပါ");
          map.put("product","ထုတ်ကုန်");
          map.put("productgroup","ထုတ်ကုန်အုပ်စု");
          map.put("left","ဝဲ");
          map.put("right","ညာဘက်ခြမ်း");
      }
    }
}
