layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    //活动列表
    var tableinit = function (data){
        if(!data){
            data = {
                buff : 3
            }
        }
        table.render({
            elem: '#managementList',
            url : '/management/detail/list',
            method:'post',
            where:data,
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
    };
    tableinit();

    //初始化下拉框内容
    $.getJSON("/info/meta/list",function(data){
        var activityList = new Array();
        var activities = data.activityList;
        $(activities).each(function() {
            var option = {
                name: this.description,
                value: this.description
            };
            activityList.push(option);
        });
        var activitySelect = xmSelect.render({
            el: '#activity-select',
            toolbar: {
                show: true,
            },
            filterable: true,
            data: activityList
        })

        var assetList = new Array();
        var assets = data.assets;
        $(assets).each(function() {
            var option = {
                name: this.name,
                value: this.name
            };
            assetList.push(option);
        });
        var assetSelect = xmSelect.render({
            el: '#asset-select',
            toolbar: {
                show: true,
            },
            filterable: true,
            data: assetList
        })

        var residentList = new Array();
        var residents = data.residents;
        $(residents).each(function() {
            var option = {
                name: this.name,
                value: this.name
            };
            residentList.push(option);
        });
        var residentSelect = xmSelect.render({
            el: '#resident-select',
            toolbar: {
                show: true,
            },
            filterable: true,
            data: residentList
        })
    });

    //搜索经营活动
    form.on("submit(searchManagement)",function(data){
        var buff = 0;
        layui.each($("[name=buff]:checked"),function(index, data){
            buff = buff | data.value;
        });
        var formData = {
            activityNames:xmSelect.get('#activity-select')[0].getValue('valueStr'),
            residentNames:xmSelect.get('#resident-select')[0].getValue('valueStr'),
            assetNames:xmSelect.get('#asset-select')[0].getValue('valueStr'),
            buff:buff
        }
        tableinit(formData);
        return false;
    });
})