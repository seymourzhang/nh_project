

/*************线性图表数据****************/

function Echart_initLine02(
    xShaftSumData  ,    //x轴数据 必填项                       数据样式 = ['1','2','3','4','5'];
    /*  
    displayConfig 是一个JSON配置项(必填),具体定义如下：
        [
            {
                "yName":"标准",                // 数据名称                            必填项
                "yData":['10','20','30'],     // y轴数据                             必填项
                "yType":"bar",                // bar-柱状图 line-曲线图 area-区域图    非必填项（默认值是line)
                "yStack":"",                  //                                     非必填项（默认值是 '')
                "yAxisIndex": 0,              // 0-左轴显示 1-右轴显示                 非必填项（默认值是0)
                "SymbolSize":0,               // 是否折线 0-不折线 1-折线              非必填项（默认值是0)
                "yColor":'#EEEE00',           // 曲线颜色                            非必填项
                "needSelected":true           // 是否选中，true-选中，false-隐藏       非必填项（默认值是true)
            }
        ] 
    */
    displayConfig  ,                                                                 
    leftName       ,    //左轴线名字                                                     非必填项
    leftRange      ,    //左轴线值区间,类型为数组，包含两个值，最小值，最大值 demo:[0,100]      非必填项
    rightShow      ,    //是否显示右轴                                                    非必填项
    rightName      ,    //右轴线名字                                                     非必填项
    rightRange     ,    //右轴线值区间,类型为数组，包含两个值，最小值，最大值 demo:[0,100]      非必填项
    clickFun       ,     //触摸时触发的事件，以及显示淡灰色框样式                              非必填项
    xLineName      ,     //x轴名字
    touchColumnNum      //触摸框列数
  ) {
    if (!xShaftSumData || !displayConfig ) {
        return app_alert("displayConfig数据不全，请检查。");
    }


    if (!leftName) {
        leftName = "";
    }

    if (!rightShow) {
        rightShow = false;
    }

    if (!rightName) {
        rightName = "";
    }

    if (!touchColumnNum) {
        touchColumnNum = 1;
    }
    
    if (!clickFun) {
        clickFun = function (params) {
            var xData = null;
            for (var i = 0; i < params.length; i++) {
                if (params[i].data != null) {
                    xData = params[i].name;
                };
            };

            var res = "";

            if(xData){
                if (!xLineName) {
                    res =  xData + '<br/>';//x轴的字
                }else{
                    res =  xLineName + "：" + xData + '<br/>';//x轴的字
                }
            }

            for (var i = 0; i < params.length; i++) {
                if (i % touchColumnNum == 0) {
                    res += params[i].seriesName + "：" + (params[i].data || params[i].data == 0 ?params[i].data:'-') + '&nbsp;&nbsp;';

                    if(i != params.length -1){
                        res += "<br/>";
                    }
                }else{
                    res +=  params[i].seriesName + "：" + (params[i].data || params[i].data == 0 ?params[i].data:'-') + '&nbsp;&nbsp;';
                }
            };
            return res;                                         
       }
    }

    //  默认颜色列表
    var defaultColorArrays = ['#EE82EE','#FFA500','#CC9933','#CCCC00','#993333','#663366',  '#DC143C','#00FFFF','#00EE76','#D1EEEE'];

    var legendData = [];  // 图例数据名称
    var selectedData = {};  // 选中的数据名称
    for (var i = 0; i < displayConfig.length; i++) {
        legendData.push(displayConfig[i].yName);
        if (displayConfig[i].needSelected != undefined) {
            selectedData[displayConfig[i].yName] = displayConfig[i].needSelected;
        }else{
            selectedData[displayConfig[i].yName] = true;
        }
    }

    // 图例数据
    var legendArray = {
        data: legendData,
        selected: selectedData,
        textStyle: {
           fontSize: 8 // 更改标题label文字大小
        }
    };
    
    var ylMin, ylMax, yRMax, yRMin;  // 左右Y轴的范围值

    if (leftRange && leftRange.length != 2) {
        return app_alert("leftRange数据有误，请检查。");
    }

    if (leftRange && leftRange.length == 2) {
        if (leftRange[0] != undefined) {
            ylMin = leftRange[0];
        }
        if (leftRange[1] != undefined) {
            ylMax = leftRange[1];
        }
    }

    if (rightRange && rightRange.length != 2) {
        return app_alert("rightRange数据有误，请检查。");
    }

    if (rightRange && rightRange.length == 2) {
        if (rightRange[0] != undefined) {
            yRMin = rightRange[0];
        }
        if (rightRange[1] != undefined) {
            yRMax = rightRange[1];
        }
    }
    
    var seriesDataArray = [];
    var myChart = echarts.init(document.getElementById('main'));
    var option = {
        tooltip: {
            trigger: 'axis',
            textStyle: {
                fontSize: 13
            },
            backgroundColor: 'rgba(96,96,96,0.5)',
            //显示框的颜色
            formatter: clickFun
        },
        legend: legendArray,
        grid: //表对应上下左右的大小
        {
            x: 50,
            y: 50,
            x2: 50,
            y2: 30
        },
        xAxis: [{
            name: xLineName ? xLineName : '',
            type: 'category',
            data: xShaftSumData,
            nameLocation: 'end',
            show: true,
            //是否显示x轴
            axisTick: true,
            axisLabel: {
                //margin:10 //文字与x轴的距离
            },
            splitLine: false,
            axisLine: {
                lineStyle: { //x轴风格
                    color: '#000',
                    width: 0.5
                }
            }
        }],
        yAxis: [{
            name: leftName,
            position: 'left',
            type: 'value',
            nameTextStyle: {
                fontSize: 10
            },
            axisLabel: {
                formatter: '{value}' //左边的数据
            },
            scale: true,
            axisLine: {
                lineStyle: {
                    color: '#000',
                    width: 1
                }
            },
            min: ylMin,
            max: ylMax
        },{
            name: rightName,
            show: rightShow,
            position: 'right',
            type: 'value',
            nameTextStyle: {
                fontSize: 10
            },
            axisLabel: {
                formatter: '{value}' //右边的数据
            },
            scale: true,
            splitLine: false,//是否显示分割线
            axisLine: {
                lineStyle: {
                    color: '#000',
                    width: 1
                }
            },
            min: yRMin,
            max: yRMax
        }],
        series: seriesDataArray
    };
    
    for (var i = 0; i < displayConfig.length; i++) {

        //  默认值
        var mmAreaStyle = null;
        var mmYAxisIndex = 0;
        var mmType = 'line'; 
        var mmYColour = null;  // 线颜色
        var mmSymbolSize = 0;
        var mmStack  = '';

        // 设定左轴还是右轴
        if (displayConfig[i].yAxisIndex) {
            mmYAxisIndex  = displayConfig[i].yAxisIndex;
        }

        // 设定图表形态
        if (displayConfig[i].yColor) {
            mmYColour = displayConfig[i].yColor;
        }else{
            mmYColour = defaultColorArrays[i];
        }

        //设置是否折线
        if (displayConfig[i].SymbolSize) {
            mmSymbolSize = displayConfig[i].SymbolSize;
        }

        // 设定图表形态
        if (displayConfig[i].yType) {
            // bar-柱状图 line-曲线图 area-区域图
            if(displayConfig[i].yType == 'area'){
                mmAreaStyle = { "color" : mmYColour} ;
            }else{
                mmType = displayConfig[i].yType;
            }
        }
        //设置是否折线
        if (displayConfig[i].yStack) {
            mmStack = displayConfig[i].yStack;
        }
        
        if(mmType != 'bar'){
            mmStack = '';
        }

        seriesDataArray.push({
                    name: displayConfig[i].yName,
                    type: mmType,
                    stack: mmStack,
                    yAxisIndex: mmYAxisIndex,
                    data: displayConfig[i].yData,
                    smooth: true,
                    //是否折线
                    symbolSize: mmSymbolSize,
                    //点得大小
                    itemStyle: {
                        normal: {
                            color: mmYColour,
                            lineStyle: {
                                color: mmYColour,
                                width: 1
                            },
                            areaStyle: mmAreaStyle
                        }
                    }
        });

        
    }

    myChart.setOption(option);
    
    window.onresize = function() {};
}
