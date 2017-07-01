/*
本文件属于配置文件
*/

// CONFIG_AppMode 配置服务端采用生产、测试、本地   'Production' 'Development' 'Local'
var CONFIG_AppMode = 'Production';  // 生产模式

// App的版本号
var CONFIG_App_Version = '1.2.8';

// 生产服务器地址
var CONFIG_Production_API_Host = 'http://egg.agnet.com.cn/eweb/';
// 测试服务器地址
var CONFIG_Development_API_Host = 'http://116.228.141.210:8088/eweb/';
// 本地服务器地址
var CONFIG_Local_API_Host = 'http://192.168.10.148:8081/sweb/';

// APK名称前缀
var CONFIG_apkNamePrefix = 'nht_layer_pro';

// 配置哪些action不显示 Loading动画
var CONFIG_actionURLArrayOfhideLoadIng = [
    "envCtrl/needAlarm.action"
];

// 持久数据
var persistentData = {
  //数据录入所选的栋舍
  'dataEntryReceiveHouse' :  '',

  //判断是否要设置竖屏
  'setVerticalKey': '',
  'switchRemind'          :  true,
  'transferUserArr'       :  [],
  'selectedUserInfo'      :  ''
};

//选择跳转的页面
var selectBackPage = {
  'firstTime':'',
  'NeedLogin':true
};

// 设备信息
var DeviInfo = {
  'ScreenHeight' : document.documentElement.clientHeight  ,
  'ScreenWidth'  : document.documentElement.clientWidth   ,
  'DeviceType'   : navigator.userAgent.toLowerCase()
};

// 临时变量
var tempVar = {
  'houseTemp' : {
          // 'id'              :''   , 
          // 'houseName'       :''   , //栋编号
          // 'h_length'        :''   ,
          // 'h_width'         :''   ,
          // 'h_height'        :''   ,
          // 'feedarea'        :''   , // 养殖面积
          // 'mtc_device_id'   :''
  },
  'userTemp' : {
          // 'id'     : ''          ,
          // 'name'   : ''          ,
          // 'role'   : {}          ,// 角色信息:01-厂长、02-饲养员、03-技术员、04-农场老板、05-管理员、06-维修员
          // 'tele'   : ''          ,
          // 'pw1'    : ''          ,
          // 'pw2'    : ''          ,
          // 'pw'     : ''          ,
          // 'houses_authority':[]  ,
          // 'houses' : ''     
  },
  'AlarmSetting' : {
            'HouseId'          :  ''  ,  //栋舍号
            'Delay'            :  ''  ,  //报警延迟
            'tempCpsation'     :  ''  ,  //温度补偿
            'tempCpsationVal'  :  ''  ,  //补偿数值
            'alarmProbe'       :  ''  ,  //报警探头
            'pointAlarm'       :  ''  ,  //点温差报警          
            "effAlarmProbe":  {
                            "tempLeft1":true,
                            "tempLeft2":true,
                            "tempMiddle1":true,
                            "tempMiddle2":true,
                            "tempRight1":true,
                            "tempRight2":true
                           },
            'tarTemp'          :  ''  ,
            'minTemp'          :  ''  ,
            'maxTemp'          :  ''  ,

  },
  'newBatchData'  : {
            "BatchCode"    :  ''  ,  //varchar型，批次编号
            "place_date"   :  ''  ,  //varchar型，入雏日期
            "place_num"    :  ''  ,  //int型，入雏数量
            "breed_days"   :  ''  ,  //int型，预计饲养天数
            "market_date"  :  ''  ,  //varchar型，预计出栏日
            "doc_vendors"  :  ''  ,  //varchar型，雏源厂家
            "breed"        :  ''     //varchar型,品种
  },
  'batchClearingData'  : {
            'companyName'       :  ''  ,
            'clearingQuantity'  :  ''  ,
            'giveQuantity'      :  ''  ,
            'clearingPrice'     :  ''  
            
  }
};

// 用户主信息
var sparraw_user = 
{                                             //user_State用来判断是否注册完成，true注册中，false注册完成
'profile':{'id_spa':0,'secret':'mtc_secret','user_State': true ,'account':'','password':''},
'weather':{
    "KeyInfo":{
        "WeatherCode1"    :  ""    ,//天气地址Code1
        "WeatherCode2"    :  ""    ,//天气地址Code2
        "WeatherCode3"    :  ""    ,//天气地址Code3
        "WeatherName1"    :  ""    ,//天气地址Name1
        "WeatherName2"    :  ""    ,//天气地址Name2
        "WeatherName3"    :  ""    ,//天气地址Name3
        "WeatherDate"     :  ""    // 天气预报的日期
    },
    "weatherinfo":[{
        "day_temp"      :  "-"  ,//白天温度
        "night_temp"    :  "-"  ,//晚上温度
        "day_desc"      :  ""  ,//白天天气
        "night_desc"    :  ""  ,//晚上天气
        "day_wind"      :  ""  ,//白天风向
        "night_desc"    :  ""  ,//晚上风向
        "day_speed"     :  ""  ,//白天风速
        "night_speed"   :  ""  ,//晚上风速
        "day_desc_png"  :  "" //图标
    },{
        "day_temp"      :  "-"  ,//白天温度
        "night_temp"    :  "-"  ,//晚上温度
        "day_desc"      :  ""  ,//白天天气
        "night_desc"    :  ""  ,//晚上天气
        "day_wind"      :  ""  ,//白天风向
        "night_desc"    :  ""  ,//晚上风向
        "day_speed"     :  ""  ,//白天风速
        "night_speed"   :  ""  ,//晚上风速
        "day_desc_png"  :  "" //图标
    },{
        "day_temp"      :  "-"  ,//白天温度
        "night_temp"    :  "-"  ,//晚上温度
        "day_desc"      :  ""  ,//白天天气
        "night_desc"    :  ""  ,//晚上天气
        "day_wind"      :  ""  ,//白天风向
        "night_desc"    :  ""  ,//晚上风向
        "day_speed"     :  ""  ,//白天风速
        "night_speed"   :  ""  ,//晚上风速
        "day_desc_png"  :  "" //图标
    }]},

"userinfo":{
          "id":"",
          "name": "",
          "role":"",
          "tele": "",
      "publicKeyExponent":"",
      "publicKeyModulus":"",
          "houses":[{
                "HouseId":'',
                "HouseName":"",
                "HouseBreedBatchId":'',
                "HouseBreedStatus":''
              }]
          },
"houseinfos":[{
           "id":"",
           "houseName":"",
           "h_length":"",
           "h_width":"",
           "h_height":"",
           "feedarea":"",
           "mtc_device_id":""
          }],
 "farminfo":{
           "id":"",
           "name":"",
           "address1":"", 
           "address2":"",
           "address3":"",
           "address4":"",
           "address5":"",
           "feedtype":"",
           "feedBreeds":"",
           "businessModle":"",
           "cageInfo_layer":"",
           "cageInfo_row":"",
           "corporation":"",
           "house_length":"",
           "house_width":"",
           "house_height":"",
           "feedarea":"",
           "house_Maxid":0,
           "farmBreedBatchId":0,
           "farmBreedBatchCode":'',
           "farmBreedStatus":""
          },
"userinfos":[{
        "id":"",
        "name": "",
        "tele": "",
        "role":"",
        "houses":['','']
        }],
"LoginResult":"",
"Authority": {
    "HouseBreed": "", //入雏出栏权限
    "basicInfo": "", //信息维护权限
    "TaskDeal": "", //任务处理权限
    "TaskSetting": "", //任务设置权限
    "DailyInput": "", //生产记录权限
    "AlarmSetting": "", //报警设置权限
    "role": 0, 
    "MonitorDeal": "", //报警延迟权限
    "EggSale": "", //销售记录权限
    "FarmBreed": ""//农场管理权限
}
};