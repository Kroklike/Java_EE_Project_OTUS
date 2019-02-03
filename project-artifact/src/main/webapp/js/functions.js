function addDepart() {
    $("#tblData tbody").append(
        "<tr>" +
        "<td style='display:none'><input type='text'/></td>" +
        "<td><input type='text' required/></td>" +
        "<td><input type='text'/></td>" +
        "<td><input type='text'/></td>" +
        "<td>" +
        "<img src='../images/save.png' class='btnSave'>" +
        "<img src='../images/cancel.png' class='btnCancel'/>" +
        "</td>" +
        "</tr>");

    $(".btnSave").bind("click", saveDepart);
    $(".btnCancel").bind("click", cancelUnsaved);
}

function cancelUnsaved() {
    var par = $(this).parent().parent();
    par.remove();
}

function loadAllDepart() {
    var getAllDepartments = $.get("/api/departments");
    getAllDepartments.done(function (data) {
        for (var i = 0; i < data.length; i++) {
            $("#tblData tbody").append(
                "<tr>" +
                "<td style='display:none'>" + data[i].departmentId + "</td>" +
                "<td>" + data[i].departmentName + "</td>" +
                "<td>" + (data[i].city ? data[i].city : "") + "</td>" +
                "<td>" + (data[i].fullAddress ? data[i].fullAddress : "") + "</td>" +
                "<td>" +
                "<img src='../images/edit.svg' class='btnEdit'>" +
                "<img src='../images/delete.png' class='btnDelete'/>" +
                "</td>" +
                "</tr>");
        }
        $(".btnEdit").bind("click", editDepart);
        $(".btnDelete").bind("click", deleteDepart);
    });
}

function saveDepart() {
    var par = $(this).parent().parent();
    var tdDepartmentId = par.children("td:nth-child(1)");
    var tdDepartmentName = par.children("td:nth-child(2)");
    var tdCity = par.children("td:nth-child(3)");
    var tdFullAddress = par.children("td:nth-child(4)");
    var tdButtons = par.children("td:nth-child(5)");

    var saveDepartment;
    var departmentId = tdDepartmentId.children("input[type=text]").val();
    var departmentName = tdDepartmentName.children("input[type=text]").val();
    var city = tdCity.children("input[type=text]").val();
    var fullAddress = tdFullAddress.children("input[type=text]").val();
    var params = {
        departmentId: departmentId,
        departmentName: departmentName,
        city: city,
        fullAddress: fullAddress
    };

    if (departmentId) {
        saveDepartment = $.put("/api/departments/" + departmentId, params);
        saveDepartment.done(function (data) {
            tdDepartmentId.html(departmentId);
            tdDepartmentName.html(departmentName);
            tdCity.html(city ? city : "");
            tdFullAddress.html(fullAddress ? fullAddress : "");
            tdButtons.html(
                "<img src='../images/edit.svg' class='btnEdit'/>" +
                "<img src='../images/delete.png' class='btnDelete'/>");

            $(".btnEdit").bind("click", editDepart);
            $(".btnDelete").bind("click", deleteDepart);
        });
    } else {
        saveDepartment = $.post("/api/departments", params);
        saveDepartment.done(function (data) {
            tdDepartmentId.html(data);
            tdDepartmentName.html(departmentName);
            tdCity.html(city ? city : "");
            tdFullAddress.html(fullAddress ? fullAddress : "");
            tdButtons.html(
                "<img src='../images/edit.svg' class='btnEdit'/>" +
                "<img src='../images/delete.png' class='btnDelete'/>");

            $(".btnEdit").bind("click", editDepart);
            $(".btnDelete").bind("click", deleteDepart);
        });
    }

}

function editDepart() {
    var par = $(this).parent().parent();
    var tdDepartmentId = par.children("td:nth-child(1)");
    var tdDepartmentName = par.children("td:nth-child(2)");
    var tdCity = par.children("td:nth-child(3)");
    var tdFullAddress = par.children("td:nth-child(4)");
    var tdButtons = par.children("td:nth-child(5)");

    tdDepartmentId.html("<input type='text' value='" + tdDepartmentId.html() + "' required/>");
    tdDepartmentName.html("<input type='text' value='" + tdDepartmentName.html() + "' required/>");
    tdCity.html("<input type='text' value='" + tdCity.html() + "'/>");
    tdFullAddress.html("<input type='text' value='" + tdFullAddress.html() + "'/>");
    tdButtons.html("<img src='../images/save.png' class='btnSave'/>" +
        "<img src='../images/cancel.png' class='btnCancelChanges'/>");

    $(".btnSave").bind("click", saveDepart);
    $(".btnCancelChanges").bind("click", cancelChanges);
}

function cancelChanges() {
    var par = $(this).parent().parent();
    var tdDepartmentId = par.children("td:nth-child(1)");
    var tdDepartmentName = par.children("td:nth-child(2)");
    var tdCity = par.children("td:nth-child(3)");
    var tdFullAddress = par.children("td:nth-child(4)");
    var tdButtons = par.children("td:nth-child(5)");

    var departmentId = tdDepartmentId.children("input[type=text]").val();

    var getAllDepartments = $.get("/api/departments/" + departmentId);
    getAllDepartments.done(function (data) {
        tdDepartmentId.html(data.departmentId);
        tdDepartmentName.html(data.departmentName);
        tdCity.html(data.city ? data.city : "");
        tdFullAddress.html(data.fullAddress ? data.fullAddress : "");
        tdButtons.html(
            "<img src='../images/edit.svg' class='btnEdit'/>" +
            "<img src='../images/delete.png' class='btnDelete'/>");

        $(".btnEdit").bind("click", editDepart);
        $(".btnDelete").bind("click", deleteDepart);
    });
}

function deleteDepart() {
    var par = $(this).parent().parent();
    var tdDepartmentId = par.children("td:nth-child(1)");
    var removeDepartment = $.delete("/api/departments/" + tdDepartmentId.html());
    removeDepartment.done(function (data) {
        par.remove();
    });
}

$.put = function (url, data, callback, type) {

    if ($.isFunction(data)) {
        type = type || callback,
            callback = data,
            data = {}
    }

    return $.ajax({
        url: url,
        type: 'PUT',
        success: callback,
        data: data,
        contentType: type
    });
}

$.delete = function (url, data, callback, type) {

    if ($.isFunction(data)) {
        type = type || callback,
            callback = data,
            data = {}
    }

    return $.ajax({
        url: url,
        type: 'DELETE',
        success: callback,
        data: data,
        contentType: type
    });
}

jQuery.each(["put", "delete"], function (i, method) {
    jQuery[method] = function (url, data, callback, type) {
        if (jQuery.isFunction(data)) {
            type = type || callback;
            callback = data;
            data = undefined;
        }

        return jQuery.ajax({
            url: url,
            type: method,
            dataType: type,
            data: data,
            success: callback
        });
    };
});