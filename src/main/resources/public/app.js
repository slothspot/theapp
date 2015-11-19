var companyTable=[
    {
        companyid:'36046631',
        companyName:'UITC',
        type:'Buy/Sell',
        aboutCompany:'TEST',
        contactDetail:'Номер телефона: +38 (044) 220-18-73, Адрес:Ахматовой 13Д, e-mail:test@test.ua',
        clients:[
            {
                companyUsers:'1',
                clientsId:'4555789',
                contactsUsers:'5'
            }
        ],
        request:[
            {
                number:'1',
                companyUser:'1',
                name:'Test',
                type:'BUY',
                need:'TENDER',
                createdOn:'18-08-2011',
                stat:'WARNING',
                DateTake:'-',
                DateFinish:'-',
                Curator:'-',
                result:'-'
            }
        ]
    },
    {
        companyid:'12345',
        companyName:'TEST',
        type:'Buy/Sell',
        aboutCompany:'TEST',
        contactDetail:'Номер телефона: +38 (044) 220-18-73, Адрес:Ахматовой 13Д, e-mail:test@test.ua',
        clients:[
            {
                companyUsers:'1',
                clientsId:'4555789',
                contactsUsers:'5'
            }
        ]
    }


];

var usersTable=[
    {

        companyid:'36046631',
        companyName:'UITC',
        login:'admin',
        name:'Vasy',
        role:'globalAdmin',
        password:'1234',
        stat:'online',
        createdOn:'default'
    },
    {
        companyid:'12345',
        companyName:'Test',
        login:'User1',
        name:'Petro',
        role:"snab",
        password:'1234',
        stat:'online',
        createdOn:'default'
    }
];

var requestTable=[
    {
        number:'1',
        companyid:'36046631',
        CreateBy:'Силенко С.В.',
        request:'Сапог ПВХ',
        requestImg:'lib/img/requestphotos/1.jpg',
        requestType:'Закупка',
        requestAbout:'Нужно 100пар',
        requestFullAbout:'Модель мужских сапоги предназначена для защиты ног от воды. Область применения данной модели сапог в основном' +
        'связана с работой в условиях повышенной влажности',
        DateCreat:'25-09-2015',
        Stat:'В работе',
        stylestat:'bg-success',
        showeRequest:'true',
        DateTake:'30-09-2015',
        DateFinish:'05-10-2015',
        curator:'User 1',
        result:'-',
        done:false

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
        stylestat:'bg-success',
        showeRequest: 'true',
        DateTake: '30-09-2015',
        DateFinish: '01-10-2015',
        curator: 'User 2',
        result: 'Закуплено',
        done:false
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
        stylestat:'bg-danger',
        showeRequest: 'true',
        DateTake: '-',
        DateFinish: '-',
        curator: 'User 2',
        result: 'Цена не актуальная. Стоимость от 70 грн.',
        done:false

    }
];

var todoTable=[
    {
        text:'Запуск учетной записи',
        done:false
    },
    {
        text:'Создать контрагента',
        done:false
    },
    {
        text:'Создать пользователя',
        done:false
    },
    {
        text:'Проверить заявки',
        done:false
    }

];

var tasksTable=[];

(function() {
    var app=angular.module('store',[]);

    app.controller('TableController',function(){
        this.content=companyTable;

    });

    app.controller("CompanyController", function(){

        this.company = companyTable;

        this.addCompany = function(company){
            companyTable.push(this.company);
            this.company = {};
        };
    });

    app.controller("editController", function() {
           this.editCtrl=function(edit){

               this.id=edit;
               alert(this.id);

           }


    });

    app.controller("userController", function() {
        this.allUsersList=usersTable;
        this.inSystem=function(){

            for(var i=0; i<usersTable.length; i++) {
                if(this.companyIdAutorization==usersTable[i].companyid&&this.loginAutorization==usersTable[i].login&&this.passwordAutorization==usersTable[i].password){
                    this.inSystem=usersTable[i];
                    alert( this.inSystem);
                }
                else
                {
                    alert('Не коректные даные, в базе отсутсвует такой пользователь либо некорректно введена информация, попробуйте еще раз');
                    this.inSystem=[];

                };


            }

        };
    });

    app.controller("InfoController",function(){
        this.allcompany=companyTable.length;
        this.allusers=usersTable.length;
        this.allrequest=requestTable.length;
        this.alltodo=todoTable.length;
        this.alltasks=tasksTable.length;

    });

    app.controller('TodoController',function(){
        this.todos = todoTable;

        this.addTodo = function() {
            todoTable.push({text:this.todoText, done:false});
            this.todoText = '';
        };
        this.remaining = function() {
            var count = 0;
            angular.forEach(this.todos, function(todo) {
                count += todo.done ? 0 : 1;
            });
            return count;
        };

    });

    app.controller('requestController',function(){
        this.request=requestTable;
    });

    app.controller("ContentController", function() {
        this.contentCtrl ='main';

        this.isSet = function(checkTab) {
            return this.contentCtrl === checkTab;
        };

        this.setTab = function(setTab) {
            this.contentCtrl = setTab;
        };
    });


    app.directive("todoList",function(){
        return {
            restrict: 'E',
            templateUrl: "lib/view/todo-list.html"
        };
    });

    app.directive("editUser",function(){
        return {
            restrict: 'E',
            templateUrl: "lib/view/edit-user-profile.html"
        };
    });

    app.directive("editCompany",function(){
        return {
            restrict: 'E',
            templateUrl: "lib/view/edit-company-profile.html"
        };
    });


})();