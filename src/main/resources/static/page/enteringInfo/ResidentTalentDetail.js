var form, $,areaData;
layui.config({
    base : "../../js/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','upload','laydate',"address","element"],function(){
    form = layui.form;
    element = layui.element;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;

    var formSelects = layui.formSelects;
    //初始化下拉框内容
    $.getJSON("/info/meta/list",function(data){
        var activities = data.activities;
        $(activities).each(function() {
            var option = '<option value="'+ this.id + '">' + this.description + '</option>';
            $(".activity-select").append(option);
        });
        var buildings = data.buildings;
        $(buildings).each(function() {
            var option = '<option value="'+ this.id + '">' + this.name + '</option>';
            $(".building-select").append(option);
        });
        var professions = data.professions;
        $(professions).each(function() {
            var option = '<option value="'+ this.value + '">' + this.name + '</option>';
            $(".profession-select").append(option);
        });
        var jobs = data.jobs;
        $(jobs).each(function() {
            var option = '<option value="'+ this.value + '">' + this.name + '</option>';
            $(".job-select").append(option);
        });
        var targets = data.targets;
        $(targets).each(function() {
            var option = '<option value="'+ this.value + '">' + this.name + '</option>';
            $(".target-select").append(option);
        });
        var assets = data.assets;
        $(assets).each(function() {
            var option = '<option value="'+ this.id + '">' + this.name + '</option>';
            $(".asset-select").append(option);
        });
        var operations = data.operations;
        $(operations).each(function() {
            var option = '<option value="'+ this.value + '">' + this.operator + '</option>';
            $(".operation-select").append(option);
        });
        //填充模板
        // $("#menu-area").before($("#enhancement-blank-template").html());
        form.render();
    });

    //初始化居民列表
    $.getJSON("/resident/list",function(data){
        var residentGroup = data.residentGroup;
        var backgroud1 = '<div style="z-index: 2;"><img src="';
        var backgroud2 = '"></div>';
        var imgPath = '';
        for(var grade in residentGroup){
            if(grade == "天"){
                imgPath = 'https://patchwiki.biligame.com/images/jiangnan/thumb/0/03/5n7cwgxjrd3nqdxb79fcxufgi93reiv.png/120px-%E5%A4%B4%E5%83%8F%E6%A1%86_%E5%A4%A9.png';
            }else if(grade == "侯"){
                imgPath = 'https://patchwiki.biligame.com/images/jiangnan/thumb/a/a2/5hoq4dreqp5of4mmm4xajmpj8t1rar3.png/120px-%E5%A4%B4%E5%83%8F%E6%A1%86_%E4%BE%AF.png';
            }else if(grade == "卿"){
                imgPath = 'https://patchwiki.biligame.com/images/jiangnan/thumb/e/e8/nb9ev6kwmyqtdorvyqijiti0ozjf3re.png/120px-%E5%A4%B4%E5%83%8F%E6%A1%86_%E5%8D%BF.png';
            }else{
                imgPath = 'https://patchwiki.biligame.com/images/jiangnan/thumb/f/f8/k6esvb99yd26u1gpi2bylcbywttgnkz.png/120px-%E5%A4%B4%E5%83%8F%E6%A1%86_%E5%A3%AB.png';
            }
            var backgroud = backgroud1 + imgPath + backgroud2;
            var iconHtml = '<fieldset class="layui-elem-field"><legend>' + grade +'</legend><div class="layui-field-box"><ul class="icons layui-row">';
            var residents = residentGroup[grade];
            for(var residentIndex = 0;residentIndex < residents.length;residentIndex++){
                var resident =  residents[residentIndex].resident;
                var talent =  residents[residentIndex].talent;
                iconHtml += '<li class="layui-col-xs4 layui-col-sm3 layui-col-md2 layui-col-lg3 talent-resident-list-item"' +
                    'resident-id="'+ resident.id +'" '+
                    'talent-id="'+ talent.id +'" '+
                    'talent-name="'+ talent.name +'"><div style="display: flex;justify-content: center;align-items: center;position: relative;">'+
                    backgroud +
                    '<div class="bgselect" style="position: absolute;z-index: 1;"><img src="'+ resident.avatar+'" class="userAvatar"></br></div></div>' + resident.name + '</li>';
            }
            iconHtml += "</ul></div></fieldset>";
            $("#talent-resident-list").append(iconHtml);
        }
    });
    $("body").on("click",".talent-resident-list-item",function(){
        $("#talent-resident-name").text($(this).text());
        $("#redident-avatar").attr("src",$(this).find("img.userAvatar").attr("src"));
        $("#talent-name").text($(this).attr("talent-name"));
        var talentId = $(this).attr("talent-id");
        //改换Tab内容
        $.getJSON("/talent/stage/list",{talentId:talentId},function(data){
            //先把空的tab框架放进去
            $("#enhancement-area").html($("#enhancement-tab-frame").html());
            //填入标题和无项目的表单
            var liList = '',tabList = '';
            for(var index = 0; index < data.length; index++){
                var stage = data[index];
                //加入新的Tab页签
                liList = liList + '<li lay-id="'+ stage.level +'">' + stage.level + '</li>';
                //Tab内容的框架
                $("#enhancement-area .layui-tab-content").append($("#enhancement-tab-content").html());
                //选中隐藏表单，填入stageid
                $("#enhancement-area .layui-tab-content .menu-area:eq("+ index +")").find("[name=stageId]").val(stage.id);
                var enhancements = data[index].enhancements;
                for(var index2 = 0; index2 < enhancements.length; index2++){
                    //加入条件的模板
                    $("#enhancement-area .layui-tab-content .menu-area:eq("+ index +")").before($("#enhancement-blank-template").html());
                    var enhancement = enhancements[index2];
                    var operations = enhancement.operations;
                    for(var index3 = 0; index3 < operations.length; index3++){
                        //加入结果模板
                        $("#enhancement-area .result-box:eq("+ index +")").append($("#result-item-template").html());
                    }
                }
            }
            $("#enhancement-area ul").html(liList);
            form.render();
            //遍历所有表单，为其赋值
            // var tablist = $(".layui-tab-content layui-tab-item");
            for(var stageIndex = 0; stageIndex < data.length; stageIndex++){
                var tab = $(".layui-tab-content .layui-tab-item").get(stageIndex);
                var tabData =  data[stageIndex];

                // var eList = $(".layui-tab-content").find(".eItem");
                var eList = $(tab).find(".eItem");
                $(tab).find(".stage-desc").html(tabData.description);
                var eDataList =  tabData.enhancements;
                for(var eIndex = 0; eIndex < eDataList.length; eIndex++){
                    var eitem = eList.get(eIndex);
                    var eitemData = eDataList[eIndex];
                    $(eitem).find("[name=enhancementId]").val(eitemData.id);
                    $(eitem).find("[name=activity]").val(eitemData.qualification.activityId);
                    $(eitem).find("[name=building]").val(eitemData.qualification.buildingId);
                    $(eitem).find("[name=profession]").val(eitemData.qualification.profession.value);
                    $(eitem).find("[name=job]").val(eitemData.qualification.job.value);

                    var oList = $(eitem).find(".operationItem");
                    var oDataList = eitemData.operations;
                    for(var oIndex = 0; oIndex < oDataList.length; oIndex++){
                        var oitem = oList[oIndex];
                        var oDataitem = oDataList[oIndex];
                        $(oitem).find("[name=target]").val(oDataitem.operationTarget.value);
                        $(oitem).find("[name=asset]").val(oDataitem.assetId);
                        $(oitem).find("[name=operation]").val(oDataitem.operation.value);
                        $(oitem).find("[name=operand]").val(oDataitem.operand);
                    }
                }
            }
            form.render();
            element.render('tab');
            if(data.length > 0){
                element.tabChange('detail-tab',data[0].level);
            }
        });
    })

    //添加提升项
    form.on("submit(addEnhancement)",function(data){
        $(data.elem.parentElement.parentElement).before($("#enhancement-blank-template").html());
        $(data.elem.parentElement.parentElement.parentElement).find(".result-box").append($("#result-item-template").html());;
        form.render();
        return false;
    });
    //添加作用下项目
    form.on("submit(addResult)",function(data){
        $(data.elem.parentElement).find(".result-box").append($("#result-item-template").html());
        form.render();
        return false;
    });
    //删除提升项
    form.on("submit(delEnhancementItem)",function(data){
        data.elem.parentElement.parentElement.parentElement.parentElement.remove();
        return false;
    });
    //删除提升项
    form.on("submit(delEnhancement)",function(data){
        data.elem.parentElement.remove();
        return false;
    });

    //保存提升项
    form.on("submit(saveEnhancements)",function(data){
        var tab = $(data.elem.parentElement.parentElement.parentElement);
        var eList = $(tab).find(".eItem");
        var stageId = $(tab).find("[name=stageId]").val();
        var datas = new Array();
        for(var eIndex = 0; eIndex < eList.length; eIndex++){
            var eitem = eList.get(eIndex);
            var qualification = {
                activityId: $(eitem).find("[name=activity]").val(),
                buildingId: $(eitem).find("[name=building]").val(),
                profession : $(eitem).find("[name=profession]").val(),
                job : $(eitem).find("[name=job]").val()
            }

            var operations = new Array();
            var resultList = $(eitem).find(".result-box .layui-field-box");
            for(var rIndex = 0; rIndex < resultList.length; rIndex++){
                var ritem = resultList.get(rIndex);
                var oItem = {
                    "assetId" : $(ritem).find("[name=asset]").val(),
                    "operand" : $(ritem).find("[name=operand]").val(),
                    "operationTarget" : $(ritem).find("[name=target]").val(),
                    "operation" : $(ritem).find("[name=operation]").val(),
                }
                operations.push(oItem);
            }
            enhancementId = $(eitem).find("[name=enhancementId]").val();
            var eData = {
                talentStageId : stageId,
                id : enhancementId,
                qualification : qualification,
                operations : operations
            }
            datas.push(eData);
        }
        $.post("/talent/stage/enhancement/list",{
            enhancements : JSON.stringify(datas),
            stageId : stageId
        },function(data){
            layer.tips(data.operationType + "  " + data.count +
            " 项天赋"+ "  "  + data.resultType, "#redident-avatar", null);
        });
        return false;
    });
})