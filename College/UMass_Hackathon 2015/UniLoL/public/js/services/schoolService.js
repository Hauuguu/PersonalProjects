app.factory('schoolService', function () {
    var school;

    return {
        saveSchool:function (data) {
            school = data;
            console.log(data);
        },
        getSchool:function () {
            return school;
        }
    };
});