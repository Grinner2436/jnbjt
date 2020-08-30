var form, $,areaData;
layui.config({
    base : "../../js/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','transfer','laydate',"address","element"],function(){
    form = layui.form;
    element = layui.element;
    transfer = layui.transfer;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;

    //初始化活动列表
    $.getJSON("/revitalization/list",function(data){
        for(var index in data){
            var revitalization = data[index];
            $("#revitalizationList").append('<li class="revitalization-list-item" revitalization-id="'+ revitalization.id +'"><button type="button" class="layui-btn layui-btn-fluid">' + revitalization.name +'</button></li>');
        }
        $("#add-revitalization-list-item").click();
    });
    $("body").on("click",".revitalization-list-item",function(){
        //初始化页面内容
        var id = $(this).attr("revitalization-id");
        $("[name=id]").val(id);
        $.getJSON("/info/meta/list",function(data){
            if(id){
                $.getJSON("/revitalization/" + id,function(info){
                    var activities = data.activities;
                    var selectedActivitiesSource = info.activities;

                    var selectedActivityIds = new Array();
                    var selectedActivities = new Array();
                    for(var index in selectedActivitiesSource){
                        selectedActivityIds.push(selectedActivitiesSource[index].id);
                        selectedActivities.push(selectedActivitiesSource[index]);
                    }
                    transfer.render({
                        elem: '#activityList'
                        ,data: activities
                        ,title: ['备选活动', '已选活动']
                        ,showSearch: true
                        ,id: 'activityList'
                        ,parseData: function(res){
                            return {
                                "value": res.id //数据值
                                ,"title": res.description //数据标题
                                ,"disabled": ""  //是否禁用
                                ,"checked": "" //是否选中
                            }
                        },value:selectedActivityIds,
                        onchange: function(source, direction){
                            for(var index in source){
                                var data = source[0];
                                if(direction == 0){
                                    //添加一项
                                    var template = $("#income-template").html();
                                    template = template.replace("{activityName}",data.title)
                                        .replace("{activityId}","income-" + data.value)
                                        .replace("{income-item-id}","income-item-" + data.value)
                                    ;
                                    $("#activityIncomeList").append(template);
                                }else {
                                    //移除一项
                                    $("#income-item-" + data.value).remove();
                                }
                            }
                        }
                    });
                    //初始化income
                    $("#activityIncomeList").html("");
                    for(var index in selectedActivities){
                        var activity = selectedActivities[index];
                        var template = $("#income-template").html();
                        template = template.replace("{activityName}",activity.description)
                            .replace("{activityId}","income-" + activity.id)
                            .replace("{income-item-id}","income-item-" + activity.id)
                        ;
                        $("#activityIncomeList").append(template);
                        $("#income-" + activity.id).val(activity.income);
                    }

                    // $(".operation-select").html("")
                    // var operations = data.operations;
                    // $(operations).each(function() {
                    //     var option = '<option value="'+ this.value + '">' + this.operator + '</option>';
                    //     $(".operation-select").append(option);
                    // });
                    // $(".operation-select").val(info.operation.value);
                    // $("[name=operand]").val(info.operand);
                    $("[name=name]").val(info.name);
                    $("[name=id]").val(info.id);
                    form.render();
                });
            }else{
                $("#activityIncomeList").html("");
                $("[name=name]").val("");
                $("[name=id]").val("");
                var activities = data.activities;
                transfer.render({
                    elem: '#activityList'
                    ,data: activities
                    ,title: ['备选活动', '已选活动']
                    ,showSearch: true
                    ,id: 'activityList'
                    ,parseData: function(res){
                        return {
                            "value": res.id //数据值
                            ,"title": res.description //数据标题
                            ,"disabled": ""  //是否禁用
                            ,"checked": "" //是否选中
                        }
                    }, onchange: function(source, direction){
                        for(var index in source){
                            var data = source[index];

                            if(direction == 0){
                                //添加一项
                                var template = $("#income-template").html();
                                template = template.replace("{activityName}",data.title)
                                    .replace("{activityId}","income-" + data.value)
                                    .replace("{income-item-id}","income-item-" + data.value)
                                ;
                                $("#activityIncomeList").append(template);
                            }else {
                                //移除一项
                                $("#income-item-" + data.value).remove();
                            }
                        }
                    }
                });
                var operations = data.operations;
                $(operations).each(function() {
                    var option = '<option value="'+ this.value + '">' + this.operator + '</option>';
                    $(".operation-select").append(option);
                });
                form.render();
            }
        });
    })


    //删除提升项
    form.on("submit(delRevitalization)",function(data){
        var id = $("[name=id]").val();
        //移除左侧
        $("[revitalization-id="+ id +"]").remove();
        //清空表单
        $("#add-revitalization-list-item").click();
        $.post("/revitalization/" + id,{
            "_method" : "DELETE"
        },function(data){

        });
        return false;
    });

    //保存提升项
    form.on("submit(saveRevitalization)",function(data){
        var formData = form.val("revitalization-form");
        var activities = layui.transfer.getData('activityList');
        for(var index in activities) {
            var activity = activities[index];
            var activityId = activity.value;
            var income = $("#income-" + activityId).val();
            formData['activities[' + index +'].id'] = activityId;
            formData['activities[' + index +'].income'] = income;
        }
        $.post("/revitalization",formData,function(data){
            layer.tips(data.operationType + "  " + data.count +
            " 项活动"+ "  "  + data.resultType, "#revitalizationList", null)
        });
        return false;
    });
})