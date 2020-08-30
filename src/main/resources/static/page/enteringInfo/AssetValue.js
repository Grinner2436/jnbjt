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

    //初始化下拉框内容
    $.getJSON("/asset/value/list",function(data){
        for(var index in data){
            var asset = data[index];
            var template = $("#item-template").html();
            if(!asset.value){
                asset.value = 0;
            }
            template = template.replace("{assetName}",asset.name)
                .replace("{assetId}",asset.id)
                .replace("{assetValue}", asset.value)
            ;
            $("#btn-group").after(template);
        }
        form.render();
    });

    //保存提升项
    form.on("submit(saveAssetValue)",function(data){
        var formData = form.val("asset-value-form");
        var index = 0;
        for(var id in formData) {
            var value = formData[id];
            formData['assetList[' + index +'].id'] = id;
            formData['assetList[' + index +'].value'] = value;
            index++;
        }
        $.post("/asset/value/",formData,function(data){
            layer.tips(data.operationType + "  " + data.count +
                " 项资源信息", "#save-btn", null)
        });
        return false;
    });
})