layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    //天赋列表
    var tableIns = table.render({
        elem: '#talentList',
        url : '/resident/talent/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limit : 20,
        limits : [10,15,20,25],
        cols : [[
            {field: 'resident', title: '居民'},
            {field: 'talent',title: '天赋', align:'center'},
            {field: 'stageLevel', title: '等级', align:'center'},
            // {field: 'talent', width: 120,title: '更新', align:'center'},
            {field: 'stageDescription', title: '描述', align:'center'},
            {field: 'talentDetails', title: '添加详情', align:'center', templet: function (data) {
                var result = '';
                $.ajaxSetup({
                        async: false
                });
                $.get("/asset/name/list",function(assetNames){
                        var assetSelectDiv ='<div class="layui-form-item"><label class="layui-form-label">资源类型</label><div class="layui-input-block"><select name="assetName" lay-verify="required" lay-search="">';
                        for(var assetName in assetNames){
                            assetSelectDiv = assetSelectDiv + '<option value="'+ assetName +'">' + assetName + '</option>';
                        }
                        assetSelectDiv = assetSelectDiv + '</select></div></div>';

                        var operationSelectDiv ='<div class="layui-form-item"><label class="layui-form-label">操作类型</label><div class="layui-input-block"><select name="operation" lay-verify="required">' +
                            '<option value="Add">Add</option><option value="Multiply">Multiply</option></select></div></div>';

                        var operandInput =   '<div class="layui-form-item">' +
                            '<label class="layui-form-label">操作数</label><div class="layui-input-block"><input type="text" name="operand" placeholder="请输入数量"  class="layui-input"></div></div>';

                        var buttons = '<div class="layui-form-item"><div class="layui-input-block"><button type="button" class="layui-btn layui-btn-normal"  lay-event="addAsset">添加</button></div></div>';
                        var buttons1 = '<div class="layui-form-item"><div class="layui-input-block"><button type="button" class="layui-btn layui-btn-normal"  lay-event="saveAssets">保存</button></div></div>';
                        result = result + '<form class="layui-form" action="" id="talent-detail-form">' + assetSelectDiv + operationSelectDiv + operandInput + buttons + buttons1 + '</form>';
                });
                return result;
            }},
            {field: 'talentDetails', title: '天赋详情', align:'center', templet: function (data) {
                var result = '<table id="talent-detail-table-'+ data.stageId+ '" lay-filter="talent-detail-table"></table>';
                return result;
            }},
        ]],done: function(res, curr, count){
            $(res.data).each(function (index,dataItem) {
                var tableData = {
                    code:0,
                    data:dataItem.talentDetails,
                    count:dataItem.talentDetails.length,
                    msg:''
                };
                var tableIns = table.render({
                    elem: '#talent-detail-table-' + dataItem.stageId,
                    data : tableData,
                    cellMinWidth : 95,
                    limit : 20,
                    limits : [10,15,20,25],
                    cols : [[
                        {field: 'assetName', title: '资本类型'},
                        {field: 'operation', title: '操作类型', align:'center'},
                        {field: 'operand', title: '操作数', align:'center'}
                    ]]
                });
                table.resize('talentList');
            });
        }
    });


    //添加数据操作
    table.on('tool(talent-list-filter)', function(dataItem){
        var layEvent = dataItem.event;
        if(layEvent == 'addAsset'){
            var oldData = dataItem.data.talentDetails;
            var newData = {
                assetName : dataItem.tr[0].querySelector("[name=assetName]").value,
                operation : dataItem.tr[0].querySelector("[name=operation]").value,
                operand : dataItem.tr[0].querySelector("[name=operand]").value
            }
            oldData.push(newData);
            dataItem.data.talentDetails = oldData;
            var tableData = {
                code:0,
                data:oldData,
                count:oldData.length,
                msg:''
            };
            var stageId = dataItem.data.stageId;
            var tableHandle = "talent-detail-table-"+ stageId;
            table.reload(tableHandle, {data : oldData});
        }else if(layEvent == 'saveAssets'){
            var datas = new Array();
            var stageId = dataItem.data.stageId;
            var tableHandle = "talent-detail-table-"+ stageId;
            var trs = dataItem.tr[0].querySelector("div[lay-id=" + tableHandle+"]").querySelectorAll("tr");
            for(var tridx =0; tridx < trs.length;tridx++){
                var tr = trs[tridx];
                var ths = tr.querySelectorAll("td");
                if(ths.length <= 0){
                    continue;
                }
                datas.push({
                    assetName:ths[0].textContent,
                    operation:ths[1].textContent,
                    operand:ths[2].textContent
                });
            }
            if(datas.length > 0){
                $.post("/talent/stage/enhancement/list",{
                    stageId : stageId,
                    enhancements : datas,
                },function(data){});
            }
        }
    });

    //删除数据操作
    table.on('tool(talent-detail-table)', function(dataItem){
        dataItem.del();
    });


    //是否置顶
    form.on('switch(newsTop)', function(data){
        var index = layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            if(data.elem.checked){
                layer.msg("置顶成功！");
            }else{
                layer.msg("取消置顶成功！");
            }
        },500);
    })

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("newsListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加文章

    $(".addNews_btn").click(function(){
        editTalent();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('newsListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].newsId);
            }
            layer.confirm('确定删除选中的文章？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            })
        }else{
            layer.msg("请选择需要删除的文章");
        }
    })


})