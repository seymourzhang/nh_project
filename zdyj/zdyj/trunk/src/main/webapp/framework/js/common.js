/**
 * Created by LeLe on 2/22/2017.
 */
// 设置全局对象
function setGlobalObj(mid, obj){
    window.top['_CACHE'][mid] = obj;
    return window.top['_CACHE'][mid];
};

// 获取全局对象
function getGlobalObj(mid){
    return window.top['_CACHE'][mid];
}

// 获取菜单标签对象
function getMenuTabObj(){
    return getGlobalObj("menu_tab");
}