var companyTable = [
    {
        companyid: '36046631',
        companyName: 'UITC',
        type: 'Buy/Sell',
        aboutCompany: 'TEST',
        contactDetail: 'Номер телефона: +38 (044) 220-18-73, Адрес:Ахматовой 13Д, e-mail:test@test.ua',
        clients: [
            {
                companyUsers: '1',
                clientsId: '4555789',
                contactsUsers: '5'
            }
        ],
        request: [
            {
                number: '1',
                companyUser: '1',
                name: 'Test',
                type: 'BUY',
                need: 'TENDER',
                createdOn: '18-08-2011',
                stat: 'WARNING',
                DateTake: '-',
                DateFinish: '-',
                Curator: '-',
                result: '-'
            }
        ]
    },
    {
        companyid: '12345',
        companyName: 'TEST',
        type: 'Buy/Sell',
        aboutCompany: 'TEST',
        contactDetail: 'Номер телефона: +38 (044) 220-18-73, Адрес:Ахматовой 13Д, e-mail:test@test.ua',
        clients: [
            {
                companyUsers: '1',
                clientsId: '4555789',
                contactsUsers: '5'
            }
        ]
    }


];

var requestTable = [
    {
        number: '1',
        companyid: '36046631',
        CreateBy: 'Силенко С.В.',
        request: 'Сапог ПВХ',
        requestImg: 'lib/img/requestphotos/1.jpg',
        requestType: 'Закупка',
        requestAbout: 'Нужно 100пар',
        requestFullAbout: 'Модель мужских сапоги предназначена для защиты ног от воды. Область применения данной модели сапог в основном' +
        'связана с работой в условиях повышенной влажности',
        DateCreat: '25-09-2015',
        Stat: 'В работе',
        stylestat: 'bg-success',
        showeRequest: 'true',
        DateTake: '30-09-2015',
        DateFinish: '05-10-2015',
        curator: 'User 1',
        result: '-',
        done: false

    },
    {
        number: '2',
        companyid: '36046631',
        CreateBy: 'Силенко С.В.',
        request: 'Халат рабочий',
        requestImg: 'lib/img/requestphotos/2.jpg',
        requestType: 'Закупка',
        requestAbout: 'Нужно 80 шт.',
        DateCreat: '13-09-2015',
        Stat: 'Выполнено',
        stylestat: 'bg-success',
        showeRequest: 'true',
        DateTake: '30-09-2015',
        DateFinish: '01-10-2015',
        curator: 'User 2',
        result: 'Закуплено',
        done: false
    },
    {
        number: '3',
        companyid: '36046631',
        CreateBy: 'Силенко С.В.',
        request: 'Сигнальный жилет',
        requestImg: 'lib/img/requestphotos/3.jpg',
        requestType: 'Просчет',
        requestAbout: 'Нужно 100 шт.Цена не выше 50 грн.',
        DateCreat: '30-09-2015',
        Stat: 'Отказ',
        stylestat: 'bg-danger',
        showeRequest: 'true',
        DateTake: '-',
        DateFinish: '-',
        curator: 'User 2',
        result: 'Цена не актуальная. Стоимость от 70 грн.',
        done: false

    }
];

var todoTable = [
    {
        text: 'Запуск учетной записи',
        done: false
    },
    {
        text: 'Создать контрагента',
        done: false
    },
    {
        text: 'Создать пользователя',
        done: false
    },
    {
        text: 'Проверить заявки',
        done: false
    }

];

var tasksTable = [];

(function () {
  'use strict';
    var app = angular.module('store', []);

    app.controller('TableController', function () {
        this.content = companyTable;

    });

    app.controller("CompanyController", function () {

        this.company = companyTable;

        this.addCompany = function (company) {
            companyTable.push(this.company);
            this.company = {};
        };
    });

    app.controller("editController", function () {
        this.editCtrl = function (edit) {
            this.id = edit;
            alert(this.id);
        };


    });

    app.controller("userController", ['$scope', '$http', function ($scope, $http) {
        $scope.sessionData = {};
        $scope.loginForm = {};
        $scope.editProfileForm = {};

        if ($scope.sessionData.login === undefined) {
            $('#loginForm').modal('show');

            $scope.login = function () {
                var loginPayload = {login: $scope.loginForm.login, password: md5($scope.loginForm.password)};
                $http.post('/api/v0/sessions', loginPayload).then(
                    function success(data) {
                        var resp = data.data;
                        if (resp.result) {
                            if (resp.result === 'error') {
                                alert('Login failed: ' + resp.reason);
                            } else if (resp.result === 'ok') {
                                if (resp.needsSetup && resp.needsSetup === true) {
                                    $('#loginForm').modal('hide');
                                    $('#AddUserModal').modal('show');
                                }
                            }
                        } else if (resp.id !== undefined && resp.name !== undefined && resp.role !== undefined) {
                            $scope.sessionData = resp;
                            $('#loginForm').modal('hide');
                        }
                    },
                    function fail(data) {
                        alert('Login request can\'t be performed');

                    });
            };
            $scope.logout = function () {
                $http.get('/api/v0/sessions').then(
                    function success(data) {
                        console.log('Logout successful');
                        console.log(data.data);
                    },
                    function fail(data) {
                        console.log('Logout failed');
                        console.log(data.data);
                    }
                );
            };
        }

        $scope.saveData = function () {
            if ($scope.editProfileForm.password === undefined || ($scope.editProfileForm.password !== $scope.editProfileForm.cfmPassword)) {
                alert('Password and Confirm Password do not match. Please try again ');
                $('#AddUserModal').modal('show');
                return;
            }
            $scope.SystemUser = {
                userLogin: $scope.editProfileForm.userLogin,
                password: $scope.editProfileForm.password,
                role: $scope.editProfileForm.role
            };
            $scope.OthersUsersInfo = {
                fullName: $scope.editProfileForm.fullName,
                email: $scope.editProfileForm.email,
                phone: $scope.editProfileForm.phone,
                address: $scope.editProfileForm.address,
                companyId: $scope.editProfileForm.companyId
            };
            $('#AddUserModal').modal('hide');
            alert('Hello ' + $scope.SystemUser.userLogin + '. You have successfully changed your data and can now get to work ');
        };

        function roleToId(role) {
            if(role === 'admin') return 0;
            if(role === 'companyAdmin') return 1;
            if(role === 'deptHead') return 2;
            if(role === 'sale') return 3;
            if(role === 'provider') return 4;
        }

        $scope.addUser = function() {
            if($scope.user) {
                console.log($scope.user);
                var addUserPayload = {
                    login: $scope.user.login,
                    password: md5($scope.user.password),
                    name: $scope.user.userFirstName + ' ' + $scope.user.userLastName,
                    companyId: '',
                    role: roleToId($scope.user.role)};
                $http.put('/api/v0/users', addUserPayload).then(
                    function success(data) {
                        console.log('Created user with response: ' + data);
                        $('#AddUserModal').modal('hide');
                        //$('#loginForm').modal('show');
                    },
                    function fail(data) {
                        console.log("Can't create user: " + data);
                    }
                );
            } else {
                console.log("User not defined");
            }
        };
    }]);

    app.controller("InfoController", function () {
        this.allcompany = companyTable.length;
        this.allrequest = requestTable.length;
        this.alltodo = todoTable.length;
        this.alltasks = tasksTable.length;

    });

    app.controller('TodoController', function () {
        this.todos = todoTable;

        this.addTodo = function () {
            todoTable.push({text: this.todoText, done: false});
            this.todoText = '';
        };
        this.remaining = function () {
            var count = 0;
            angular.forEach(this.todos, function (todo) {
                count += todo.done ? 0 : 1;
            });
            return count;
        };

    });

    app.controller('requestController', function () {
        this.request = requestTable;
    });

    app.controller("ContentController", function () {
        this.contentCtrl = 'main';

        this.isSet = function (checkTab) {
            return this.contentCtrl === checkTab;
        };

        this.setTab = function (setTab) {
            this.contentCtrl = setTab;
        };
    });

    app.directive("todoList", function () {
        return {
            restrict: 'E',
            templateUrl: "lib/view/todo-list.html"
        };
    });

    app.directive("editUserProfile", function () {
        return {
            restrict: 'E',
            templateUrl: "lib/view/edit-user-profile.html"
        };
    });

    app.directive("editCompany", function () {
        return {
            restrict: 'E',
            templateUrl: "lib/view/edit-company-profile.html"
        };
    });

    app.directive("loginForm", function () {
        return {
            restrict: 'E',
            templateUrl: 'lib/view/login-form.html'
        };
    });

    app.directive("requestFullForm", function () {
        return {
            restrict: 'E',
            templateUrl: 'lib/view/request-full-form.html'
        };
    });

    app.directive("addCompanyModal", function () {
        return {
            restrict: 'E',
            templateUrl: 'lib/view/add-company-modal.html'
        };
    });

    app.directive("addUserModal", function () {
        return {
            restrict: 'E',
            templateUrl: 'lib/view/add-user-modal.html'
        };
    });

})();
