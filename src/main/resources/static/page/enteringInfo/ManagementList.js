layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    //活动列表
    var tableIns = table.render({
        elem: '#managementList',
        url : '/management/detail/list',
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        id : "managementList",
        cols : [[
            {field: 'actityName', title: '活动', width:450, align:"center"},
            {field: 'residentName', title: '居民', width:80},
            {field: 'profit', title: '收益项', align:'center', templet:function(d){
                var assets = d.profit.assetProperties;
                var result = "";
                for(var index in assets){
                    var asset = assets[index];
                    result = result + "<li>" + asset.assetName + ":" +asset.amount +"</li></br>";
                }
                return result;
            }},
            {field: 'value', title: '收益价值', align:'center', sort: true}
        ]]
    });
})