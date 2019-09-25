"use strict"

/**
 * @title: stringToArray
 * @author: chenguangju
 * @date: 2019/9/23 13:00
 * @description: 字符串转为字符串数组
 * @param: list
 * @return: arrayNew
 */
function stringToArray(list) {
    var array = list.split(",");
    var arrayNew=[];
    for(var i=0;i<array.length;i++){
        if(i==0){
            arrayNew[i] = array[i].replace("[","");
        }else if(i==array.length-1){
            arrayNew[i] = array[i].replace("]","");
        }else{
            arrayNew[i] = array[i]
        }
    }
    return arrayNew;
}
/**
 * @title: initVaccine
 * @author: chenguangju
 * @date: 2019/9/23 12:13
 * @description: 疫苗企业、疫苗规格初始化
 * @param:
 * @return:
 */
function initVaccine() {
    //疫苗企业
    var entVaccineNameArray = stringToArray(entVaccineNameList);
    for(var j=0;j<entVaccineNameArray.length;j++){
        var optionVar ="<option value='"+entVaccineNameArray[j]+"'>"+entVaccineNameArray[j]+"</option>";
        optionVaccineVar +=optionVar;
    }
    //疫苗规格
    var vaccineSpecsCodeArray = stringToArray(vaccineSpecsCodeList);
    for (var i=0;i<vaccineSpecsCodeArray.length;i++){
        var optionVar ="<option value='"+vaccineSpecsCodeArray[i]+"'>"+vaccineSpecsCodeArray[i]+"</option>";
        optionSpecsVar +=optionVar;
    }
}

/**
 * @title: initVaccineRow
 * @author: chenguangju
 * @date: 2019/9/23 12:04
 * @description: 初始化行
 * @param:
 * @return: 5行
 */
function initVaccineRow() {
    for(var i=0;i<5;i++){
        addVaccineRow();
    }
}

/**
 * 疫苗新增一行
 * @author chenguangju
 * @date 2019/9/18 8:39
 * @param
 * @return
 */
var optionVaccineVar;//企业
var optionSpecsVar; //疫苗规格
function addVaccineRow() {
    //获取tr数量
    var time2 = new Date().format("yyyy-MM-dd");
    var $num = $("#tbody").children('tr').length+1;
    var $td0 = $("<td width='10%'>"+$num+"</td>");
    var $td1 = $("<td><label id='label"+$num+"'></label></td>");
    var $td2 = $("<td><select class='required'id='entName"+$num+"' name='entName"+$num+"' style='width: 100%' onchange=findEntNo('entName"+$num+"','"+$num+"')><option>--请选择--</option></select></td>");
    $($td2).find("select").append(optionVaccineVar);
    var $td3 = $("<td><input id='vaccineName"+$num+"' value=''/></td>");
    var $td4 = $("<td><input id='batchNo"+$num+"' value=''/></td>");
    var $td5 = $("<td><input id='validityMonth"+$num+"' /></td>");
    var $td6 = $("<td><input id='produceDate"+$num+"'value='"+time2+"' readonly onclick=vaccineTime() /></td>");
    var $td7 = $("<td><input id='validityAmount"+$num+"' value=''/></td>");
    var $td8 = $("<td><label>瓶</label></td>");
    var $td9 = $("<td><select class='required' id='specs_code"+$num+"' name='specs_code"+$num+"' style='width: 100%;'><option>--请选择--</option></select></td>");
    $($td9).find("select").append(optionSpecsVar);
    var $td10 = $("<td><img id='delete' src='"+imgSrc+"' onclick='deleteSelectedRow(" + $num + ")'/></td>");
    var tr = $("<tr id="+$num+"></tr>");
    $(tr).append($td0).append($td1).append($td2).append($td3).append($td4).append($td5).append($td6).append($td7).append($td8).append($td9).append($td10);

    $("#tbody").append(tr);
}

/**
 * @title: deleteSelectedRow
 * @author: chenguangju
 * @date: 2019/9/23 10:32
 * @description: 疫苗登记删除行
 * @param:
 * @return:
 */
function deleteSelectedRow(rowID) {
    confirmLayer("确定删除该行吗？",function () {
        $("#" + rowID).remove();
        $("#tbody").find("tr").each(function(i){
            $(this).find("td:first").text(i+1);
        });
    });
}
/**
 * @title：findEntNo
 * @author：chenguangju
 * @date：2019/9/20 8:37
 * @description：根据企业名称获取对应的企业代码
 * @param：entNameId entNoId
 * @return：entNo
 */
function findEntNo(entName,labelId) {
    var entName = $("#"+entName).val();
    $.ajax({
        type:'post',
        data:"entName="+entName,
        url:ctx+'/imm/vaccine/findEntNo',
        success:function (data) {
            $("#label"+labelId).html(data);
        }
    })
}

/**
 * @title: vaccineSave
 * @author: chenguangju
 * @date: 2019/9/23 17:52
 * @description: 保存
 * @param:  0没值 1有值
 * @return: 
 */
function vaccineSave() {
    var trLen = $("#tbody").find("tr").length; //遍历所有的tr，若tr中的数据填写的不完整，不允许保存
    var vaccineDataArray = []; //定义对象数组
    var b = 0; //判断数据是否都为空
    $("#tbody").find("tr").each(function(i){
        //校验数据
        var entName = $("#entName"+(i+1)).find("option:selected").text();
        var vaccineName = $("#vaccineName"+(i+1)).val();
        var batchNo = $("#batchNo"+(i+1)).val();
        var validityMonth = $("#validityMonth"+(i+1)).val();
        var produceDate = $("#produceDate"+(i+1)).val();
        var validityAmount = $("#validityAmount"+(i+1)).val();
        var specs_code = $("#specs_code"+(i+1)).find("option:selected").text();
        var a = ("--请选择--"==entName?0:1) + (''==vaccineName?0:1) + (''==batchNo?0:1) +
            (''==validityMonth?0:1) + (''!=produceDate?0:1) + (''==validityAmount?0:1) + ("--请选择--"==specs_code?0:1);
        console.log(("--请选择--"==entName?0:1)+"  "+(''==vaccineName?0:1) +" "+" "+(''==batchNo?0:1)+" "+(''==validityMonth?0:1)
        +" "+(''!=produceDate?0:1)+" "+(''==validityAmount?0:1)+" "+("--请选择--"==specs_code?0:1))
        console.log("aaaaaaa"+a)
        if(a==6){
            //json对象
            var data = {
                entName:entName,
                vaccineName:vaccineName,
                batchNo:batchNo,
                validityMonth:validityMonth,
                produceDate:produceDate,
                validityAmount:validityAmount,
                vaccineSpecsCode:specs_code
            };
            //把json对象存入json对象数组中
            vaccineDataArray.push(data);
        }
        b += a;
    });
    console.log(vaccineDataArray)
    if(b==0) {   //没有数据
        layer.msg("您没有填写数据，不能保存！")
    }else if(b%6>0&&b%6<6){
        layer.msg("请把信息填写完整");
    } else if(b%6==0){   //把json对象数组传入后台
        layer.confirm("确定保存数据吗？", function() {
            $.ajax({
                url : ctx + "/imm/vaccine/save",
                type : "post",
                contentType : "application/json;charset=UTF-8" ,
                dataType: 'json',
                data : JSON.stringify({vaccineArray:vaccineDataArray})
            }).then(function(result) {
                if (0 === result.code) {
                    layer.msg("保存成功", {
                        icon : 1,
                        time : 1000
                    }, function() {
                        $("#btnSubmit").click();
                    });
                } else {
                    layer.msg(result.message, {
                        icon : 2,
                        time : 1000
                    });
                }
            });
        });
    }
}

/**
 * @title: vaccineListUpdate
 * @author: chenguangju
 * @date: 2019/9/25 9:41
 * @description: 疫苗修改
 * @param: id
 */
function vaccineModifyForm(id) {
    layer.open({
        type : 2,
        title : "修改疫苗信息",
        skin : "save", /* 自定义按钮样式 */
        anim : "5", /* 动画效果 */
        area : [ "500px", "400px" ],
        content : ctx + "/imm/vaccine/form/update?id=" + id,
        btn : [ "确定" ],
        yes : function(index, layero) {
            var $win = window[layero.find("iframe")[0]["name"]];
            $win.$("#inputForm").submit();
        }
    });
}

/**
 * @title: vaccineUpdate
 * @author: chenguangju
 * @date: 2019/9/25 10:44
 * @description: 编辑
 * @param: form
 */
function vaccineUpdate(form) {
    $.ajax({
        url : ctx + "/imm/vaccine/update",
        type : "post",
        data : $(form).serialize()
    }).then(function(result) {
        console.log(result)
        if (0 === result.code) {
            layer.msg("修改成功", {
                icon : 1,
                time : 1000
            }, function() {
                $("#btnSubmit", parent.document).click();
            });
        } else {
            layer.msg(result.message, {
                icon : 2,
                time : 1000
            });
        }
    });
}

/**
 * @title: vaccineDelete
 * @author: chenguangju
 * @date: 2019/9/25 12:52
 * @description: 删除
 * @param: id
 */
function vaccineDelete(id) {
    layer.confirm("是否确认删除该疫苗信息？",{title:"删除疫苗信息"
    },function() {
        $.ajax({
            url : ctx + "/imm/vaccine/delete",
            type : "post",
            data : "id=" + id
        }).then(function(result) {
            console.log(result)
            if (0 === result.code) {
                layer.msg("删除成功！", {
                    icon : 1,
                    time : 1000
                }, function() {
                    $("#btnSubmit").click();
                });
            } else {
                layer.msg(result.message, {
                    icon : 2,
                    time : 1000
                });
            }
        });
    });
}