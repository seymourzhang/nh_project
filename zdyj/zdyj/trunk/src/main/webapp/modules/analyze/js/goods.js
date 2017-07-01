/**
 * Created by LeLe on 11/14/2016.
 */
var ar ;

$(document).ready(function() {
    ar = getInstance({入库:"tab_1",耗用:"tab_2",库存:"tab_3"}, {iframe_tab_1:"inStock.cpt",iframe_tab_2:"outStock.cpt",iframe_tab_3:"stock.cpt"});
    ar.setCurrOrgId(org_id);
    ar.initToolBarFarm();
});

