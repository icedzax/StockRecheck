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
          map.put("move", "ย้ายเสา");
      }else{
          map.clear();
          map.put("lang", "မြန်မာ");
          map.put("empcode", "ဝန်ထမ်းကုဒ်");
          map.put("stock", "စတော့");
          map.put("move", "ရွှေ့");
      }
    }
}
