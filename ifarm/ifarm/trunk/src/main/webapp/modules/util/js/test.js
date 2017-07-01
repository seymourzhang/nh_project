/**
 * Created by LeLe on 2017-05-10.
 */


$(function () {
    'use strict';
    var url = path + "/info/upload?upload_file_type=1";
    $('#fileupload').fileupload({
        url: url,
        autoUpload: true,
        add: function (e, data){
            if(data.originalFiles.length>0){
            }
            data.submit();
        },
        done: function (e, data) {
            var rt = '(' + data.result + ')';
            console.log(rt);
            var json = eval(rt);
            if(json.success == true){
                console.log(json.obj);
            } else {
                console.log(json.obj);
            }
            return;
        },
        fail:function (e, data) {
            console.log(e);
            return;
        }
    });
});
