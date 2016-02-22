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

  var userRoles = [
      {
          id: 0,
          value: 'admin',
          title: 'Администратор'
      },
      {
          id: 1,
          value: 'companyAdmin',
          title: 'Администратор компании'
      },
      {
          id: 2,
          value: 'deptHead',
          title: 'Руководитель отдела'
      },
      {
          id: 3,
          value: 'sale',
          title: 'Сотрудник отдела продаж'
      },
      {
          id: 4,
          value: 'provider',
          title: 'Сотрудник отдела обеспечения'
      }
  ];

    function roleToId(role) {
        for(var i = 0; i < userRoles.length; i++){
            if(userRoles[i].value === role)
                return userRoles[i].id;
        }
    }

    function idToRole(id) {
        for(var i = 0; i < userRoles.length; i++){
            if(userRoles[i].id === id)
                return userRoles[i].title;
        }
    }

    var companyDomains = [
        {id: 0, title: 'Розничная торговля / Продажи'},
        {id: 1, title: 'Транспорт / логистика'},
        {id: 2, title: 'Строительство'},
        {id: 3, title: 'Бары / рестораны'},
        {id: 4, title: 'Юриспруденция и бухгалтерия'},
        {id: 5, title: 'Охрана / безопасность'},
        {id: 6, title: 'Домашний персонал'},
        {id: 7, title: 'Красота / фитнес / спорт'},
        {id: 8, title: 'Туризм / отдых / развлечения'},
        {id: 9, title: 'Образование'},
        {id: 10, title: 'Культура / искусство'},
        {id: 11, title: 'Медицина / фармация'},
        {id: 12, title: 'ИТ / телеком / компьютеры'},
        {id: 13, title: 'Недвижимость'},
        {id: 14, title: 'Маркетинг / реклама / дизайн'},
        {id: 15, title: 'Производство / энергетика'},
        {id: 16, title: 'Cекретариат / АХО'},
        {id: 17, title: 'Сервис и быт'},
        {id: 18, title: 'Другие сферы занятий'}
        ];

    var requestsPriorities = [
        {id: 0, title: 'Нормальный'},
        {id: 1, title: 'Высокий'},
        {id: 2, title: 'Низкий'}
    ];

    var app = angular.module('store', ['datatables', 'ngResource', 'ngRoute', 'ui.router', 'ngMaterial', 'angularFileUpload']);

    app.directive('convertToNumber', function(){
        return {
            require: 'ngModel',
            link: function(scope, element, attrs, ngModel) {
                ngModel.$parsers.push(function(val) {
                    return parseInt(val, 10);
                });
                ngModel.$formatters.push(function(val) {
                    return '' + val;
                });
            }
        };
    });

    app.directive('ngThumb', ['$window', function($window) {
        var helper = {
            support: !!($window.FileReader && $window.CanvasRenderingContext2D),
            isFile: function (item) {
                return angular.isObject(item) && item instanceof $window.File;
            },
            isImage: function (file) {
                var type = '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        };

        return {
            restrict: 'A',
            template: '<canvas/>',
            link: function (scope, element, attributes) {
                if (!helper.support) return;

                var params = scope.$eval(attributes.ngThumb);

                if (!helper.isFile(params.file)) return;
                if (!helper.isImage(params.file)) return;

                var canvas = element.find('canvas');
                var reader = new FileReader();

                reader.onload = onLoadFile;
                reader.readAsDataURL(params.file);

                function onLoadFile(event) {
                    var img = new Image();
                    img.onload = onLoadImage;
                    img.src = event.target.result;
                }

                function onLoadImage() {
                    var width = params.width || this.width / this.height * params.height;
                    var height = params.height || this.height / this.width * params.width;
                    canvas.attr({width: width, height: height});
                    canvas[0].getContext('2d').drawImage(this, 0, 0, width, height);
                }
            }
        };
    }]);

    app.filter('idtorole', function(){
        return function(input){
            return idToRole(input);
        };
    });
    app.filter('toDomainTitle', function(){
       return function(input){
           for(var i = 0; i < companyDomains.length; i++){
               if(companyDomains[i].id === input)
                   return companyDomains[i].title;
           }
       };
    });
    app.filter('priorityToText', function () {
       return function(input){
           for(var i = 0; i < requestsPriorities.length; i++) {
               if(requestsPriorities[i].id === input)
                   return requestsPriorities[i].title;
           }
       }
    });

    app.config(function($stateProvider, $urlRouterProvider){
      $urlRouterProvider.otherwise('/login');

      $stateProvider
        .state('login',{
          url: "/login",
          templateUrl: 'lib/view/login-form.html'
        })
        .state('dashboard', {
          url: "/dashboard",
          templateUrl: 'lib/view/dashboard.html',
          access: {
            requiresLogin: true
          }
        })
        .state('dashboard.main', {
          url: '/main',
          templateUrl: 'lib/view/info.html',
          access: {
            requiresLogin: true
          }
        })
        .state('dashboard.users', {
          url: '/users',
          templateUrl: 'lib/view/table-users.html',
          access: {
            requiresLogin: true
          }
        })
          .state('dashboard.addUser', {
              url: '/addUser',
              templateUrl: 'lib/view/add-user.html',
              access: {
                  requiresLogin: true
              }
          })
          .state('dashboard.editUser', {
              url: '/editUser/:id',
              templateUrl: 'lib/view/edit-user-profile.html',
              controller: 'userController',
              access: {
                  requiresLogin: true
              }
          })
        .state('dashboard.companies', {
          url: '/companies',
          templateUrl: 'lib/view/table-companies.html',
          access: {
            requiresLogin: true
          }
        })
          .state('dashboard.addCompany', {
              url: '/addCompany',
              templateUrl: 'lib/view/add-company.html',
              access: {
                  requiresLogin: true
              }
          })
          .state('dashboard.editCompany', {
              url: '/editCompany/:id',
              templateUrl: 'lib/view/edit-company-profile.html',
              access: {
                  requiresLogin: true
              }
          })
          .state('dashboard.invoices', {
              url: '/invoices',
              templateUrl: 'lib/view/table-request.html',
              access: {
                  requiresLogin: true
              }
          })
          .state('dashboard.addRequest', {
              url: '/addRequest',
              templateUrl: 'lib/view/request-details.html',
              access: {
                  requiresLogin: true
              }
          })
          .state('dashboard.editRequest', {
              url: '/editRequest/:id',
              templateUrl: 'lib/view/request-details.html',
              access: {
                  requiresLogin: true
              }
          })
        ;
    });

    app.factory('sessionService', function(){
        var sessionService = {
            isLogged : false,
            sessionData : undefined
        };

        return sessionService;
    });

    app.run(['$rootScope', '$location', 'sessionService', function($rootScope, $location, sessionService){
        $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
            if(toState.access !== undefined){
                if(sessionService.isLogged === false) {
                    $location.path('/login');
                    event.preventDefault();
                }
            }
        });
    }]);

    app.controller("CompanyController", ['$scope', '$http', '$resource', '$location', '$state', '$stateParams', 'sessionService', function ($scope, $http, $resource, $location, $state, $stateParams, sessionService) {
        var vm = this;
        $scope.company = {};

        if($stateParams.id !== undefined) {
          $resource('/api/v0/company/' + $stateParams.id).get().$promise.then(function(company){
            $scope.company = company;
          });
        }

        this.companyDomains = companyDomains;

        this.isGlobalAdminUser = function(){
          return sessionService.sessionData.role === 0;
        };

        this.addCompany = function () {
            var payload = $scope.company;
            $http.put('/api/v0/companies', payload).then(
                function success(data){
                    $location.path('/dashboard/companies').replace();
                },
                function fail(data){
                    alert('Something went wrong');
                }
            );
        };

        this.updateCompany = function () {
            var payload = $scope.company;
            $http.post('/api/v0/companies', payload).then(
                function success(data){
                    $location.path('/dashboard/companies').replace();
                },
                function fail(data){
                    alert('Something went wrong');
                }
            );
        };

        if(sessionService.sessionData !== undefined) {
            $resource('/api/v0/companies').query().$promise.then(function(companies){
                vm.allCompaniesList = companies;
            });
        }
    }]);

    app.controller("invoiceController", ['$scope', '$http', '$resource', '$location', '$state', '$stateParams', 'FileUploader', 'sessionService', function($scope, $http, $resource, $location, $state, $stateParams, FileUploader, sessionService){
      var vm = this;
      $scope.invoice = {};

        $scope.fileUploader = new FileUploader({
            url: '/api/v0/upload',
            method: 'PUT'
        });
        $scope.fileUploader.filters.push({
            name: 'imageFilter',
            fn: function(item, options){
                var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        });
        $scope.fileUploader.onBeforeUploadItem = function(item){
            item.formData.push({invoiceId: $scope.invoiceId});
        };

        if($stateParams.id !== undefined) {
            $scope.invoiceId = $stateParams.id;
            $resource('/api/v0/invoice/' + $scope.invoiceId).get().$promise.then(function(invoice){
                $scope.invoice = invoice;
            });
        }

      this.requestsPriorities = requestsPriorities;

        this.isEditMode = function(){
          return $scope.invoiceId !== undefined;
        };

        this.processInvoice = function(){
            if(this.isEditMode())
                this.updateInvoice();
            else
                this.addInvoice();
        };

      this.addInvoice = function(){
        var payload = $scope.invoice;
        payload.creatorId = sessionService.sessionData.id;
        payload.companyId = sessionService.sessionData.companyId;
        $http.put('/api/v0/invoices', payload).then(
          function success(data){
              $scope.invoiceId = data.data.id;
              $scope.fileUploader.onCompleteAll = function() {
                  $location.path('/dashboard/invoices').replace();
              };
              $scope.fileUploader.uploadAll();
          },
          function fail(data){
            alert('Can\'t add invoice');
          }
        );
      };

        this.updateInvoice = function(){
            var payload = $scope.invoice;
            var invoiceId = $scope.invoiceId;
            payload.id = invoiceId;
            payload.creationTime = $scope.invoice.creationTime.$date;
            $http.post('/api/v0/invoices', payload).then(
                function success(data){
                    $scope.invoiceId = data.data.id;
                    $scope.fileUploader.onCompleteAll = function() {
                        $location.path('/dashboard/invoices').replace();
                    };
                    $scope.fileUploader.uploadAll();
                },
                function fail(data){
                    alert('Can\'t update invoice');
                }
            );
        };

      if(sessionService.sessionData !== undefined) {
        $resource('/api/v0/invoices').query().$promise.then(function(invoices){
          vm.allInvoices = invoices;
        });
        $resource('/api/v0/users').query().$promise.then(function (users){
          vm.users = users;
        });
        vm.sessionData = sessionService.sessionData;
      }
    }]);

    app.controller("userController", ['$scope', '$window', '$http', '$resource', '$location', '$state', '$stateParams', 'sessionService', function ($scope, $window, $http, $resource, $location, $state, $stateParams, sessionService) {
        $scope.loginForm = {};
        $scope.editProfileForm = {};

        if($stateParams.id !== undefined) {
            $scope.userId = $stateParams.id;
            $resource('/api/v0/user/' + $scope.userId).get().$promise.then(function(user){
                $scope.user = user;
                $scope.user.role = idToRole(user.role);
            });
        }

        var vm = this;

        $window.onunload = function(evt){
            $scope.logout();
        };

        this.userRoles = userRoles;

        this.isAdminUser = function(){
          var role = sessionService.sessionData.role;
          return role === 0 || role === 1;
        };

        this.updateUser = function (){
            var user = $scope.user;
            var userId = $scope.userId;
            var userPayload = {
                id: userId,
                login: user.login,
                password: md5(user.password),
                name: user.name,
                email: user.email,
                phone: user.phone,
                address: user.address
            };
            $http.post('/api/v0/users', userPayload).then(
                function success(data){
                    $location.path('/dashboard/users').replace();
                },
                function fail(data){
                    alert('User update failed');
                }
            );
        };

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
                                sessionService.isLogged = true;
                                $location.path('/dashboard/addUser').replace();
                            }
                        }
                    } else if (resp.id !== undefined && resp.name !== undefined && resp.role !== undefined) {
                        sessionService.sessionData = resp;
                        sessionService.isLogged = true;
                        $location.path('/dashboard/main').replace();
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
            sessionService.isLogged = false;
            sessionService.sessionData = undefined;
        };

        $scope.addUser = function() {
            if($scope.user) {
                var addUserPayload = {
                    login: $scope.user.login,
                    password: md5($scope.user.password),
                    name: $scope.user.userFirstName + ' ' + $scope.user.userLastName,
                    companyId: ($scope.user.companyId === undefined) ? "" : $scope.user.companyId,
                    role: roleToId($scope.user.role)};
                $http.put('/api/v0/users', addUserPayload).then(
                    function success(data) {
                        $location.path('/dashboard/users').replace();
                    },
                    function fail(data) {
                        alert('Somethong went wrong');
                    }
                );
            } else {
                console.log("User not defined");
            }
        };

        if(sessionService.sessionData !== undefined) {
            $resource('/api/v0/users').query().$promise.then(function (persons) {
                vm.allUsersList = persons;
            });
            $resource('/api/v0/companies').query().$promise.then(function (companies){
                vm.companies = companies;
            });
        }
    }]);

    app.controller("InfoController", ['$resource', function ($resource) {
      var vm = this;
      $resource('/api/v0/stats/users').get().$promise.then(function(users){
        vm.users = users;
      });
      $resource('/api/v0/stats/companies').get().$promise.then(function(companies){
        vm.companies = companies;
      });
      $resource('/api/v0/stats/invoices').get().$promise.then(function(invoices){
        vm.invoices = invoices;
      });
        this.alltodo = todoTable.length;
        this.alltasks = tasksTable.length;
    }]);

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

    app.directive("todoList", function () {
        return {
            restrict: 'E',
            templateUrl: "lib/view/todo-list.html"
        };
    });

    app.directive('navigationSidebar', function () {
        return {
            restrict: 'E',
            templateUrl: 'lib/view/navigation-sidebar.html'
        };
    });

})();
